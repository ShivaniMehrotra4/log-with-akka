package com.knoldus

import akka.actor.Actor
import akka.pattern.pipe
import scala.concurrent.Future
import scala.io.Source
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * The class LogActor provides Actor functionality.
 */
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

  /**
   * finder function finds Errors,Warnings and Information from each line of each file
   * @param listOfLines - a list of Lines from input file
   * @return - an object of newDatatructure case class containing counts of Errors,Warnings and Information
   */
  def finder(listOfLines: List[String]): newDataStrucutre = {
    listOfLines match {
      case Nil => newDataStrucutre(numOfErrors, numOfWarnings, numOfInfo)
      case head :: rest if head.contains("[ERROR]") => numOfErrors += 1; finder(rest)
      case head :: rest if head.contains("[WARN]") => numOfWarnings += 1; finder(rest)
      case head :: rest if head.contains("[INFO]") => numOfInfo += 1; finder(rest)
      case _ :: rest => finder(rest)
    }
  }

  /**
   * f1 function wraps the parameter object of case class in future
   * @param cd - object of case class newDataStructure
   * @return - Future[case-class-object]
   */
  def f1(cd: newDataStrucutre): Future[newDataStrucutre] = Future {
    cd
  }

}
