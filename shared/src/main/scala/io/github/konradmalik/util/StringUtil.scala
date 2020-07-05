package io.github.konradmalik.util

import java.util.regex.Pattern

/**
  * Various utilities for working with strings
  */
object StringUtil {

  /**
    * Checks if string contains only alphanumeric characters
    *
    * @param s String to check
    * @return Is alphanumeric only?
    */
  final def isAlphanumeric(s: String): Boolean = {
    val p = Pattern.compile("[^a-zA-Z0-9]")
    val hasSpecialChar = p.matcher(s).find()
    !hasSpecialChar
  }

}
