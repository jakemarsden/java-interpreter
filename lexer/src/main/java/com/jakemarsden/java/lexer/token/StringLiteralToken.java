package com.jakemarsden.java.lexer.token;

import static com.jakemarsden.java.lexer.token.TokenType.STRING_LITERAL;
import static com.jakemarsden.java.lexer.util.StringUtils.strContentEquals;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import com.jakemarsden.java.lexer.text.TextPosition;

public final class StringLiteralToken extends Token {

  private final CharSequence value;

  StringLiteralToken(CharSequence value, TextPosition position) {
    super(STRING_LITERAL, position);
    this.value = requireNonNull(value);
  }

  /**
   * Returns the value of the literal, including any opening/closing markers such as {@code '"'}
   *
   * @return the value of the literal
   */
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
    var obj = (StringLiteralToken) o;
    return strContentEquals(this.value, obj.value);
  }
}
