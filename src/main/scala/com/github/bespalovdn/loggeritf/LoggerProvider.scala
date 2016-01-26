package com.github.bespalovdn.loggeritf

import scala.reflect.ClassTag

trait LoggerProvider
{
    def logger: Logger
    def loggerT[T : ClassTag]: Logger
}
