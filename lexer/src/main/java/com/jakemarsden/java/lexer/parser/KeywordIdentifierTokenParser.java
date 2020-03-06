package com.jakemarsden.java.lexer.parser;

import com.jakemarsden.java.lexer.text.TextParser;
import com.jakemarsden.java.lexer.token.Keyword;
import com.jakemarsden.java.lexer.token.Token;

public final class KeywordIdentifierTokenParser extends TokenParser {

  public KeywordIdentifierTokenParser(TextParser parser) {
    super(parser);
  }

  @Override
  Token maybeConsumeNextToken() {
    var ch0 = this.parser.peek();

    if (isKeywordOrIdentifierStart(ch0)) {
      var startPos = this.parser.getPosition();
      var tokenBuf = new StringBuilder();
      this.parser.consumeWhile(tokenBuf, p -> isKeywordOrIdentifierPart(p.peek()));

      var keyword = Keyword.of(tokenBuf.toString());
      return keyword.isPresent()
          ? Token.keyword(keyword.get(), startPos)
          : Token.identifier(tokenBuf, startPos);
    }

    return null;
  }

  static boolean isKeywordOrIdentifierStart(int ch0) {
    return (ch0 >= 'a' && ch0 <= 'z') || (ch0 >= 'A' && ch0 <= 'Z') || ch0 == '$' || ch0 == '_';
  }

  static boolean isKeywordOrIdentifierPart(int ch0) {
    return isKeywordOrIdentifierStart(ch0) || (ch0 >= '0' && ch0 <= '9');
  }
}
