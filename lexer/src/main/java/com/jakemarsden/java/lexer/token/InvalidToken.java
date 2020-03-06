package com.jakemarsden.java.lexer.token;

import static com.jakemarsden.java.lexer.token.TokenType.INVALID;
import static com.jakemarsden.java.lexer.util.StringUtils.strContentEquals;
import static java.util.Objects.hash;

import com.jakemarsden.java.lexer.text.TextPosition;

public final class InvalidToken extends Token {

  private final CharSequence value;

  InvalidToken(CharSequence value, TextPosition position) {
    super(INVALID, position);
    this.value = value;
  }

  public CharSequence value() {
    return this.value;
  }

  @Override
  public int hashCode() {
    return hash(super.hashCode(), this.value);
  }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) return false;
    var obj = (InvalidToken) o;
    return strContentEquals(this.value, obj.value);
  }
}
