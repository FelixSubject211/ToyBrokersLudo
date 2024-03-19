package de.htwg.se.toybrokersludo.model

import de.htwg.se.toybrokersludo.model.Cell

case class Move(fromIndex: Int, toIndex: Int):
  def fromCell(map: Map[(Int, Int), Cell]): Cell =
    map.find { case (_, cell) => cell.index == fromIndex }.map(_._2).get

  def toCell(map: Map[(Int, Int), Cell]): Cell =
    map.find { case (_, cell) => cell.index == toIndex }.map(_._2).get  

