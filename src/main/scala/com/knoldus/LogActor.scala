package com.knoldus

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.Future
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global

class LogActor extends Actor {
  var numOfErrors = 0
  var numOfWarnings = 0
  var numOfInfo = 0

  override def receive: Receive = {
    case fileName(file) =>
      val fSource = Source.fromFile(s"$file")
      val listOfLines = fSource.getLines().toList
      finder(listOfLines)
      
    case "shivani" => val cd = newDataStrucutre(numOfErrors, numOfWarnings, numOfInfo)
      f1(cd).pipeTo(context.sender())
  }

  def finder(listOfLines: List[String]): newDataStrucutre = {
    listOfLines match {
      case Nil => newDataStrucutre(numOfErrors, numOfWarnings, numOfInfo)
      case head :: rest if head.contains("[ERROR]") => numOfErrors += 1; finder(rest)
      case head :: rest if head.contains("[WARN]") => numOfWarnings += 1; finder(rest)
      case head :: rest if head.contains("[INFO]") => numOfInfo += 1; finder(rest)
      case _ :: rest => finder(rest)
    }
  }

  def f1(cd: newDataStrucutre): Future[newDataStrucutre] = Future {
    cd
  }

}
