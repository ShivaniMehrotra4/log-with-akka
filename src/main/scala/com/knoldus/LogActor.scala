package com.knoldus

import java.io.File

import akka.actor.Actor

import scala.io.Source


class LogActor extends Actor {
  override def receive: Receive = {
    case "Error" =>
      val file = new File("/home/knoldus/Documents/SampleFolderLogs/stdout")
      val fileName = file.toString
      val fSource = Source.fromFile(s"$fileName")
      val countValues = fSource.getLines().flatMap(_.split(" ")).toList.groupBy((word: String) => word).view.mapValues(_.length)
      val x = Map("error" -> countValues.get("[ERROR]"))
      sender() ! Map("Errors" -> x("error").getOrElse(0))

    case "Warning" =>
      val file = new File("/home/knoldus/Documents/SampleFolderLogs/stdout")
      val fileName = file.toString
      val fSource = Source.fromFile(s"$fileName")
      val countValues = fSource.getLines().flatMap(_.split(" ")).toList.groupBy((word: String) => word).view.mapValues(_.length)
      val x = Map("warn" -> countValues.get("[WARN]"))
      sender() ! Map("Warnings" -> x("warn").getOrElse(0))

    case "Info" =>
      val file = new File("/home/knoldus/Documents/SampleFolderLogs/stdout")
      val fileName = file.toString
      val fSource = Source.fromFile(s"$fileName")
      val countValues = fSource.getLines().flatMap(_.split(" ")).toList.groupBy((word: String) => word).view.mapValues(_.length)
      val x = Map("info" -> countValues.get("[INFO]"))
      sender() ! Map("Information" -> x("info").getOrElse(0))

  }
}
