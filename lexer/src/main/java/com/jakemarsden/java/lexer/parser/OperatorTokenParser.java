package com.jakemarsden.java.lexer.parser;

import static com.jakemarsden.java.lexer.token.Operator.*;

import com.jakemarsden.java.lexer.text.TextParser;
import com.jakemarsden.java.lexer.token.Operator;
import com.jakemarsden.java.lexer.token.OperatorToken;
import com.jakemarsden.java.lexer.token.Token;

public final class OperatorTokenParser extends TokenParser {

  public OperatorTokenParser(TextParser parser) {
    super(parser);
  }

  @Override
  Token maybeConsumeNextToken() {
    var p = this.parser;
    var ch0 = p.peek();
    var ch1 = p.peek(1);

    switch (ch0) {
      case '=':
        if (ch1 == '=') {
          return this.consumeExactToken("==", EQUAL_TO);
        } else {
          return this.consumeExactToken("=", ASSIGNMENT);
        }

      case '+':
        if (ch1 == '+') {
          return this.consumeExactToken("++", INCREMENT);
        } else if (ch1 == '=') {
          return this.consumeExactToken("+=", ADDITION_ASSIGNMENT);
        } else {
          return this.consumeExactToken('+', ADDITION);
        }

      case '/':
        if (ch1 == '=') {
          return this.consumeExactToken("/=", DIVISION_ASSIGNMENT);
        } else {
          return this.consumeExactToken("/", DIVISION);
        }

      case '*':
        if (ch1 == '=') {
          return this.consumeExactToken("*=", MULTIPLICATION_ASSIGNMENT);
        } else {
          return this.consumeExactToken('*', MULTIPLICATION);
        }

      case '%':
        if (ch1 == '=') {
          return this.consumeExactToken("%=", REMAINDER_ASSIGNMENT);
        } else {
          return this.consumeExactToken('%', REMAINDER);
        }

      case '-':
        if (ch1 == '-') {
          return this.consumeExactToken("--", DECREMENT);
        } else if (ch1 == '=') {
          return this.consumeExactToken("-=", SUBTRACTION_ASSIGNMENT);
        } else if (ch1 == '>') {
          return this.consumeExactToken("->", LAMBDA);
        } else {
          return this.consumeExactToken('-', SUBTRACTION);
        }

      case '&':
        if (ch1 == '&') {
          return this.consumeExactToken("&&", LOGICAL_AND);
        } else if (ch1 == '=') {
          return this.consumeExactToken("&=", BITWISE_AND_ASSIGNMENT);
        } else {
          return this.consumeExactToken('&', BITWISE_AND);
        }

      case '~':
        return this.consumeExactToken("~", BITWISE_COMPLEMENT);

      case '|':
        if (ch1 == '|') {
          return this.consumeExactToken("||", LOGICAL_IOR);
        } else if (ch1 == '=') {
          return this.consumeExactToken("|=", BITWISE_IOR_ASSIGNMENT);
        } else {
          return this.consumeExactToken('|', BITWISE_IOR);
        }

      case '^':
        if (ch1 == '=') {
          return this.consumeExactToken("^=", BITWISE_XOR_ASSIGNMENT);
        } else {
          return this.consumeExactToken('^', BITWISE_XOR);
        }

      case '<':
        if (ch1 == '<') {
          if (p.peek(2) == '=') {
            return this.consumeExactToken("<<=", LEFT_SHIFT_ASSIGNMENT);
          } else {
            return this.consumeExactToken("<<", LEFT_SHIFT);
          }
        } else if (ch1 == '=') {
          return this.consumeExactToken("<=", LESS_THAN_OR_EQUAL_TO);
        } else {
          return this.consumeExactToken('<', LESS_THAN);
        }

      case '>':
        if (ch1 == '>') {
          if (p.peek(2) == '>') {
            if (p.peek(3) == '=') {
              return this.consumeExactToken(">>>=", UNSIGNED_RIGHT_SHIFT_ASSIGNMENT);
            } else {
              return this.consumeExactToken(">>>", UNSIGNED_RIGHT_SHIFT);
            }
          } else if (p.peek(2) == '=') {
            return this.consumeExactToken(">>=", RIGHT_SHIFT_ASSIGNMENT);
          } else {
            return this.consumeExactToken(">>", RIGHT_SHIFT);
          }
        } else if (ch1 == '=') {
          return this.consumeExactToken(">=", GREATER_THAN_OR_EQUAL_TO);
        } else {
          return this.consumeExactToken(">", GREATER_THAN);
        }

      case '!':
        if (ch1 == '=') {
          return this.consumeExactToken("!=", NOT_EQUAL_TO);
        } else {
          return this.consumeExactToken('!', LOGICAL_COMPLEMENT);
        }

      case '?':
        return this.consumeExactToken('?', TERNARY1);

      case ':':
        return this.consumeExactToken(':', TERNARY2);

      default:
        return null;
    }
  }

  private OperatorToken consumeExactToken(int expected, Operator op) {
    var startPos = this.parser.getPosition();
    this.parser.consumeExact(expected);
    return Token.operator(op, startPos);
  }

  private OperatorToken consumeExactToken(CharSequence expected, Operator op) {
    var startPos = this.parser.getPosition();
    this.parser.consumeExact(expected);
    return Token.operator(op, startPos);
  }
}
