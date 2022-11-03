package de.htwg.se.toybrokersludo.model.model

import de.htwg.se.toybrokersludo.model.Player
import de.htwg.se.toybrokersludo.model.Move
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MoveSpec extends AnyWordSpec with Matchers {
  
  "move" should {

    "have a Player and number" in {
      val move = Move(Player(1,"B"), 0)
      move.player should be(Player(1,"B"))
      move.number should be(0)
    }

  }

}
