package com.github.bespalovdn.scalalog

import scala.reflect.ClassTag

abstract class LoggerProxy(source: LoggerSupport) extends LoggerSupport
{
    override implicit def logger: Logger = source.logger
    override def loggerT[T: ClassTag]: Logger = source.loggerT
}
