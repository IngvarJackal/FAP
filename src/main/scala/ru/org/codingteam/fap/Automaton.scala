package ru.org.codingteam.fap

import scala.collection.mutable.Stack
import scala.collection.mutable.Map

class Automaton {
  var stacks: Map[String, Stack[String]] = Map()
  var states: Map[Integer, State] = Map()

  def readInput(input: String) = {
    val inStack = Stack[String]()
    for (char <- input) {
      inStack.push(char.toString)
    }
    stacks("in") = inStack
    // TODO: read states

    // TODO: start state

    // TODO: reduce results and return result
  }

}
