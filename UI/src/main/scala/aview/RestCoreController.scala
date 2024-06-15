package aview

import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.*
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.ws.{Message, TextMessage, WebSocketRequest}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import model.{GameField, Move}
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import play.api.libs.json.Json
import util.json.JsonReaders.*
import util.json.JsonWriters.*
import util.{Observable, establishWebSocketConnection, handleResponse, sendHttpRequest}

import java.util.Properties
import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

class RestCoreController extends CoreController:
  implicit val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "CoreController")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  private val (gameFieldQueue, gameFieldSource) =
    Source.queue[GameField](
      bufferSize = 100,
      overflowStrategy = OverflowStrategy.dropHead
    ).preMaterialize()

  def gameFieldStream(): Source[GameField, NotUsed] = gameFieldSource
  
  override def possibleMoves: Future[List[Move]] =
    val request = HttpRequest(uri = "http://core-service:8082/core/possibleMoves")
    sendHttpRequest(request).flatMap { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr).as[List[Move]])
    }

  override def move(move: Move): Future[Unit] =
    val jsonBody = Json.toJson(move).toString()
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = s"http://core-service:8082/core/move",
      entity = HttpEntity(ContentTypes.`application/json`, jsonBody)
    )
    sendHttpRequest(request).map { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr))
    }

  override def dice(): Future[Unit] =
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = s"http://core-service:8082/core/dice"
    )
    sendHttpRequest(request).map { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr))
    }

  override def undo(): Future[Unit] =
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = s"http://core-service:8082/core/undo"
    )
    sendHttpRequest(request).map { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr))
    }

  override def redo(): Future[Unit] =
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = s"http://core-service:8082/core/redo"
    )
    sendHttpRequest(request).map { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr))
    }

  override def save(fileName: String): Future[Unit] = {
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = "http://core-service:8082/core/save",
      entity = HttpEntity(ContentTypes.`application/json`, fileName)
    )
    sendHttpRequest(request).map { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr))
    }
  }

  override def load(fileName: String): Future[Unit] =
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = s"http://core-service:8082/core/load",
      entity = HttpEntity(ContentTypes.`application/json`, fileName)
    )
    sendHttpRequest(request).map { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr))
    }

  override def newGame(): Future[Unit] =
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = s"http://core-service:8082/core/newGame",
      entity = HttpEntity(ContentTypes.`application/json`, "")
    )
    sendHttpRequest(request).map { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr))
    }

  override def getTargets: Future[List[String]] =
    val request = HttpRequest(uri = "http://core-service:8082/core/getTargets")
    sendHttpRequest(request).flatMap { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr).as[List[String]])
    }