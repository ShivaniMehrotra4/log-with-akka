package com.knoldus


import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps
import akka.routing.RoundRobinPool

object ActorsMain extends App {

  val path = "/home/knoldus/Music"
  val rd = new ReadDirectory
  val listOfFiles = rd.getListOfFile(path).map(_.toString)
  val actorSystem = ActorSystem("First-Actor-System")
  val myActor = actorSystem.actorOf(RoundRobinPool(5).props(Props[LogActor]).withDispatcher("fixed-thread-pool").withDispatcher("fixed-thread-pool"))
  val x = futureListCountItems(listOfFiles,myActor, List())
  val futureFinalValue = Future.sequence(x).map(an => an.foldLeft(CountItems(0, 0, 0)) { (acc, y) => caseClassMembersAddition(acc, y) })
  val finalResult = Await.result(futureFinalValue, 1 second)
  println(finalResult)

  /**
   * futureList function returns a list that contains all case class objects with future wrapper.
   * @param actorReference - a list of actor references.
   * @param files - a list containing futures of case class objects (initially empty).
   * @return - list of future of case class objects
   */
  @scala.annotation.tailrec
  def futureListCountItems(files: List[String],actorReference : ActorRef, listCountItems: List[Future[CountItems]]): List[Future[CountItems]] = {
    implicit val timeout: Timeout = Timeout(5 second)
    files match {
      case Nil => listCountItems
      case head :: rest =>
        val temp = (actorReference ? fileName(head)).mapTo[CountItems]
        futureListCountItems(rest, actorReference, temp :: listCountItems )
    }
  }

  /**
   * caseClassMembersAddition function performs addition of member's values on two case class objects
   * @param acc - first case class object
   * @param y - second case class object
   * @return - case class object after addition
   */
  def caseClassMembersAddition(acc: CountItems, y: CountItems): CountItems = {
    CountItems(acc.countError + y.countError, acc.countWarnings + y.countWarnings, acc.countInfo + y.countInfo)
  }


}
