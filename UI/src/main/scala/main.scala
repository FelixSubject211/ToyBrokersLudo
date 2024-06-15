import aview.{KafkaCoreController, RestCoreController, Tui}

@main def ui(): Unit =
  val coreController = KafkaCoreController()
  val tui = Tui(coreController)
  tui.inputLoop()
