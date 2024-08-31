package it.unibo.controller.subscribers

import it.unibo.controller.logger

object SubscriberUtils:
  def onErrorHandler(ex: Throwable): Unit =
    logger.error(s"Received error: ${ex.getMessage}")
    ex.getStackTrace.foreach { traceElement =>
      logger.error(s"at ${traceElement.getClassName}.${traceElement.getMethodName}(${traceElement
          .getFileName}:${traceElement.getLineNumber})")
    }
    logger.error(s"Full description: ${ex.toString}")

  def onCompleteHandler(): Unit = logger.info(s"Received final event")
