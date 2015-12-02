package com.github.bespalovdn.loggeritf

trait Logger
{
    def isTraceEnabled: Boolean
    def trace(message: => String): Unit
    def trace(message: => String, t: => Throwable): Unit

    def isDebugEnabled: Boolean
    def debug(message: => String): Unit
    def debug(message: => String, t: => Throwable): Unit

    def isInfoEnabled: Boolean
    def info(message: => String): Unit
    def info(message: => String, t: => Throwable): Unit

    def isWarnEnabled: Boolean
    def warn(message: => String): Unit
    def warn(message: => String, t: => Throwable): Unit

    def isErrorEnabled: Boolean
    def error(message: => String): Unit
    def error(message: => String, t: => Throwable): Unit
}
