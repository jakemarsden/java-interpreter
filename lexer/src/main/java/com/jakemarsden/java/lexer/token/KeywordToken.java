package com.jakemarsden.java.lexer.token;

import static com.jakemarsden.java.lexer.token.TokenType.KEYWORD;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import com.jakemarsden.java.lexer.text.TextPosition;

public final class KeywordToken extends Token {

  private final Keyword keyword;

  KeywordToken(Keyword type, TextPosition position) {
    super(KEYWORD, position);
    this.keyword = requireNonNull(type);
  }

  public Keyword keyword() {
    return this.keyword;
  }

  @Override
  CharSequence valueAsString() {
    return this.keyword.toString();
  }

  @Override
  public int hashCode() {
    return hash(super.hashCode(), this.keyword);
  }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) return false;
    var obj = (KeywordToken) o;
    return this.keyword == obj.keyword;
  }
}
