package com.jakemarsden.java.lexer.util;

import static java.lang.Math.toIntExact;

public final class StringUtils {

  /**
   * Compares the characters of two character sequences.
   *
   * @param a the sequence to compare to {@code b}
   * @param b the sequence to compare to {@code a}
   * @return {@code true} if the contents of {@code a} and {@code b} are equal
   * @see String#contentEquals(CharSequence)
   */
  public static boolean strContentEquals(CharSequence a, CharSequence b) {
    if (a.length() != b.length()) return false;
    for (int charIdx = 0; charIdx < a.length(); charIdx++) {
      if (a.charAt(charIdx) != b.charAt(charIdx)) return false;
    }
    return true;
  }

  /**
   * Returns the number of code points in a characters sequence. This is distinct from its {@link
   * CharSequence#length() length} because some code points are made up of two adjacent characters.
   *
   * @param str the character sequence to examine
   * @return the number of code points in {@code str}
   * @see String#codePointCount(int, int)
   */
  public static int codePointCount(CharSequence str) {
    if (str instanceof String) return ((String) str).codePointCount(0, str.length());
    if (str instanceof StringBuilder) return ((StringBuilder) str).codePointCount(0, str.length());
    if (str instanceof StringBuffer) return ((StringBuffer) str).codePointCount(0, str.length());
    return toIntExact(str.codePoints().count());
  }

  private StringUtils() {
    throw new UnsupportedOperationException();
  }
}
