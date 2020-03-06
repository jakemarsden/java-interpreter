package com.jakemarsden.java.lexer.parser;

import com.jakemarsden.java.lexer.text.TextParser;
import com.jakemarsden.java.lexer.token.Token;

public final class WhitespaceTokenParser extends TokenParser {

  public WhitespaceTokenParser(TextParser parser) {
    super(parser);
  }

  @Override
  Token maybeConsumeNextToken() {
    if (Character.isWhitespace(this.parser.peek())) {
      var tokenBuf = new StringBuilder();
      var startPos = this.parser.getPosition();
      this.parser.consumeWhile(tokenBuf, p -> Character.isWhitespace(p.peek()));
      return Token.whitespace(tokenBuf, startPos);
    }

    return null;
  }
}
