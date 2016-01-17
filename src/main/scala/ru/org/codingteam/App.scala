package ru.org.codingteam

import ru.org.codingteam.fap.Automaton
import scala.collection.mutable.Stack

object App {
  def main(args: Array[String]): Unit = {
    val inStack = Stack[String]()
    for (char <- "abcdefg") {
      inStack.push(char.toString)
    }
    println(new Automaton().execute(inStack))
  }

}
