package com.jakemarsden.java.lexer.util;

import static java.lang.Math.min;
import static java.lang.String.format;

public final class NumberUtils {

  public static final int BIN = 2;
  public static final int OCT = 8;
  public static final int DEC = 10;
  public static final int HEX = 16;

  /**
   * Returns {@code true} if {@code ch} is considered a digit for the {@code radix}.
   *
   * @param ch the code point to examine
   * @param radix how many digits are in the number system, in the range {@code [2, 36]}
   * @return {@code true} if {@code ch} is considered a digit for the {@code radix}
   * @throws IllegalArgumentException if the {@code radix} is out of the range {@code [2, 36]}
   */
  public static boolean isDigit(int ch, int radix) {
    if (radix < 2 || radix > 36) throw new IllegalArgumentException("Unsupported radix: " + radix);
    return (ch >= '0' && ch < '0' + min(radix, 10))
        || (ch >= 'a' && ch < 'a' + radix - 10)
        || (ch >= 'A' && ch < 'A' + radix - 10);
  }

  /**
   * Returns the numerical value of a code point.
   *
   * @param ch the code point to convert
   * @param radix how many digits are in the number system, in the range {@code [2, 36]}
   * @return the {@code ch} converted to a digit
   * @throws IllegalArgumentException if the {@code ch} doesn't represent a digit for {@code radix}
   * @throws IllegalArgumentException if the {@code radix} is out of the range {@code [2, 36]}
   */
  public static int toDigit(int ch, int radix) {
    if (radix < 2 || radix > 36) throw new IllegalArgumentException("Unsupported radix: " + radix);

    if (ch >= '0' && ch <= '9' && ch - '0' < radix) return ch - '0';
    if (ch >= 'a' && ch <= 'z' && ch - 'a' + 10 < radix) return ch - 'a' + 10;
    if (ch >= 'A' && ch <= 'Z' && ch - 'A' + 10 < radix) return ch - 'A' + 10;
    throw new IllegalArgumentException(
        format("Not a digit for radix %d: '%c' U+%04X", radix, ch, ch));
  }

  private NumberUtils() {
    throw new UnsupportedOperationException();
  }
}
