package com.github.bespalovdn.scalalog

import scala.reflect.ClassTag

trait LoggerSupport
{
    implicit def logger: Logger
    def loggerT[T : ClassTag]: Logger
}
