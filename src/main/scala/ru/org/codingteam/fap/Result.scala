package ru.org.codingteam.fap

import scala.collection.mutable.Stack
import scala.concurrent.Future

class Result(val result: List[Future[Either[Stack[String], Result]]]) {
}
