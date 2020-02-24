package com.knoldus

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

object ActorsMain extends App {

  //  val rd = new ReadDirectory
  //  val path = "/home/knoldus/Documents/SampleFolderLogs/stdout"
  //  val listOfFiles = rd.getListOfFile(path)


  val actorSystem = ActorSystem("First-Actor-System")
  val myActor = actorSystem.actorOf(Props[LogActor], "countLog")

  implicit val timeout: Timeout = Timeout(10 seconds)

  val numOfErrors = myActor ? "Error"
  val numOfWarnings = myActor ? "Warning"
  val numOfInfo = myActor ? "Info"

  numOfErrors.map(result => println(result))
  numOfWarnings.map(result => println(result))
  numOfInfo.map(result => println(result))

}
