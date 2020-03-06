package com.jakemarsden.java.lexer.token;

import static com.jakemarsden.java.lexer.token.TokenType.NULL_LITERAL;

import com.jakemarsden.java.lexer.text.TextPosition;

public final class NullLiteralToken extends Token {

  NullLiteralToken(TextPosition position) {
    super(NULL_LITERAL, position);
  }
}
