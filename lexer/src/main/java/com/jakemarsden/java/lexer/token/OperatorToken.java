package com.jakemarsden.java.lexer.token;

import static com.jakemarsden.java.lexer.token.TokenType.OPERATOR;
import static java.util.Objects.hash;
import static java.util.Objects.requireNonNull;

import com.jakemarsden.java.lexer.text.TextPosition;

public final class OperatorToken extends Token {

  private final Operator operator;

  OperatorToken(Operator type, TextPosition position) {
    super(OPERATOR, position);
    this.operator = requireNonNull(type);
  }

  public Operator operator() {
    return this.operator;
  }

  @Override
  CharSequence valueAsString() {
    return this.operator.toString();
  }

  @Override
  public int hashCode() {
    return hash(super.hashCode(), this.operator);
  }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) return false;
    var obj = (OperatorToken) o;
    return this.operator == obj.operator;
  }
}
