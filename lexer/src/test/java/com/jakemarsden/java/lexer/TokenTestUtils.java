package com.jakemarsden.java.lexer;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.jakemarsden.java.lexer.text.TextPosition;
import java.util.Arrays;
import java.util.Iterator;

final class TokenTestUtils {

  static Token createToken(TokenType type, String value, int col) {
    return createToken(type, value, 0, col, 0);
  }

  static Token createToken(TokenType type, String value, int line, int col, int lineOffset) {
    var position = TextPosition.of(line, col, lineOffset + col);
    return Token.of(type, value, position);
  }

  static void assertTokensEquals(Iterator<Token> actualItr, Token... expected) {
    var expectedItr = Arrays.stream(expected).iterator();
    assertTokensEquals(expectedItr, actualItr);
  }

  static void assertTokensEquals(Iterator<Token> expectedItr, Iterator<Token> actualItr) {
    int idx = 0;
    while (expectedItr.hasNext() && actualItr.hasNext()) {
      var expectedToken = expectedItr.next();
      var actualToken = actualItr.next();
      assertEquals(expectedToken, actualToken, "Token at index " + idx);
      idx++;
    }

    var finalIdx = idx;
    assertFalse(
        expectedItr.hasNext(),
        () -> format("Missing token at index %d: %s", finalIdx, expectedItr.next()));
    assertFalse(
        actualItr.hasNext(),
        () -> format("Unexpected token at index %d: %s", finalIdx, actualItr.next()));
  }

  private TokenTestUtils() {
    throw new UnsupportedOperationException();
  }
}
