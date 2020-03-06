package com.jakemarsden.java.lexer.token;

import static com.jakemarsden.java.lexer.token.TokenType.WHITESPACE;
import static com.jakemarsden.java.lexer.util.StringUtils.codePointCount;
import static com.jakemarsden.java.lexer.util.StringUtils.strContentEquals;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import com.jakemarsden.java.lexer.text.TextPosition;

public final class WhitespaceToken extends Token {

  private final CharSequence value;

  WhitespaceToken(CharSequence value, TextPosition position) {
    super(WHITESPACE, position);
    this.value = requireNonNull(value);
  }

  public CharSequence value() {
    return this.value;
  }

  @Override
  CharSequence valueAsString() {
    return Integer.toString(codePointCount(this.value));
  }

  @Override
  public int hashCode() {
    return hash(super.hashCode(), this.value);
  }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) return false;
    var obj = (WhitespaceToken) o;
    return strContentEquals(this.value, obj.value);
  }
}
