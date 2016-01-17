package ru.org.codingteam.fap

import java.util.Random

import ru.org.codingteam.fap.commands.{DebugCmd, EmptyCmd, EndCmd}

import scala.collection.mutable
import scala.collection.mutable.{Map, Stack}
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class Automaton {

  var stacks: Map[String, Stack[String]] = Map()
  var states: Map[Integer, State] = Map()
  val rand = new Random(System.currentTimeMillis());

  private def choice[T](list:mutable.MutableList[T]): Option[T] = {
    if (list.length > 0)
      Some(list(rand.nextInt(list.length)))
    else
      None
  }

  private def flatten(executionResult: Result, results: mutable.MutableList[Stack[String]]): Unit= {
    for (future: Future[Either[Stack[String], Result]] <- executionResult.result) {
      Await.result(future, 500000.hours) match { // random big number
        case Left(l) => {
          results += l
        }
        case Right(r) => {
          flatten(r, results)
        }
      }
    }
  }

  def execute(inStack: Stack[String]): Stack[String] = {
    stacks("in") = inStack
    // TODO: read states

    /*
      1 start
      2 debug
      3 end in
      #
      1 2 \?
      2 3 \?
     */

    states += ((1, new State(states, new EmptyCmd())))
    states += ((2, new State(states, new DebugCmd())))
    states += ((3, new State(states, new EndCmd("in"))))

    states(1).outgoing += new Arrow(states(2), Literals.ANY)
    states(2).outgoing += new Arrow(states(3), Literals.ANY)


    // TODO: start state

    // TODO: reduce results and return result

    val results = new mutable.MutableList[Stack[String]]()

    val a = states(1).apply(stacks("in").top, stacks)
    flatten(a, results)

    choice(results) match {
      case Some(x) => x
      case None => new Stack[String]()
    }
  }

}
