package com.jakemarsden.java.lexer.token;

import static com.jakemarsden.java.lexer.token.TokenType.CHARACTER_LITERAL;
import static com.jakemarsden.java.lexer.util.StringUtils.strContentEquals;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import com.jakemarsden.java.lexer.text.TextPosition;

public final class CharacterLiteralToken extends Token {

  private final CharSequence value;

  CharacterLiteralToken(CharSequence value, TextPosition position) {
    super(CHARACTER_LITERAL, position);
    this.value = requireNonNull(value);
  }

  /**
   * Returns the value of the literal, including any opening/closing markers such as {@code "'"}.
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
    var obj = (CharacterLiteralToken) o;
    return strContentEquals(this.value, obj.value);
  }
}
