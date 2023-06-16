package utils

import org.slf4j.Logger
import wvlet.log.LogSupport

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object TimeIt extends LogSupport {

  def measureExecutionTime[T](name: String, function: => T): T = {
    Try {
      val startTime = System.nanoTime()
      val result = function
      (startTime -> result)

    } match {
      case Success(value) =>
        val endTime = System.nanoTime()
        val executionTimeNs = endTime - value._1
        info(s"$name executed/instantiated in ${Duration.fromNanos(executionTimeNs).toMillis}ms")
        value._2
      case Failure(exception) => throw new RuntimeException(s"failed to execute $name")
    }
  }

}
