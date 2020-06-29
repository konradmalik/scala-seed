package io.github.konradmalik.util

import java.time.Instant
import java.time.format.DateTimeFormatter

import javax.xml.bind.DatatypeConverter

import scala.util.Try

/**
 * Utility object for dates handling methods
 */
object DateUtil {
  /**
   * Formatter that represents ISO 8601 date
   */
  private lazy val isoFormatter = DateTimeFormatter.ISO_DATE_TIME

  /**
   * Tries to parse string into [[Instant]] using a couple of different formats
   *
   * @param s String to parse as a date
   * @return Parsed date
   */
  def parseDateTime(s: String): Instant = {
    // try Instant first as it keeps precision!
    Try {
      Instant.parse(s)
    }.orElse {
      Try(Instant.parse(s + "Z"))
    }.orElse {
      Try(Instant.from(isoFormatter.parse(s)))
    }.orElse {
      Try(DatatypeConverter.parseDateTime(s).toInstant)
    }.get
  }
}
