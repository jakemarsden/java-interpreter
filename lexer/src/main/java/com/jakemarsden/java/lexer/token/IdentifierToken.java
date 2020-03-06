package com.jakemarsden.java.lexer.token;

import static com.jakemarsden.java.lexer.token.TokenType.IDENTIFIER;
import static com.jakemarsden.java.lexer.util.StringUtils.strContentEquals;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import com.jakemarsden.java.lexer.text.TextPosition;

public final class IdentifierToken extends Token {

  private final CharSequence value;

  IdentifierToken(CharSequence value, TextPosition position) {
    super(IDENTIFIER, position);
    this.value = requireNonNull(value);
  }

  public CharSequence value() {
    return this.value;
  }

  @Override
  CharSequence valueAsString() {
    return this.value;
  }

  @Override
  public int hashCode() {
    return hash(super.hashCode(), this.value);
  }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) return false;
    var obj = (IdentifierToken) o;
    return strContentEquals(this.value, obj.value);
  }
}
