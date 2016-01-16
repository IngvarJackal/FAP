package ru.org.codingteam.fap.commands

import scala.collection.mutable
import scala.collection.mutable.Stack

class EmptyCmd extends Command {
  override def apply(stacks: mutable.Map[String, mutable.Stack[String]]): Either[Unit, Stack[String]] = {
    stacks("in").pop()
    Left()
  }
}
