package aview

import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.*
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.ws.{Message, TextMessage, WebSocketRequest}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import model.kafka.Topic
import model.{GameField, Move}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer
import play.api.libs.json.Json
import util.json.JsonReaders.*
import util.json.JsonWriters.*
import util.{Observable, establishWebSocketConnection, handleResponse, sendHttpRequest}

import java.util.Properties
import java.util.concurrent.Executors
import scala.collection.JavaConverters.*
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

class KafkaCoreController extends CoreController:
  implicit val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "CoreController")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  private val topic = Topic.Game

  private val consumerSettings = ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers("localhost:9092")
    .withGroupId("your-static-group-id")
    .withProperty(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, "your-static-instance-id")


  private val source = Consumer.plainSource(
    consumerSettings,
    Subscriptions.topics(topic.name)
  )

  private val flow = Flow[ConsumerRecord[String, String]].map { record =>
    println(s"Received record: $record")
    record
  }

  private val gameFieldSink = Sink.foreach[ConsumerRecord[String, String]] { record =>
    println(s"Processing record: ${record.value()}")
  }

  source.via(flow).to(gameFieldSink).run()
  
  
  private val (gameFieldQueue, gameFieldSource) =
    Source.queue[GameField](
      bufferSize = 100,
      overflowStrategy = OverflowStrategy.dropHead
    ).preMaterialize()

  override def gameFieldStream(): Source[GameField, NotUsed] = gameFieldSource

  override def possibleMoves: Future[List[Move]] = throw InternalError()

  override def move(move: Move): Future[Unit] = throw InternalError()

  override def dice(): Future[Unit] = throw InternalError()

  override def undo(): Future[Unit] = throw InternalError()

  override def redo(): Future[Unit] = throw InternalError()

  override def save(fileName: String): Future[Unit] = throw InternalError()

  override def load(fileName: String): Future[Unit] = throw InternalError()

  override def newGame(): Future[Unit] = throw InternalError()

  override def getTargets: Future[List[String]] = throw InternalError()
