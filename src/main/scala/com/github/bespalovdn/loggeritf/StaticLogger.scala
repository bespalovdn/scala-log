package com.github.bespalovdn.loggeritf

import com.github.bespalovdn.loggeritf.impl.LoggerFactory

import scala.reflect.ClassTag

trait StaticLogger extends LoggerProvider
{
    val loggerClass: Class[_] = getClass
    val loggerMDC: Map[String, String] = Map.empty

    override lazy val logger: Logger = createLogger(loggerClass)

    override def loggerT[T : ClassTag]: Logger = createLogger(implicitly[ClassTag[T]].runtimeClass)

    private def createLogger(clazz: Class[_]): Logger = LoggerFactory().newLogger(clazz, loggerMDC)
}
