package com.jakemarsden.java.lexer.token;

import static com.jakemarsden.java.lexer.token.TokenType.SEPARATOR;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import com.jakemarsden.java.lexer.text.TextPosition;

public final class SeparatorToken extends Token {

  private final Separator separator;

  SeparatorToken(Separator type, TextPosition position) {
    super(SEPARATOR, position);
    this.separator = requireNonNull(type);
  }

  public Separator separator() {
    return this.separator;
  }

  @Override
  CharSequence valueAsString() {
    return this.separator.toString();
  }

  @Override
  public int hashCode() {
    return hash(super.hashCode(), this.separator);
  }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) return false;
    var obj = (SeparatorToken) o;
    return this.separator == obj.separator;
  }
}
