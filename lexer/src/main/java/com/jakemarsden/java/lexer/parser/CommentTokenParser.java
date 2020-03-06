package com.jakemarsden.java.lexer.parser;

import static com.jakemarsden.java.lexer.token.CommentType.BLOCK;
import static com.jakemarsden.java.lexer.token.CommentType.LINE;

import com.jakemarsden.java.lexer.text.TextParser;
import com.jakemarsden.java.lexer.token.Token;

public final class CommentTokenParser extends TokenParser {

  public CommentTokenParser(TextParser parser) {
    super(parser);
  }

  @Override
  Token maybeConsumeNextToken() {
    var ch0 = this.parser.peek();
    var ch1 = this.parser.peek(1);

    if (ch0 == '/' && ch1 == '/') {
      // terminating newline **is not** part of the token
      var startPos = this.parser.getPosition();
      var tokenBuf = new StringBuilder();
      this.parser.consumeExact("//");
      tokenBuf.append("//");

      this.parser.consumeWhile(tokenBuf, p -> p.peek() != '\n');
      return Token.comment(LINE, tokenBuf, startPos);
    }

    if (ch0 == '/' && ch1 == '*') {
      // terminating "*/" **is** part of the token
      var startPos = this.parser.getPosition();
      var tokenBuf = new StringBuilder();
      this.parser.consumeExact("/*");
      tokenBuf.append("/*");

      var eof = !this.parser.consumeWhile(tokenBuf, p -> p.peek() != '*' || p.peek(1) != '/');
      if (eof) {
        // unclosed block comment is invalid
        return Token.invalid(tokenBuf, startPos);
      }

      this.parser.consumeExact("*/");
      tokenBuf.append("*/");
      return Token.comment(BLOCK, tokenBuf, startPos);
    }

    return null;
  }
}
