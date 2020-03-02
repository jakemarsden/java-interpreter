package com.jakemarsden.java.lexer;

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
  IDENTIFIER,
  KEYWORD,
  QUOTED_DOUBLE,
  QUOTED_SINGLE,
  WHITESPACE,

  INVALID;

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

  public boolean isSingleChar() {
    return this.singleChar != '\u0000';
  }

  public char getSingleChar() {
    if (!this.isSingleChar()) throw new UnsupportedOperationException();
    return this.singleChar;
  }
}
