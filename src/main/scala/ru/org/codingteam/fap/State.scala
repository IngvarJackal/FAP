package ru.org.codingteam.fap

import com.rits.cloning.Cloner
import ru.org.codingteam.fap.commands.{Command, EmptyCmd}

import scala.collection.mutable
import scala.collection.mutable.{Map, Stack}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class State(val states: Map[Integer, State], var body: Command) {
  var outgoing: Set[Arrow] = Set()

  private var emptyStack:Stack[String] = Stack()

  private val cloner = new Cloner();
  var isSingle = true

  private def executeState(state: State, stacks: mutable.Map[String, mutable.Stack[String]]):Future[Either[Stack[String], Result]] = {
    var stacks2:mutable.Map[String, mutable.Stack[String]] = null
    if (isSingle) {
      stacks2 = stacks
      isSingle = false
    } else {
      stacks2 = cloner.deepClone(stacks)
    }
    Future[Either[Stack[String], Result]]({
      Right(state.apply(stacks("in").top, stacks2))
    })
  }

  def apply(char: String, stacks: mutable.Map[String, mutable.Stack[String]]): Result = {
    var results: mutable.MutableList[Future[Either[Stack[String], Result]]] = mutable.MutableList()
    body.apply(stacks) match {
      case Left(x: Unit) => {
        for (arrow <- outgoing) {
          results += (arrow.condition match {
            case Literals.ANY if outgoing.forall(_.condition != char) => {
              executeState(arrow.destination, stacks)
            }
            case Literals.END if char == Literals.END  => {
              executeState(arrow.destination, stacks)
            }
            case orinaryChar: String if orinaryChar == char => {
              executeState(arrow.destination, stacks)
            }
            case Literals.EMPTY if char != Literals.END => {
              executeState(arrow.destination, stacks)
            }
            case _ => {
              Future(Left(emptyStack.clone()))
            }
          })
        }
      }
      case Right(x: Stack[String]) => {
        results += Future(Left(x))
      }
    }
    new Result(results.toList)
  }
}
