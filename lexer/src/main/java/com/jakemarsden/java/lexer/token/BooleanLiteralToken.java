package com.jakemarsden.java.lexer.token;

import static com.jakemarsden.java.lexer.token.TokenType.BOOLEAN_LITERAL;
import static java.util.Objects.hash;

import com.jakemarsden.java.lexer.text.TextPosition;

public final class BooleanLiteralToken extends Token {

  private final boolean value;

  BooleanLiteralToken(boolean value, TextPosition position) {
    super(BOOLEAN_LITERAL, position);
    this.value = value;
  }

  public boolean value() {
    return this.value;
  }

  @Override
  CharSequence valueAsString() {
    return Boolean.toString(this.value);
  }

  @Override
  public int hashCode() {
    return hash(super.hashCode(), this.value);
  }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) return false;
    var obj = (BooleanLiteralToken) o;
    return this.value == obj.value;
  }
}
