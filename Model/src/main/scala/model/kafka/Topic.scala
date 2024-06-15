package model.kafka

sealed trait Topic:
  def name: String

object Topic:
  case object Game extends Topic:
    val name: String = "game"

    sealed trait GameKeys:
      def keyName: String

    object GameKeys:
      case object GAME_UPDATES extends GameKeys:
        val keyName: String = "game_updates"

      case object CONTROLLER_COMMANDS extends GameKeys:
        val keyName: String = "controller_commands"

