package ru.org.codingteam.fap.commands

import scala.collection.mutable.{Map, Stack}

abstract class Command {
  def apply(stacks: Map[String, Stack[String]]): Either[Unit, Stack[String]]
}
