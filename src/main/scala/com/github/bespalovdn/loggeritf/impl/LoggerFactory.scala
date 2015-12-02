package com.github.bespalovdn.loggeritf.impl

import com.github.bespalovdn.loggeritf.Logger

trait LoggerFactory
{
    def newLogger(clazz: Class[_], mdc: Map[String, String] = null): Logger
}
