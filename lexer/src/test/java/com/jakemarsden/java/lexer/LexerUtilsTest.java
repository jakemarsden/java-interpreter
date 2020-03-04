package com.jakemarsden.java.lexer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LexerUtilsTest {

  @Test
  void isDecimalNumberLiteral() {
    // decimal integer
    assertTrue(LexerUtils.isNumberLiteral("0"));
    assertTrue(LexerUtils.isNumberLiteral("1234"));
    assertTrue(LexerUtils.isNumberLiteral("1_234"));

    // bad decimal integer
    assertFalse(LexerUtils.isNumberLiteral("1234_"));
    assertFalse(LexerUtils.isNumberLiteral("1234a"));
    assertFalse(LexerUtils.isNumberLiteral("1234\uD83D\uDE00"));

    // decimal long
    assertTrue(LexerUtils.isNumberLiteral("1234l"));
    assertTrue(LexerUtils.isNumberLiteral("1234L"));

    // bad decimal long
    assertFalse(LexerUtils.isNumberLiteral("12e3L"));

    // decimal float
    assertTrue(LexerUtils.isNumberLiteral(".1f"));
    assertTrue(LexerUtils.isNumberLiteral("1.f"));
    assertTrue(LexerUtils.isNumberLiteral("1234.56f"));
    assertTrue(LexerUtils.isNumberLiteral("1234.56F"));

    // decimal double
    assertTrue(LexerUtils.isNumberLiteral(".1"));
    assertTrue(LexerUtils.isNumberLiteral("1."));
    assertTrue(LexerUtils.isNumberLiteral(".1d"));
    assertTrue(LexerUtils.isNumberLiteral("1.d"));
    assertTrue(LexerUtils.isNumberLiteral("1.D"));
    assertTrue(LexerUtils.isNumberLiteral("0.1"));
    assertTrue(LexerUtils.isNumberLiteral("1234.56"));
    assertTrue(LexerUtils.isNumberLiteral("1.234e3"));
    assertTrue(LexerUtils.isNumberLiteral("1.234e+3"));
    assertTrue(LexerUtils.isNumberLiteral("1.234e-3"));

    // bad decimal double
    assertFalse(LexerUtils.isNumberLiteral("12._34"));
    assertFalse(LexerUtils.isNumberLiteral("12_.34"));
    assertFalse(LexerUtils.isNumberLiteral("12.34e"));
    assertFalse(LexerUtils.isNumberLiteral("12.34e+"));
    assertFalse(LexerUtils.isNumberLiteral("12.34e++1"));
  }

  @Test
  void isBinaryNumberLiteral() {
    // binary integer
    assertTrue(LexerUtils.isNumberLiteral("0b1011"));
    assertTrue(LexerUtils.isNumberLiteral("0B1011"));
    assertTrue(LexerUtils.isNumberLiteral("0b10_11"));
    assertTrue(LexerUtils.isNumberLiteral("0b00001011"));

    // bad binary integer
    assertFalse(LexerUtils.isNumberLiteral("0b_1011"));
    assertFalse(LexerUtils.isNumberLiteral("0b1012"));
    assertFalse(LexerUtils.isNumberLiteral("0b1011\uD83D\uDE00"));
  }

  @Test
  void isOctalNumberLiteral() {
    // octal integer
    assertTrue(LexerUtils.isNumberLiteral("0231"));
    assertTrue(LexerUtils.isNumberLiteral("0_231"));
    assertTrue(LexerUtils.isNumberLiteral("02_31"));
    assertTrue(LexerUtils.isNumberLiteral("0000231"));

    // bad octal integer
    assertFalse(LexerUtils.isNumberLiteral("0238"));
    assertFalse(LexerUtils.isNumberLiteral("0338\uD83D\uDE00"));
  }

  @Test
  void isHexadecimalNumberLiteral() {
    // hexadecimal integer
    assertTrue(LexerUtils.isNumberLiteral("0x99"));
    assertTrue(LexerUtils.isNumberLiteral("0xabcdef"));
    assertTrue(LexerUtils.isNumberLiteral("0xABCDEF"));
    assertTrue(LexerUtils.isNumberLiteral("0xabc_def"));

    // bad hexadecimal integer
    assertFalse(LexerUtils.isNumberLiteral("0x_99"));
    assertFalse(LexerUtils.isNumberLiteral("0x9g"));
    assertFalse(LexerUtils.isNumberLiteral("0x99\uD83D\uDE00"));
  }

  @Test
  void isDigit() {
    assertIsDigitWorksForRange('0', '1', LexerUtils.BIN);
    assertIsDigitWorksForRange('0', '7', LexerUtils.OCT);
    assertIsDigitWorksForRange('0', '9', LexerUtils.DEC);
    assertIsDigitWorksForRange('0', '9', LexerUtils.HEX);
    assertIsDigitWorksForRange('a', 'f', LexerUtils.HEX);
    assertIsDigitWorksForRange('A', 'F', LexerUtils.HEX);
    assertIsDigitWorksForRange('0', '9', 36);
    assertIsDigitWorksForRange('a', 'z', 36);
    assertIsDigitWorksForRange('A', 'Z', 36);
  }

  @Test
  void isDigitThrowsForBadRadix() {
    assertThrows(IllegalArgumentException.class, () -> LexerUtils.isDigit('0', -1));
    assertThrows(IllegalArgumentException.class, () -> LexerUtils.isDigit('0', 1));
    assertThrows(IllegalArgumentException.class, () -> LexerUtils.isDigit('0', 37));
  }

  private static void assertIsDigitWorksForRange(int minCodePoint, int maxCodePoint, int radix) {
    assertTrue(LexerUtils.isDigit(minCodePoint, radix));
    assertTrue(LexerUtils.isDigit(maxCodePoint, radix));
    assertFalse(LexerUtils.isDigit(minCodePoint - 1, radix));
    assertFalse(LexerUtils.isDigit(maxCodePoint + 1, radix));
  }
}
