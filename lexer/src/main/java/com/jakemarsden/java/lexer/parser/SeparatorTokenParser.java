package com.jakemarsden.java.lexer.parser;

import static com.jakemarsden.java.lexer.token.Separator.*;

import com.jakemarsden.java.lexer.text.TextParser;
import com.jakemarsden.java.lexer.token.Separator;
import com.jakemarsden.java.lexer.token.SeparatorToken;
import com.jakemarsden.java.lexer.token.Token;

public final class SeparatorTokenParser extends TokenParser {

  public SeparatorTokenParser(TextParser parser) {
    super(parser);
  }

  @Override
  Token maybeConsumeNextToken() {
    var p = this.parser;
    var ch0 = p.peek();

    switch (ch0) {
      case '{':
        return this.consumeExactToken('{', OPENING_BRACE);

      case '}':
        return this.consumeExactToken('}', CLOSING_BRACE);

      case '[':
        return this.consumeExactToken('[', OPENING_BRACKET);

      case ']':
        return this.consumeExactToken(']', CLOSING_BRACKET);

      case '(':
        return this.consumeExactToken('(', OPENING_PAREN);

      case ')':
        return this.consumeExactToken(')', CLOSING_PAREN);

      case '@':
        return this.consumeExactToken('@', ANNOTATION);

      case '.':
        if (p.peek(1) == '.' && p.peek(2) == '.') {
          return this.consumeExactToken("...", VARARG);
        } else {
          return this.consumeExactToken('.', DOT);
        }

      case ',':
        return this.consumeExactToken(',', ELEMENT_DELIMITER);

      case ':':
        if (p.peek(1) == ':') {
          return this.consumeExactToken("::", METHOD_REFERENCE);
        } else {
          return null;
        }

      case ';':
        return this.consumeExactToken(';', SEMICOLON);

      default:
        return null;
    }
  }

  private SeparatorToken consumeExactToken(int expected, Separator sep) {
    var startPos = this.parser.getPosition();
    this.parser.consumeExact(expected);
    return Token.separator(sep, startPos);
  }

  private SeparatorToken consumeExactToken(CharSequence expected, Separator sep) {
    var startPos = this.parser.getPosition();
    this.parser.consumeExact(expected);
    return Token.separator(sep, startPos);
  }
}
