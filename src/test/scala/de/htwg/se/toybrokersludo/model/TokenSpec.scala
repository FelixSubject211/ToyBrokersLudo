package de.htwg.se.toybrokersludo.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TokenSpec extends AnyWordSpec with Matchers {

  "token" should {
    "have a number and color" in {
      val player = PlayToken.apply(0,"Y")
      player.getNumber() should equal (0)
      player.getColor() should equal ("Y")
    }

    "can convert Color and Number to String" in {
      val player = PlayToken.apply(1,"B")
      player.toString() should equal("B1")
    }
  }
}