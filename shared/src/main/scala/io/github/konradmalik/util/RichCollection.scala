package io.github.konradmalik.util

import scala.collection.IterableLike
import scala.collection.generic.CanBuildFrom

/**
 * Object containing methods to enrich standard Scala collections
 */
object RichCollection {

  /**
   * Enriched collection
   * @param xs Collection to enrich
   * @tparam A Type of the collection elements
   * @tparam Repr Type of the collection
   */
  class RichCollection[A, Repr](xs: IterableLike[A, Repr]) {
    /**
     * Makes a collection distinct by values defined using a custom function
     * @param f function returning values to make this collection distinct by
     * @param cbf implicit builder of this collection
     * @tparam B Type of the distinct value
     * @tparam That New collection type
     * @return New collection with custom-defined distinct elements
     */
    def distinctBy[B, That](f: A => B)(implicit cbf: CanBuildFrom[Repr, A, That]): That = {
      val builder = cbf(xs.repr)
      val i = xs.iterator
      var set = Set[B]()
      while (i.hasNext) {
        val o = i.next
        val b = f(o)
        if (!set(b)) {
          set += b
          builder += o
        }
      }
      builder.result
    }
  }

  /**
   * Implicitly converts every collection to the enriched one
   * @param xs Collection to enrich
   * @tparam A Type of the collection elements
   * @tparam Repr Type of the collection
   * @return Enriched collection
   */
  implicit def toRich[A, Repr](xs: IterableLike[A, Repr]): RichCollection[A, Repr] = new RichCollection(xs)
}
