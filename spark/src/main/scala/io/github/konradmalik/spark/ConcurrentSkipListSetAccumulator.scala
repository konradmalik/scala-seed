package io.github.konradmalik.spark

import java.util.concurrent.ConcurrentSkipListSet

import org.apache.spark.util.AccumulatorV2

/**
  * [[ConcurrentSkipListSet]] implemented as a Spark Accumulator
  *
  * @param value Object to be used in this accumulator
  * @tparam T Type of the accumulated values
  */
class ConcurrentSkipListSetAccumulator[T](
    override val value: ConcurrentSkipListSet[T]
) extends AccumulatorV2[T, ConcurrentSkipListSet[T]] {

  /**
    * Constructor
    */
  def this() = this(new ConcurrentSkipListSet[T]())

  override def isZero: Boolean = value.isEmpty

  override def copy() = new ConcurrentSkipListSetAccumulator(value)

  override def reset(): Unit = value.clear()

  override def add(v: T): Unit = value.add(v)

  override def merge(
      other: AccumulatorV2[T, ConcurrentSkipListSet[T]]
  ): Unit = {
    val iterator = other.value.iterator()
    while (iterator.hasNext) {
      value.add(iterator.next())
    }
  }
}
