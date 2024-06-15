import controller.impl.{Controller, PersistenceController}
import controller.{KafkaCoreService, RestCoreAPI}

@main def core(): Unit =
  val persistenceController: PersistenceController = PersistenceController()
  val controller = new Controller(using persistenceController)

  val restCoreAPI = RestCoreAPI(controller)
  val foo = KafkaCoreService(controller)
  
  controller.dice()

