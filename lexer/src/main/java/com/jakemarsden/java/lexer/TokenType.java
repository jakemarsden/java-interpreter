package com.jakemarsden.java.lexer;

import java.util.Arrays;
import java.util.stream.Stream;

public enum TokenType {
  // Single character tokens:
  ANGLE_LEFT('<'),
  ANGLE_RIGHT('>'),
  BRACE_LEFT('{'),
  BRACE_RIGHT('}'),
  BRACKET_LEFT('['),
  BRACKET_RIGHT(']'),
  DOT('.'),
  PAREN_LEFT('('),
  PAREN_RIGHT(')'),
  SEMICOLON(';'),

  // Multi-character tokens:
  COMMENT_BLOCK,
  COMMENT_LINE,
  IDENTIFIER,
  KEYWORD,
  LITERAL_BOOLEAN,
  LITERAL_CHAR,
  LITERAL_NULL,
  LITERAL_NUMBER,
  LITERAL_STRING,
  WHITESPACE,

  INVALID;

  public static Stream<TokenType> streamValues() {
    return Arrays.stream(TokenType.values());
  }

  private final char singleChar;

  TokenType() {
    this('\u0000');
  }

  TokenType(char singleChar) {
    this.singleChar = singleChar;
  }

  public boolean isValid() {
    return this != INVALID;
  }

  public boolean isWhitespace() {
    return this == WHITESPACE;
  }

  /** @return if tokens of this type represent a single character */
  public boolean isSingleChar() {
    return this.singleChar != '\u0000';
  }

  /**
   * @return the single character represented by tokens of this type
   * @throws UnsupportedOperationException if tokens of this type don't represent a single character
   */
  public char getSingleChar() {
    if (!this.isSingleChar()) throw new UnsupportedOperationException();
    return this.singleChar;
  }
}
