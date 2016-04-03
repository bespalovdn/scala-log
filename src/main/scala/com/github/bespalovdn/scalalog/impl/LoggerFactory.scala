package com.github.bespalovdn.scalalog.impl

import java.util.concurrent.atomic.AtomicReference

import com.github.bespalovdn.scalalog.Logger
import com.github.bespalovdn.scalalog.impl.factory.BufferedLoggerFactory

trait LoggerFactory
{
    def newLogger(clazz: Class[_], mdc: Map[String, String] = null): Logger
    def update(newFactory: LoggerFactory): Unit = {}
}

object LoggerFactory
{
    def apply(): LoggerFactory = factory.get()
    def initialize(f: LoggerFactory): Unit = factory.set(f)

    private lazy val factory: AtomicReference[LoggerFactory] = new AtomicReference(BufferedLoggerFactory)
}
