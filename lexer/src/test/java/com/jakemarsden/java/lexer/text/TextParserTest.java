package com.jakemarsden.java.lexer.text;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TextParserTest {

  @Test
  void consumeExact() {
    var p = initObjUnderTest("abcd");
    assertDoesNotThrow(() -> p.consumeExact('a'));
    assertEquals(TextPosition.of(0, 1, 1), p.getPosition(), "Position did advance");
  }

  @Test
  void consumeExactThrowsOnBadChar() {
    var p = initObjUnderTest("abcd");
    assertThrows(IllegalStateException.class, () -> p.consumeExact('b'));
    assertEquals(TextPosition.start(), p.getPosition(), "Position did not advance");
  }

  @Test
  void consumeExactThrowsWhenExpectationIsTooLong() {
    var p = initObjUnderTest("");
    assertThrows(IllegalStateException.class, () -> p.consumeExact('a'));
  }

  @Test
  void consumeExactCharSequence() {
    var p = initObjUnderTest("abcd");
    assertDoesNotThrow(() -> p.consumeExact("ab"));
    assertEquals(TextPosition.of(0, 2, 2), p.getPosition(), "Position did advance");
  }

  @Test
  void consumeExactCharSequenceThrowsOnBadChar() {
    var p = initObjUnderTest("abcd");
    assertThrows(IllegalStateException.class, () -> p.consumeExact("abd"));
    assertEquals(TextPosition.start(), p.getPosition(), "Position did not advance");
  }

  @Test
  void consumeExactCharSequenceThrowsWhenExpectationIsTooLong() {
    var p = initObjUnderTest("abcd");
    assertThrows(IllegalStateException.class, () -> p.consumeExact("abcde"));
    assertEquals(TextPosition.start(), p.getPosition(), "Position did not advance");
  }

  private static TextParser initObjUnderTest(String text) {
    return new TextParser(text.codePoints().iterator());
  }
}
