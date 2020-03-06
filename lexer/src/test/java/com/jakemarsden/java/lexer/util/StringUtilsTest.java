package com.jakemarsden.java.lexer.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StringUtilsTest {

  @Test
  void strContentEquals() {
    assertTrue(StringUtils.strContentEquals("abc", "abc"));
    assertFalse(StringUtils.strContentEquals("abc", "acb"));
    assertFalse(StringUtils.strContentEquals("abc", "abcd"));

    assertTrue(StringUtils.strContentEquals(new StringBuilder("abc"), "abc"));
    assertTrue(StringUtils.strContentEquals("abc", new StringBuilder("abc")));
    assertTrue(StringUtils.strContentEquals(new StringBuilder("abc"), new StringBuilder("abc")));

    assertTrue(StringUtils.strContentEquals("\uD83D\uDE00", "\uD83D\uDE00"));
  }

  @Test
  void codePointCount() {
    assertEquals(0, StringUtils.codePointCount(""));
    assertEquals(0, StringUtils.codePointCount(new StringBuilder(0)));

    assertEquals(13, StringUtils.codePointCount("Hello, world!"));
    assertEquals(13, StringUtils.codePointCount(new StringBuilder("Hello, world!")));

    // 😀 has 2 chars but 1 code point
    assertEquals(1, StringUtils.codePointCount("\uD83D\uDE00"));
    assertEquals(1, StringUtils.codePointCount(new StringBuilder("\uD83D\uDE00")));
  }
}
