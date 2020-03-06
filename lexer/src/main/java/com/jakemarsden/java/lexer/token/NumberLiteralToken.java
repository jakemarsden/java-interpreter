package com.jakemarsden.java.lexer.token;

import static com.jakemarsden.java.lexer.token.TokenType.NUMBER_LITERAL;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import com.jakemarsden.java.lexer.text.TextPosition;

public final class NumberLiteralToken extends Token {

  private final NumberLiteral value;

  NumberLiteralToken(NumberLiteral value, TextPosition position) {
    super(NUMBER_LITERAL, position);
    this.value = requireNonNull(value);
  }

  public NumberLiteral value() {
    return this.value;
  }

  @Override
  CharSequence valueAsString() {
    return this.value.toString();
  }

  @Override
  public int hashCode() {
    return hash(super.hashCode(), this.value);
  }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) return false;
    var obj = (NumberLiteralToken) o;
    return this.value.equals(obj.value);
  }
}
