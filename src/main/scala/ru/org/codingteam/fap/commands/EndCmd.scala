package ru.org.codingteam.fap.commands

import scala.collection.mutable

class EndCmd(stackName: String) extends Command {
  override def apply(stacks: mutable.Map[String, mutable.Stack[String]]): Either[Unit, mutable.Stack[String]] = {
    Right(stacks(stackName))
  }
}
