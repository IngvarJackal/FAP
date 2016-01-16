package ru.org.codingteam.fap

import ru.org.codingteam.fap.commands.{Command, EmptyCmd}

import scala.collection.mutable
import scala.collection.mutable.{Map, Stack}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class State(val stacks: mutable.Map[String, mutable.Stack[String]])(val states: Map[Integer, State]) {
  var outgoing: Set[Arrow] = Set()
  var body: Command = new EmptyCmd()

  private var emptyStack:Stack[String] = Stack()

  private def executeState(state: State):Future[Either[Stack[String], StateResult]] = {
    Future[Either[Stack[String], StateResult]]({
      Right(state.apply(stacks("in").top))
    })
  }

  def apply(char: String): StateResult = {
    var results: mutable.MutableList[Future[Either[Stack[String], StateResult]]] = mutable.MutableList()
    body.apply(stacks) match {
      case Left => {
        for (arrow <- outgoing) {
          results += (arrow.condition match {
            case Literals.ANY if outgoing.forall(_.condition != char) => {
              executeState(arrow.destination)
            }
            case Literals.END if char == Literals.END  => {
              executeState(arrow.destination)
            }
            case orinaryChar: String if orinaryChar == char => {
              executeState(arrow.destination)
            }
            case Literals.EMPTY if char != Literals.END => {
              executeState(arrow.destination)
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
    new StateResult(results.toList)
  }
}
