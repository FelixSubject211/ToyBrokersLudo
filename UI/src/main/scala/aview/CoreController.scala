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
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import scala.concurrent.{ExecutionContext, Future}
import scala.collection.JavaConverters._
import scala.util.{Failure, Success}


import java.util.Properties
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

class CoreController extends Observable:
  implicit val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "CoreController")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  private val (gameFieldQueue, gameFieldSource) =
    Source.queue[GameField](
      bufferSize = 100,
      overflowStrategy = OverflowStrategy.dropHead
    ).preMaterialize()

  consumeFromKafka()

  private def createConsumerConfig(): Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "game-consumer-group")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer")
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    props
  }

  private val kafkaConsumer = {
    val consumer = new KafkaConsumer[Array[Byte], String](createConsumerConfig())
    consumer.subscribe(java.util.Collections.singletonList("game"))
    consumer
  }

  private def consumeFromKafka()(implicit ec: ExecutionContext): Future[Unit] = Future {
    while (true) {
      val records = kafkaConsumer.poll(java.time.Duration.ofMillis(1000)).asScala
      for (record <- records) {
        println(s"Received message: (key: ${new String(record.key)}, value: ${record.value}) at offset ${record.offset}")
      }
    }
  }

  def gameFieldStream(): Source[GameField, NotUsed] = gameFieldSource
  
  def possibleMoves: Future[List[Move]] =
    val request = HttpRequest(uri = "http://core-service:8082/core/possibleMoves")
    sendHttpRequest(request).flatMap { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr).as[List[Move]])
    }

  def move(move: Move): Future[Unit] =
    val jsonBody = Json.toJson(move).toString()
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = s"http://core-service:8082/core/move",
      entity = HttpEntity(ContentTypes.`application/json`, jsonBody)
    )
    sendHttpRequest(request).map { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr))
    }

  def dice(): Future[Unit] =
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = s"http://core-service:8082/core/dice"
    )
    sendHttpRequest(request).map { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr))
    }

  def undo(): Future[Unit] =
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = s"http://core-service:8082/core/undo"
    )
    sendHttpRequest(request).map { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr))
    }

  def redo(): Future[Unit] =
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = s"http://core-service:8082/core/redo"
    )
    sendHttpRequest(request).map { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr))
    }

  def save(fileName: String): Future[Unit] = {
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = "http://core-service:8082/core/save",
      entity = HttpEntity(ContentTypes.`application/json`, fileName)
    )
    sendHttpRequest(request).map { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr))
    }
  }

  def load(fileName: String): Future[Unit] =
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = s"http://core-service:8082/core/load",
      entity = HttpEntity(ContentTypes.`application/json`, fileName)
    )
    sendHttpRequest(request).map { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr))
    }
    
  def newGame(): Future[Unit] =
    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = s"http://core-service:8082/core/newGame",
      entity = HttpEntity(ContentTypes.`application/json`, "")
    )
    sendHttpRequest(request).map { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr))
    }
  
  def getTargets: Future[List[String]] =
    val request = HttpRequest(uri = "http://core-service:8082/core/getTargets")
    sendHttpRequest(request).flatMap { response =>
      handleResponse(response)(jsonStr => Json.parse(jsonStr).as[List[String]])
    }