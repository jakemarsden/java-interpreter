package com.jakemarsden.java.lexer.parser;

import static com.jakemarsden.java.lexer.util.NumberUtils.DEC;
import static com.jakemarsden.java.lexer.util.NumberUtils.isDigit;
import static com.jakemarsden.java.lexer.util.NumberUtils.toDigit;

import com.jakemarsden.java.lexer.text.TextParser;
import com.jakemarsden.java.lexer.token.NumberLiteral;
import com.jakemarsden.java.lexer.token.Token;

public final class NumberLiteralTokenParser extends TokenParser {

  public NumberLiteralTokenParser(TextParser parser) {
    super(parser);
  }

  @Override
  Token maybeConsumeNextToken() {
    // TODO: Only currently supporting decimal integers
    var p = this.parser;
    var ch0 = p.peek();

    if (!isDigit(ch0, DEC)) return null;

    var startPos = p.getPosition();
    var rawValue = new StringBuilder();
    int value = 0;
    int radix = DEC;

    int prevCh;
    int ch = -1;
    while (true) {
      prevCh = ch;
      ch = p.peek();

      if (isDigit(ch, radix)) {
        p.consumeExact(ch);
        rawValue.appendCodePoint(ch);
        value *= radix;
        value += toDigit(ch, radix);

      } else if (ch == '_' && isDigit(prevCh, radix) && isDigit(p.peek(1), radix)) {
        p.consumeExact('_');
        rawValue.appendCodePoint('_');

      } else {
        // not a valid addition to the number literal
        break;
      }
    }

    return Token.numberLiteral(NumberLiteral.of(value, rawValue), startPos);
  }
}
