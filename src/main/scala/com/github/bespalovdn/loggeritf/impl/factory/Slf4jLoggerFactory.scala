package com.github.bespalovdn.loggeritf.impl.factory

import java.util.{Map => JMap}

import com.github.bespalovdn.loggeritf.Logger
import com.github.bespalovdn.loggeritf.impl.LoggerFactory
import org.slf4j.{LoggerFactory => NativeSlf4jLoggerFactory, MDC}

import scala.collection.JavaConverters._

object Slf4jLoggerFactory extends LoggerFactory
{
    private val isMdcSet = new ThreadLocal[Boolean]{
        override def initialValue(): Boolean = false
    }

    override def newLogger(clazz: Class[_], mdc: Map[String, String] = null): Logger = new Impl(clazz, mdc.asJava)

    private
    class Impl(clazz: Class[_], mdc: JMap[String, String]) extends Logger
    {
        private lazy val logger = NativeSlf4jLoggerFactory.getLogger(clazz)

        override def isTraceEnabled: Boolean = logger.isTraceEnabled
        override def trace(message: => String): Unit = if(isTraceEnabled) withMDC{ logger.trace(message) }
        override def trace(message: => String, t: => Throwable): Unit = if(isTraceEnabled) withMDC{ logger.trace(message, t) }

        override def isDebugEnabled: Boolean = logger.isDebugEnabled
        override def debug(message: => String): Unit = if(isDebugEnabled) withMDC{ logger.debug(message) }
        override def debug(message: => String, t: => Throwable): Unit = if(isDebugEnabled) withMDC{ logger.debug(message, t) }

        override def isInfoEnabled: Boolean = logger.isInfoEnabled
        override def info(message: => String): Unit = if(isInfoEnabled) withMDC{ logger.info(message) }
        override def info(message: => String, t: => Throwable): Unit = if(isInfoEnabled) withMDC{ logger.info(message, t) }

        override def isWarnEnabled: Boolean = logger.isWarnEnabled
        override def warn(message: => String): Unit = if(isWarnEnabled) withMDC{ logger.warn(message) }
        override def warn(message: => String, t: => Throwable): Unit = if(isWarnEnabled) withMDC{ logger.warn(message, t) }

        override def isErrorEnabled: Boolean = logger.isErrorEnabled
        override def error(message: => String): Unit = if(isErrorEnabled) withMDC{ logger.error(message) }
        override def error(message: => String, t: => Throwable): Unit = if(isErrorEnabled) withMDC{ logger.error(message, t) }

        private def withMDC(logFn: => Unit): Unit ={
            // Avoid subsequent MDC initialization/clearing:
            if(isMdcSet.get()){
                logFn
            }else{
                try {
                    isMdcSet.set(true)
                    MDC.setContextMap(mdc)
                    logFn
                }finally{
                    MDC.clear()
                    isMdcSet.set(false)
                }
            }
        }
    }
}
