package com.jakemarsden.java.lexer.parser;

import static com.jakemarsden.java.lexer.parser.KeywordIdentifierTokenParser.isKeywordOrIdentifierPart;

import com.jakemarsden.java.lexer.text.TextParser;
import com.jakemarsden.java.lexer.token.Token;

public class SimpleLiteralTokenParser extends TokenParser {

  public SimpleLiteralTokenParser(TextParser parser) {
    super(parser);
  }

  @Override
  Token maybeConsumeNextToken() {
    var ch0 = this.parser.peek();

    if (ch0 == 'n'
        && this.parser.peek(1) == 'u'
        && this.parser.peek(2) == 'l'
        && this.parser.peek(3) == 'l'
        && !isKeywordOrIdentifierPart(this.parser.peek(4))) {
      var startPos = this.parser.getPosition();
      this.parser.consumeExact("null");
      return Token.nullLiteral(startPos);
    }

    if (ch0 == 't'
        && this.parser.peek(1) == 'r'
        && this.parser.peek(2) == 'u'
        && this.parser.peek(3) == 'e'
        && !isKeywordOrIdentifierPart(this.parser.peek(4))) {
      var startPos = this.parser.getPosition();
      this.parser.consumeExact("true");
      return Token.booleanLiteral(true, startPos);
    }

    if (ch0 == 'f'
        && this.parser.peek(1) == 'a'
        && this.parser.peek(2) == 'l'
        && this.parser.peek(3) == 's'
        && this.parser.peek(4) == 'e'
        && !isKeywordOrIdentifierPart(this.parser.peek(5))) {
      var startPos = this.parser.getPosition();
      this.parser.consumeExact("false");
      return Token.booleanLiteral(false, startPos);
    }

    return null;
  }
}
