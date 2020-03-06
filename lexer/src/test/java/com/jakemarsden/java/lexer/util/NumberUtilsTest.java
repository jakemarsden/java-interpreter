package com.jakemarsden.java.lexer.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class NumberUtilsTest {

  @Test
  void isDigit() {
    assertIsDigitWorksForRange('0', '1', NumberUtils.BIN);
    assertIsDigitWorksForRange('0', '7', NumberUtils.OCT);
    assertIsDigitWorksForRange('0', '9', NumberUtils.DEC);
    assertIsDigitWorksForRange('0', '9', NumberUtils.HEX);
    assertIsDigitWorksForRange('a', 'f', NumberUtils.HEX);
    assertIsDigitWorksForRange('A', 'F', NumberUtils.HEX);
    assertIsDigitWorksForRange('0', '9', 36);
    assertIsDigitWorksForRange('a', 'z', 36);
    assertIsDigitWorksForRange('A', 'Z', 36);
  }

  @Test
  void isDigitThrowsForBadRadix() {
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.isDigit('0', -1));
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.isDigit('0', 1));
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.isDigit('0', 37));
  }

  private static void assertIsDigitWorksForRange(int minCodePoint, int maxCodePoint, int radix) {
    assertTrue(NumberUtils.isDigit(minCodePoint, radix));
    assertTrue(NumberUtils.isDigit(maxCodePoint, radix));
    assertFalse(NumberUtils.isDigit(minCodePoint - 1, radix));
    assertFalse(NumberUtils.isDigit(maxCodePoint + 1, radix));
  }

  @Test
  void toDigit() {
    assertEquals(0, NumberUtils.toDigit('0', NumberUtils.BIN));
    assertEquals(1, NumberUtils.toDigit('1', NumberUtils.BIN));
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.toDigit('2', NumberUtils.BIN));
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.toDigit('a', NumberUtils.BIN));
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.toDigit('A', NumberUtils.BIN));

    assertEquals(0, NumberUtils.toDigit('0', NumberUtils.DEC));
    assertEquals(9, NumberUtils.toDigit('9', NumberUtils.DEC));
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.toDigit('a', NumberUtils.DEC));
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.toDigit('A', NumberUtils.DEC));

    assertEquals(0x0, NumberUtils.toDigit('0', NumberUtils.HEX));
    assertEquals(0x9, NumberUtils.toDigit('9', NumberUtils.HEX));
    assertEquals(0xa, NumberUtils.toDigit('a', NumberUtils.HEX));
    assertEquals(0xa, NumberUtils.toDigit('A', NumberUtils.HEX));
    assertEquals(0xf, NumberUtils.toDigit('f', NumberUtils.HEX));
    assertEquals(0xf, NumberUtils.toDigit('F', NumberUtils.HEX));
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.toDigit('g', NumberUtils.HEX));
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.toDigit('G', NumberUtils.HEX));

    assertEquals(0, NumberUtils.toDigit('0', 36));
    assertEquals(9, NumberUtils.toDigit('9', 36));
    assertEquals(10, NumberUtils.toDigit('a', 36));
    assertEquals(10, NumberUtils.toDigit('A', 36));
    assertEquals(35, NumberUtils.toDigit('z', 36));
    assertEquals(35, NumberUtils.toDigit('Z', 36));
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.toDigit('z' + 1, 36));
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.toDigit('Z' + 1, 36));
  }

  @Test
  void toDigitThrowsForBadRadix() {
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.toDigit('0', -1));
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.toDigit('0', 1));
    assertThrows(IllegalArgumentException.class, () -> NumberUtils.toDigit('0', 37));
  }
}
