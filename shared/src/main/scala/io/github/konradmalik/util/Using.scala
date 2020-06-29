package io.github.konradmalik.util

import scala.util.control.NonFatal

/**
 * Implements "using" pattern
 */
object Using {

  /**
   * Uses a resource and makes sure it will be closed correctly after use
   *
   * @param r resource to use
   * @param f function that uses the resource
   * @tparam T Type of the resource
   * @tparam V Type of the value returned
   * @return Value after applying 'f' to 'r'
   */
  def withResources[T <: AutoCloseable, V](r: => T)(f: T => V): V = {
    val resource: T = r
    require(resource != null, "resource is null")
    var exception: Throwable = null
    try {
      f(resource)
    } catch {
      case NonFatal(e) =>
        exception = e
        throw e
    } finally {
      closeAndAddSuppressed(exception, resource)
    }
  }

  /**
   * Close the resource and add any suppressed exceptions
   *
   * @param e        Suppressed throwable exception
   * @param resource resource to close
   */
  private def closeAndAddSuppressed(e: Throwable,
                                    resource: AutoCloseable): Unit = {
    if (e != null) {
      try {
        resource.close()
      } catch {
        case NonFatal(suppressed) =>
          e.addSuppressed(suppressed)
      }
    } else {
      resource.close()
    }
  }


}
