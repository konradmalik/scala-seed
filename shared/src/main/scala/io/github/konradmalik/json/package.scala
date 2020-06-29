package io.github.konradmalik

import java.sql.Timestamp
import java.time.Instant

import com.fasterxml.jackson.databind.JsonNode

/**
 * Package to handle Json serialization and deserialization.
 * Here package-shared helper methods are placed.
 * Current implementation uses Jackson.
 */
package object konradmalik {
  def longNodeToTimestamp(node: JsonNode): Timestamp = {
    if (node.canConvertToLong)
      new Timestamp(node.longValue())
    else if (node.canConvertToInt)
      new Timestamp(node.intValue().toLong)
    else
      Timestamp.from(Instant.now())
  }

  def doubleNodeToDouble(node: JsonNode): Double = {
    if (node.isDouble)
      node.doubleValue()
    else
      Double.NaN
  }
}
