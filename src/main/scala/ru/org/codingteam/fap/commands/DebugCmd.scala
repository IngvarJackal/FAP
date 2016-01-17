package ru.org.codingteam.fap.commands

import scala.collection.mutable

class DebugCmd(val debugmsg: String = "DEBUG!") extends Command {
  override def apply(stacks: mutable.Map[String, mutable.Stack[String]]): Either[Unit, mutable.Stack[String]] = {
    System.out.println(debugmsg)
    Left()
  }
}
