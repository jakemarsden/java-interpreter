package com.jakemarsden.java.lexer;

import static java.lang.Math.min;

/** Utility methods for lexing the Java language. */
public final class LexerUtils {

  public static final int BIN = 2;
  public static final int OCT = 8;
  public static final int DEC = 10;
  public static final int HEX = 16;

  /**
   * Returns {@code true} if the {@code token} is a valid Java number literal.
   *
   * @param token the value to examine
   * @return {@code true} if the {@code token} is a valid Java number literal
   */
  public static boolean isNumberLiteral(String token) {
    boolean isFloatingPoint = false;
    boolean isScientific = false;
    var radix = DEC;

    var pointItr = token.codePoints().iterator();
    int currPoint = -1;
    int prevPoint;
    int idx = -1;
    while (pointItr.hasNext()) {
      prevPoint = currPoint;
      currPoint = pointItr.nextInt();
      idx++;

      if (idx == 1 && prevPoint == '0') {
        if (currPoint == 'b' || currPoint == 'B') radix = BIN;
        if (currPoint == 'x' || currPoint == 'X') radix = HEX;
        if (currPoint == '_' || isDigit(currPoint, OCT)) radix = OCT;
        if (radix != DEC) continue;
      }

      if (isDigit(currPoint, radix)) continue;

      switch (currPoint) {
        case '_':
          if (isDigit(prevPoint, radix) && pointItr.hasNext()) continue;
          break;
        case '.':
          if (prevPoint != '_' && radix == DEC && !isScientific && !isFloatingPoint) {
            isFloatingPoint = true;
            continue;
          }
          break;
        case 'e':
          if (radix == DEC && !isScientific && pointItr.hasNext()) {
            isScientific = true;
            continue;
          }
          break;
        case '+':
        case '-':
          if (prevPoint == 'e' && isScientific && pointItr.hasNext()) continue;
          break;
        case 'f':
        case 'F':
        case 'd':
        case 'D':
          if (!pointItr.hasNext()) continue;
          break;
        case 'l':
        case 'L':
          if (!isScientific && !isFloatingPoint && !pointItr.hasNext()) continue;
          break;
      }

      return false;
    }
    return true;
  }

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

  LexerUtils() {
    throw new UnsupportedOperationException();
  }
}
