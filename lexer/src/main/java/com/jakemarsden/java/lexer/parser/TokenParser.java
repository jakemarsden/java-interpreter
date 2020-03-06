package com.jakemarsden.java.lexer.parser;

import static java.util.Objects.requireNonNull;

import com.jakemarsden.java.lexer.text.TextParser;
import com.jakemarsden.java.lexer.token.Token;
import java.util.Optional;

public abstract class TokenParser {

  final TextParser parser;

  TokenParser(TextParser parser) {
    this.parser = requireNonNull(parser);
  }

  public final Optional<? extends Token> maybeConsumeNext() {
    return Optional.ofNullable(this.maybeConsumeNextToken());
  }

  abstract Token maybeConsumeNextToken();
}
