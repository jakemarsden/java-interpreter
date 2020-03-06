package com.jakemarsden.java.lexer.parser;

import com.jakemarsden.java.lexer.text.TextParser;
import com.jakemarsden.java.lexer.token.Token;

public final class StringCharacterLiteralTokenParser extends TokenParser {

  public StringCharacterLiteralTokenParser(TextParser parser) {
    super(parser);
  }

  @Override
  Token maybeConsumeNextToken() {
    var ch0 = this.parser.peek();

    if (ch0 == '\'') {
      var startPos = this.parser.getPosition();
      var tokenBuf = new StringBuilder();
      this.parser.consumeExact('\'');
      tokenBuf.append('\'');

      var eof = !this.parser.consumeWhile(tokenBuf, p -> p.peek() != '\'' && p.peek() != '\n');
      if (eof || this.parser.peek() != '\'') {
        // unclosed character literal is invalid
        return Token.invalid(tokenBuf, startPos);
      }

      this.parser.consumeExact('\'');
      tokenBuf.append('\'');
      return Token.characterLiteral(tokenBuf, startPos);
    }

    if (ch0 == '"') {
      var startPos = this.parser.getPosition();
      var tokenBuf = new StringBuilder();
      this.parser.consumeExact('"');
      tokenBuf.append('"');

      var eof = !this.parser.consumeWhile(tokenBuf, p -> p.peek() != '"' && p.peek() != '\n');
      if (eof || this.parser.peek() != '"') {
        // unclosed string literal is invalid
        return Token.invalid(tokenBuf, startPos);
      }

      this.parser.consumeExact('"');
      tokenBuf.append('"');
      return Token.stringLiteral(tokenBuf, startPos);
    }

    return null;
  }
}
