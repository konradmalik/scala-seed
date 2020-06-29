package io.github.konradmalik.json

import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

/**
 * Provides JSON serialization and deserialization methods to implementing classes
 */
trait JsonSupport {
  /**
   * Jackson mapper instance with preloaded modules
   */
  protected final lazy val mapper = JsonSupport.mapper

  /**
   * Converts value to stringified JSON
   *
   * @param value value to convert
   * @return Stringified JSON representation
   */
  protected def toJson(value: Map[Symbol, Any]): String = {
    toJson(value map { case (k, v) => k.name -> v })
  }

  /**
   * Converts value to stringified JSON
   *
   * @param value value to convert
   * @return Stringified JSON representation
   */
  protected def toJson(value: Any): String = {
    mapper.writeValueAsString(value)
  }

  /**
   * Converts value to stringified JSON in bytes
   *
   * @param value value to convert
   * @return Stringified JSON representation in bytes
   */
  protected def toJsonBytes(value: Any): Array[Byte] = {
    mapper.writeValueAsBytes(value)
  }

  /**
   * Converts stringified JSON to a map representation
   *
   * @param json stringified JSON to convert
   * @param m    class manifest of the values class
   * @tparam V Type of the map values
   * @return Map instance
   */
  protected def toMap[V](json: String)(implicit m: Manifest[V]): Map[String, V] = fromJson[Map[String, V]](json)

  /**
   * Converts stringified JSON to the provided class instance
   *
   * @param json stringified JSON to convert
   * @param m    class manifest of the target class
   * @tparam T type to convert to
   * @return Converted instance
   */
  protected def fromJson[T](json: String)(implicit m: Manifest[T]): T = {
    mapper.readValue[T](json)
  }

  /**
   * Converts bytes of the stringified JSON to the provided class instance
   *
   * @param json bytes of JSON to convert
   * @param m    class manifest of the target class
   * @tparam T type to convert to
   * @return Converted instance
   */
  protected def fromJsonBytes[T](json: Array[Byte])(implicit m: Manifest[T]): T = {
    mapper.readValue[T](json)
  }
}

/**
 * Internal object responsible for initializing of all custom mappers
 */
private object JsonSupport {
  /**
   * Jackson mapper instance
   */
  final val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  // my serializers and deserializers
  mapper.registerModule(new JavaTimeModule())
  mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
}
