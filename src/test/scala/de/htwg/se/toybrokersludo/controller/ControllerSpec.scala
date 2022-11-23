package de.htwg.se.toybrokersludo.controller

import de.htwg.se.toybrokersludo.model.{Field, Matrix, Move, Player, Stone}
import de.htwg.se.toybrokersludo.aview.TUI
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class ControllerSpec extends AnyWordSpec with Matchers  {

  val eol = "\n"

  val map: List[List[Stone]] = List(
    List(
      Stone(true, 0, None), Stone(false, -1, None)),
    List(
      Stone(true, 1, None), Stone(false, -1, None)
    ))

  val field = Field(Matrix(map))
  val controller = Controller(field)
  val tui = TUI(controller)

  controller.doAndPublish(controller.put, Move(Player(0, "B"), 0))

  "The Controller" should  {
    "can put" in {
      controller.field.matrix.map should equal(List(
        List(Stone(true, 0, Option(Player(0, "B"))), Stone(false, -1, None)),
        List(Stone(true, 1, None), Stone(false, -1, None)))
      )
    }

    "can publish" in {
      val move = Move(Player(0, "B"), 1)
      controller.doAndPublish(controller.put,move).toString should equal (
        "()"
      )
    }

    "can startup player count 1" in {
      val field2 = Field(Matrix())
      val controller2 = Controller(field2)
      controller2.startup(1).toString should be(
        "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "| G1 |      | G2 |      |    ||    ||    |      |    |      |    |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "| G3 |      | G4 |      |    ||    ||    |      |    |      |    |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "|    ||    ||    ||    ||    ||    ||    ||    ||    ||    ||    |" + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "+----++----++----++----++----+      +----++----++----++----++----+" + eol +
          "|    ||    ||    ||    ||    |      |    ||    ||    ||    ||    |" + eol +
          "+----++----++----++----++----+      +----++----++----++----++----+" + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "|    ||    ||    ||    ||    ||    ||    ||    ||    ||    ||    |" + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "|    |      |    |      |    ||    ||    |      |    |      |    |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "|    |      |    |      |    ||    ||    |      |    |      |    |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol

      )
    }

    "can startup player count 2" in {
      val field2 = Field(Matrix())
      val controller2 = Controller(field2)
      controller2.startup(2).toString should be(
        "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "| G1 |      | G2 |      |    ||    ||    |      | R1 |      | R2 |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "| G3 |      | G4 |      |    ||    ||    |      | R3 |      | R4 |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "|    ||    ||    ||    ||    ||    ||    ||    ||    ||    ||    |" + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "+----++----++----++----++----+      +----++----++----++----++----+" + eol +
          "|    ||    ||    ||    ||    |      |    ||    ||    ||    ||    |" + eol +
          "+----++----++----++----++----+      +----++----++----++----++----+" + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "|    ||    ||    ||    ||    ||    ||    ||    ||    ||    ||    |" + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "|    |      |    |      |    ||    ||    |      |    |      |    |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "|    |      |    |      |    ||    ||    |      |    |      |    |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol
      )
    }
    "can startup player count 3" in {
      val field2 = Field(Matrix())
      val controller2 = Controller(field2)
      controller2.startup(3).toString should be(
        "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "| G1 |      | G2 |      |    ||    ||    |      | R1 |      | R2 |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "| G3 |      | G4 |      |    ||    ||    |      | R3 |      | R4 |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "|    ||    ||    ||    ||    ||    ||    ||    ||    ||    ||    |" + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "+----++----++----++----++----+      +----++----++----++----++----+" + eol +
          "|    ||    ||    ||    ||    |      |    ||    ||    ||    ||    |" + eol +
          "+----++----++----++----++----+      +----++----++----++----++----+" + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "|    ||    ||    ||    ||    ||    ||    ||    ||    ||    ||    |" + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "| Y1 |      | Y2 |      |    ||    ||    |      |    |      |    |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "| Y3 |      | Y4 |      |    ||    ||    |      |    |      |    |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol
      )
    }
    "can startup player count 4" in {
      val field2 = Field(Matrix())
      val controller2 = Controller(field2)
      controller2.startup(4).toString should be(
        "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "| G1 |      | G2 |      |    ||    ||    |      | R1 |      | R2 |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "| G3 |      | G4 |      |    ||    ||    |      | R3 |      | R4 |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "|    ||    ||    ||    ||    ||    ||    ||    ||    ||    ||    |" + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "+----++----++----++----++----+      +----++----++----++----++----+" + eol +
          "|    ||    ||    ||    ||    |      |    ||    ||    ||    ||    |" + eol +
          "+----++----++----++----++----+      +----++----++----++----++----+" + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "|    ||    ||    ||    ||    ||    ||    ||    ||    ||    ||    |" + eol +
          "+----++----++----++----++----++----++----++----++----++----++----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "| Y1 |      | Y2 |      |    ||    ||    |      | B1 |      | B2 |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "                        +----++----++----+                        " + eol +
          "                        |    ||    ||    |                        " + eol +
          "                        +----++----++----+                        " + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol +
          "| Y3 |      | Y4 |      |    ||    ||    |      | B3 |      | B4 |" + eol +
          "+----+      +----+      +----++----++----+      +----+      +----+" + eol
      )
    }
  }
}
