package com.github.bespalovdn.scalalog

import scala.reflect.ClassTag

trait LoggerProxy extends LoggerSupport
{
    def loggerSource: LoggerSupport

    override implicit def logger: Logger = loggerSource.logger
    override def loggerT[T: ClassTag]: Logger = loggerSource.loggerT
}
