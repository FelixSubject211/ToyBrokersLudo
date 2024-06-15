package controller

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.kafka.scaladsl.{Consumer, Producer}
import akka.kafka.{ConsumerSettings, ProducerSettings, Subscriptions}
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{Materializer, SystemMaterializer}
import controller.impl.{Controller, PersistenceController}
import model.kafka.Topic
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import play.api.libs.json.Json
import util.Observer

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class KafkaCoreService(controller: Controller) extends Observer {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "KafkaCoreService")
  implicit val executionContext: ExecutionContext = system.executionContext
  implicit val materializer: Materializer = SystemMaterializer(system).materializer

  private val topic = Topic.Game
  controller.add(this)

  // #################################################################################### Produce

  private val producerSettings = ProducerSettings(system, new StringSerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")

  private val kafkaSink = Producer.plainSink(producerSettings)

  override def update(): Unit = {
    val record = new ProducerRecord[String, String](
      topic.name,
      topic.GameKeys.GAME_UPDATES.keyName,
      "update called"
    )
    Source.single(record).runWith(kafkaSink).onComplete {
      case Success(_) => println("Message successfully sent to Kafka")
      case Failure(exception) => println(s"Failed to send message to Kafka: ${exception.getMessage}")
    }
  }

  // #################################################################################### Consume


}
