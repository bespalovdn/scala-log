package com.github.bespalovdn.loggeritf

import com.github.bespalovdn.loggeritf.impl.LoggerFactory

import scala.reflect.ClassTag

trait LoggerSupport extends LoggerProvider
{
    override def logger: Logger = createLogger(loggerClass)

    override def loggerT[A](implicit classTag: ClassTag[A]): Logger = createLogger(classTag.runtimeClass)

    def loggerClass: Class[_] = getClass

    def loggerMdc: Map[String, String] = Map.empty

    private def createLogger(clazz: Class[_]): Logger = LoggerFactory().newLogger(clazz, loggerMdc)
}
