import controller.KafkaCoreService
import controller.impl.{Controller, PersistenceController}

@main def core(): Unit =
  val persistenceController: PersistenceController = PersistenceController()
  val controller = new Controller(using persistenceController)
  val kafkaCoreService = KafkaCoreService(controller)
