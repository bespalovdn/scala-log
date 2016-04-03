package com.github.bespalovdn.scalalog

import com.github.bespalovdn.scalalog.impl.LoggerFactory

import scala.reflect.ClassTag

trait DynamicLogger extends LoggerSupport
{
    override def logger: Logger = createLogger(loggerClass)

    override def loggerT[T : ClassTag]: Logger = createLogger(implicitly[ClassTag[T]].runtimeClass)

    def loggerClass: Class[_] = getClass

    def loggerMDC: Map[String, String] = Map.empty

    private def createLogger(clazz: Class[_]): Logger = LoggerFactory().newLogger(clazz, loggerMDC)
}
