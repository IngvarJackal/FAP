package ru.org.codingteam.fap

import scala.collection.mutable.Stack
import scala.concurrent.Future

class StateResult(val result: List[Future[Either[Stack[String], StateResult]]]) {
  // stateId if transition, stack if end state
}
