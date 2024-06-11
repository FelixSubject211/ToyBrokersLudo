package gatling

import scala.concurrent.duration._
import io.gatling.core.Predef.*
import io.gatling.http.Predef.*
import io.gatling.jdbc.Predef.*
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.http.request.builder.HttpRequestBuilder
import io.gatling.core.structure.ChainBuilder
import io.gatling.javaapi.core.PopulationBuilder

class ControllerLoadTest extends SimulationTemplate {
  override val operations: List[ChainBuilder] = List(
    buildOperation("API root", "GET", "/", StringBody("")),
  )

  override def executeOperations(): Unit = {
    val scn = buildScenario("Scenario 1")

    setUp(
      scn.inject(
        rampUsers(10) during (10.seconds)
      )

    ).protocols(httpProtocol)
  }

  executeOperations()
}