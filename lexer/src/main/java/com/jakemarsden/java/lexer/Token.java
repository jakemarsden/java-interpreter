package com.jakemarsden.java.lexer;

import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import com.jakemarsden.java.lexer.text.TextPosition;

public final class Token {

  public static Token of(TokenType type, TextPosition position, String source) {
    return new Token(type, position, source);
  }

  private final TokenType type;
  private final TextPosition position;
  private final String source;

  private Token(TokenType type, TextPosition position, String source) {
    this.type = requireNonNull(type);
    this.position = requireNonNull(position);
    this.source = requireNonNull(source);
  }

  public TokenType type() {
    return this.type;
  }

  public TextPosition position() {
    return this.position;
  }

  public String source() {
    return this.source;
  }

  @Override
  public String toString() {
    var type = this.type();

    if (type.isSingleChar() || type.isWhitespace())
      return format("%s[%s, %s]", this.getClass().getSimpleName(), this.position(), this.type());

    return format(
        "%s[%s, %s, \"%s\"]",
        this.getClass().getSimpleName(), this.position(), this.type(), this.source());
  }

  @Override
  public int hashCode() {
    return hash(this.type(), this.position(), this.source());
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Token)) return false;
    var obj = (Token) o;
    return obj.type() == this.type()
        && obj.position().equals(this.position())
        && obj.source().equals(this.source());
  }
}
