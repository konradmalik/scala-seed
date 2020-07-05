package io.github.konradmalik.util

/**
  * Utility functions for scala maps
  */
object MapUtil {

  /**
    * Merges two maps by key
    *
    * @param m1 first map
    * @param m2 second map
    * @tparam K Keys type
    * @tparam V Values type
    * @return Merged map
    */
  def mergeToColletion[K, V](m1: Map[K, V], m2: Map[K, V]): Map[K, List[V]] =
    (m1.keySet ++ m2.keys) map { i =>
      i -> (m1.get(i).toList ::: m2.get(i).toList)
    } toMap

  /**
    * Merges two maps containing iterables by key
    *
    * @param m1 first map
    * @param m2 second map
    * @tparam K Keys type
    * @tparam V Iterable values type
    * @return Merged map
    */
  def mergeWithCollections[K, V](
      m1: Map[K, Iterable[V]],
      m2: Map[K, Iterable[V]]
  ): Map[K, Iterable[V]] =
    (m1.keySet ++ m2.keys) map { k =>
      k -> (m1.getOrElse(k, Iterable.empty) ++ m2.getOrElse(k, Iterable.empty))
    } toMap

}
