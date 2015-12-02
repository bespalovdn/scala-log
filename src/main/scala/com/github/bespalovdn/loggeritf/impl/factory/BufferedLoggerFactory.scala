package com.github.bespalovdn.loggeritf.impl.factory

import com.github.bespalovdn.loggeritf.Logger
import com.github.bespalovdn.loggeritf.impl.LoggerFactory

import scala.collection.mutable.ArrayBuffer

private [impl]
object BufferedLoggerFactory extends LoggerFactory
{
    override def newLogger(clazz: Class[_], mdc: Map[String, String]): Logger = synchronized{
        factory.map(_.newLogger(clazz, mdc)).getOrElse(new BufferedLogger(clazz, mdc, append))
    }

    override def update(newFactory: LoggerFactory): Unit = synchronized {
        factory = Some(newFactory)
        // write all the logs using new logger factory:
        buffer foreach {writer =>
            val logger = newFactory.newLogger(writer.clazz, writer.mdc)
            writer.write(logger)
        }
        buffer.clear()
    }

    private def append(writer: BufferedLogWriter): Unit = synchronized{ buffer += writer }

    private var factory: Option[LoggerFactory] = None
    private var buffer = ArrayBuffer.empty[BufferedLogWriter]
}

private
class BufferedLogger(clazz: Class[_],
                     mdc: Map[String, String],
                     append: BufferedLogWriter => Unit) extends Logger
{
    self =>

    def isTraceEnabled = true
    def trace(message: => String): Unit = write(_.trace(message))
    def trace(message: => String, t: => Throwable): Unit = write(_.trace(message, t))

    def isDebugEnabled = true
    def debug(message: => String): Unit = write(_.debug(message))
    def debug(message: => String, t: => Throwable): Unit = write(_.debug(message, t))

    def isInfoEnabled = true
    def info(message: => String): Unit = write(_.info(message))
    def info(message: => String, t: => Throwable): Unit = write(_.info(message, t))

    def isWarnEnabled = true
    def warn(message: => String): Unit = write(_.warn(message))
    def warn(message: => String, t: => Throwable): Unit = write(_.warn(message, t))

    def isErrorEnabled = true
    def error(message: => String): Unit = write(_.error(message))
    def error(message: => String, t: => Throwable): Unit = write(_.error(message, t))

    private def write(fn: Logger => Unit): Unit = append(new BufferedLogWriter {
        override def mdc: Map[String, String] = self.mdc
        override def clazz: Class[_] = self.clazz
        override def write(l: Logger): Unit = fn(l)
    })
}

private trait BufferedLogWriter
{
    def clazz: Class[_]
    def mdc: Map[String, String]
    def write(l: Logger): Unit
}
