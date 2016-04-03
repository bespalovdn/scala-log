package com.github.bespalovdn.scalalog.impl.factory

import com.github.bespalovdn.scalalog.Logger
import com.github.bespalovdn.scalalog.impl.LoggerFactory

import scala.collection.mutable.ArrayBuffer

/**
  * BufferedLoggerFactory intended to write logs into buffer until logging subsystem is initialized.
  * When logging subsystem is initialized, method `update` should be called with corresponding `LoggerFactory`.
  */
private [impl]
object BufferedLoggerFactory extends LoggerFactory
{
    override def newLogger(clazz: Class[_], mdc: Map[String, String] = null): Logger = synchronized{
        factory.map(_.newLogger(clazz, mdc)).getOrElse(new BufferedLogger(clazz, mdc, append))
    }

    override def update(newFactory: LoggerFactory): Unit = synchronized {
        factory = Some(newFactory)
        // write all the logs using new logger factory:
        buffer foreach {writer =>
            val logger = newFactory.newLogger(writer.clazz, writer.mdc)
            writer write logger
        }
        buffer.clear()
    }

    private def append(writer: BufferedLogWriter): Unit = synchronized{
        factory match {
            case Some(f) =>
                val logger = f.newLogger(writer.clazz, writer.mdc)
                writer write logger
            case None =>
                writer.evaluate()
                buffer += writer
        }
    }

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
    def trace(m: => String): Unit = {
        var msg: Option[String] = None
        def eval(): Unit = msg = Some(evaluate(m))
        write(eval){l => if(l.isTraceEnabled) l.trace(msg getOrElse m)}
    }
    def trace(m: => String, t: => Throwable): Unit = {
        var msg: Option[String] = None
        var thr: Option[Throwable] = None
        def eval(): Unit = {msg = Some(evaluate(m)); thr = Some(evaluate(t))}
        write(eval){l => if(l.isTraceEnabled) l.trace(msg getOrElse m, thr getOrElse t)}
    }

    def isDebugEnabled = true
    def debug(m: => String): Unit = {
        var msg: Option[String] = None
        def eval(): Unit = msg = Some(evaluate(m))
        write(eval){l => if(l.isDebugEnabled) l.debug(msg getOrElse m)}
    }
    def debug(m: => String, t: => Throwable): Unit = {
        var msg: Option[String] = None
        var thr: Option[Throwable] = None
        def eval(): Unit = {msg = Some(evaluate(m)); thr = Some(evaluate(t))}
        write(eval){l => if(l.isDebugEnabled) l.debug(msg getOrElse m, thr getOrElse t)}
    }

    def isInfoEnabled = true
    def info(m: => String): Unit = {
        var msg: Option[String] = None
        def eval(): Unit = msg = Some(evaluate(m))
        write(eval){l => if(l.isInfoEnabled) l.info(msg getOrElse m)}
    }
    def info(m: => String, t: => Throwable): Unit = {
        var msg: Option[String] = None
        var thr: Option[Throwable] = None
        def eval(): Unit = {msg = Some(evaluate(m)); thr = Some(evaluate(t))}
        write(eval){l => if(l.isInfoEnabled) l.info(msg getOrElse m, thr getOrElse t)}
    }

    def isWarnEnabled = true
    def warn(m: => String): Unit = {
        var msg: Option[String] = None
        def eval(): Unit = msg = Some(evaluate(m))
        write(eval){l => if(l.isWarnEnabled) l.warn(msg getOrElse m)}
    }
    def warn(m: => String, t: => Throwable): Unit = {
        var msg: Option[String] = None
        var thr: Option[Throwable] = None
        def eval(): Unit = {msg = Some(evaluate(m)); thr = Some(evaluate(t))}
        write(eval){l => if(l.isWarnEnabled) l.warn(msg getOrElse m, thr getOrElse t)}
    }

    def isErrorEnabled = true
    def error(m: => String): Unit = {
        var msg: Option[String] = None
        def eval(): Unit = msg = Some(evaluate(m))
        write(eval){l => if(l.isErrorEnabled) l.error(msg getOrElse m)}
    }
    def error(m: => String, t: => Throwable): Unit = {
        var msg: Option[String] = None
        var thr: Option[Throwable] = None
        def eval(): Unit = {msg = Some(evaluate(m)); thr = Some(evaluate(t))}
        write(eval){l => if(l.isErrorEnabled) l.error(msg getOrElse m, thr getOrElse t)}
    }

    private def evaluate[A](fa: => A): A = {val a: A = fa; a}

    private def write(eval: () => Unit)(fn: Logger => Unit): Unit = append(new BufferedLogWriter {
        override def mdc: Map[String, String] = self.mdc
        override def clazz: Class[_] = self.clazz
        override def evaluate(): Unit = eval()
        override def write(l: Logger): Unit = fn(l)
    })
}

private trait BufferedLogWriter
{
    def clazz: Class[_]
    def mdc: Map[String, String]
    def evaluate(): Unit
    def write(l: Logger): Unit
}
