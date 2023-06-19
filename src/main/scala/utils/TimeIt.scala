package utils

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
        info(s"$name executed in ${Duration.fromNanos(executionTimeNs).toMillis}ms")
        value._2
      case Failure(exception) =>
        error(s"failed to execute $name msg:${exception.getMessage}")
        throw exception
    }
  }

}
