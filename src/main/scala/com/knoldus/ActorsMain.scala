package com.knoldus

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps


object ActorsMain extends App {

  val path = "/home/knoldus/Documents/Assignment-Akka/akka-actors-log-assignment/src/main/resources/SampleFolderLogs"
  val rd = new ReadDirectory
  val listOfFiles = rd.getListOfFile(path).map(_.toString)

  val actorSystem = ActorSystem("First-Actor-System")
  val listOfActorRef = mine(listOfFiles, List())
  val x = futureList(listOfActorRef, List())
  val almostFinal = Future.sequence(x).map(an => an.foldLeft(newDataStrucutre(0, 0, 0)) { (acc, y) => caseClassMembersAddition(acc, y) })
  val finalResult = Await.result(almostFinal, 1 second)
  println(finalResult)

  /**
   * mine function basically calls the actors for each individual files.
   * @param listOfFiles - list of file names in a directory
   * @param listOfActorRef - list of ActorRef (references to an actor)
   * @return - a list of ActorRef
   */
  @scala.annotation.tailrec
  def mine(listOfFiles: List[String], listOfActorRef: List[ActorRef]): List[ActorRef] = {
    listOfFiles match {
      case Nil => listOfActorRef
      case head :: rest =>
        val myActor = actorSystem.actorOf(Props[LogActor])
        myActor ! fileName(head)
        mine(rest, myActor :: listOfActorRef)

    }
  }

  /**
   * futureList function returns a list that contains all case class objects with future wrapper.
   * @param value - a list of actor references.
   * @param futureLst - a list containing futures of case class objects (initially empty).
   * @return - list of future of case class objects
   */
  @scala.annotation.tailrec
  def futureList(value: List[ActorRef], futureLst: List[Future[newDataStrucutre]]): List[Future[newDataStrucutre]] = {
    implicit val timeout: Timeout = Timeout(5 second)
    value match {
      case Nil => futureLst
      case head :: rest =>
        val temp = (head ? "shivani").mapTo[newDataStrucutre]
        futureList(rest, temp :: futureLst)
    }
  }

  /**
   * f2 function performs addition of member's values on two case class objects
   * @param acc - first case class object
   * @param y - second case class object
   * @return - case class object after addition
   */
  def caseClassMembersAddition(acc: newDataStrucutre, y: newDataStrucutre): newDataStrucutre = {
    newDataStrucutre(acc.countError + y.countError, acc.countWarnings + y.countWarnings, acc.countInfo + y.countInfo)
  }


}
