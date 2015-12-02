package com.github.bespalovdn.loggeritf.impl.factory

import com.github.bespalovdn.loggeritf.Logger
import com.github.bespalovdn.loggeritf.impl.LoggerFactory

private [impl]
object BufferLoggerFactory extends LoggerFactory
{
    override def newLogger(clazz: Class[_], mdc: Map[String, String]): Logger = ???
}
