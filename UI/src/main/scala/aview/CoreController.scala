package aview

import akka.NotUsed
import akka.stream.scaladsl.Source
import model.{GameField, Move}

import scala.concurrent.Future

trait CoreController {

  def gameFieldStream(): Source[GameField, NotUsed]

  def possibleMoves: Future[List[Move]]

  def move(move: Move): Future[Unit]

  def dice(): Future[Unit]

  def undo(): Future[Unit]

  def redo(): Future[Unit]

  def save(fileName: String): Future[Unit]

  def load(fileName: String): Future[Unit]

  def newGame(): Future[Unit]

  def getTargets: Future[List[String]]
}
