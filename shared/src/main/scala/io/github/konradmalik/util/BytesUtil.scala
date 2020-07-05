package io.github.konradmalik.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.ByteBuffer
import java.util.Base64

/**
  * Utility class for serialization/deserialization of objects to/from various `byte` based classes
  */
object BytesUtil {

  /**
    * Converts an object to [[ByteBuffer]]
    *
    * @param obj object to convert
    * @return Byte buffer instance
    */
  def toByteBuffer(obj: Any): ByteBuffer = ByteBuffer.wrap(toByteArray(obj))

  /**
    * Converts an object to [[Array[Byte]]
    *
    * @param obj object to convert
    * @return Array of bytes
    */
  def toByteArray(obj: Any): Array[Byte] = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(obj)
    oos.close()
    stream.toByteArray
  }

  /**
    * Converts [[ByteBuffer]] back to a object (manual cast needed)
    *
    * @param byteBuffer byte byffer to convert back
    * @return Object instance
    */
  def toObject(byteBuffer: ByteBuffer): Any = toObject(byteBuffer.array)

  /**
    * Converts array of bytes back to a object (manual cast needed)
    *
    * @param bytes bytes to convert back
    * @return Object instance
    */
  def toObject(bytes: Array[Byte]): Any = {
    val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
    val value = ois.readObject
    ois.close()
    value
  }

  /**
    * Converts bytes to base64 representation
    *
    * @param bytes bytes to convert
    * @return base64 string
    */
  def toBase64(bytes: Array[Byte]) = new String(Base64.getEncoder.encode(bytes))

  /**
    * Creates bytes from base64 string
    *
    * @param base64 string representation of base64
    * @return bytes
    */
  def fromBase64(base64: String): Array[Byte] = Base64.getDecoder.decode(base64)

}
