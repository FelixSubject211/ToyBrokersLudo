package de.htwg.se.toybrokersludo.controller

import de.htwg.se.toybrokersludo.model.{Field, Matrix, Move, Player, Stone}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ControllerSpec extends AnyWordSpec with Matchers  {


  val map: List[List[Stone]] = List(
    List(
      Stone(true, 0, None), Stone(false, -1, None)),
    List(
      Stone(true, 1, None), Stone(false, -1, None)
    ))

  val field = Field(Matrix(map))
  val controller = Controller(field)
  val tui = TUI(controller)

  controller.doAndPublish(controller.put, Move(Player(0, "B"), 0), tui)

  "The Controller" should  {
    "can put" in
      controller.field.matrix.map == List(
        List(Stone(true, 0, Option(Player(0, "B"))), Stone(false, -1, None)),
        List(Stone(true, 1, None), Stone(false, -1, None)))
  }
}