import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy

import static ch.qos.logback.classic.Level.*

appender("FILE", RollingFileAppender) {
  file = "/var/log/discodj/discodj-service.log"

  rollingPolicy(FixedWindowRollingPolicy) {
    fileNamePattern = "discodj-service.%i.log"
    minIndex = 1
    maxIndex = 3
  }

  triggeringPolicy(SizeBasedTriggeringPolicy) {
    maxFileSize = "1MB"
  }

  encoder(PatternLayoutEncoder) {
    pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  }
}

appender("ASYNC", AsyncAppender) {
  appenderRef("FILE")
}

logger("com.totalchange", INFO)
root(WARN, ["ASYNC"])
