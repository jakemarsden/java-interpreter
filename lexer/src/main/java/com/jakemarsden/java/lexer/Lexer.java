package com.jakemarsden.java.lexer;

import static com.jakemarsden.java.lexer.TokenType.*;
import static java.util.Objects.requireNonNull;

import com.jakemarsden.java.lexer.text.CharIterator;
import com.jakemarsden.java.lexer.text.TextParser;
import com.jakemarsden.java.lexer.text.TextPosition;

public final class Lexer {

  public TokenIterator lex(CharIterator source) {
    var parser = new TextParser(source);
    return new LexTokenIterator(parser);
  }

  private static final class LexTokenIterator implements TokenIterator {

    private static final TokenType[] SINGLE_CHAR_TOKENS;

    static {
      SINGLE_CHAR_TOKENS = new TokenType[0x80];
      for (var tokenType : TokenType.values()) {
        if (!tokenType.isSingleChar()) continue;
        SINGLE_CHAR_TOKENS[tokenType.getSingleChar()] = tokenType;
      }
    }

    private final TextParser parser;

    LexTokenIterator(TextParser parser) {
      this.parser = requireNonNull(parser);
    }

    @Override
    public boolean hasNext() {
      return this.parser.hasNext();
    }

    @Override
    public Token next() {
      var position = this.parser.getPosition();
      var c = this.parser.peek();

      if (SINGLE_CHAR_TOKENS[c] != null) {
        this.parser.consume();
        return createToken(SINGLE_CHAR_TOKENS[c], position, c);
      }

      if (Character.isWhitespace(c)) {
        var token = this.parser.consumeWhile(Character::isWhitespace);
        return createToken(WHITESPACE, position, token);
      }

      if (isJavaNumberLiteralStart(c)) {
        var token = this.parser.consumeWhile(LexTokenIterator::isJavaNumberLiteralPart);
        return createToken(LITERAL_NUMBER, position, token);
      }

      if (Character.isJavaIdentifierStart(c)) {
        var token = this.parser.consumeWhile(Character::isJavaIdentifierPart);
        var type = IDENTIFIER;
        if (Keyword.isKeyword(token)) return createToken(KEYWORD, position, token);
        if (token.equals("true")) return createToken(LITERAL_BOOLEAN, position, token);
        if (token.equals("false")) return createToken(LITERAL_BOOLEAN, position, token);
        if (token.equals("null")) return createToken(LITERAL_NULL, position, token);
        return createToken(IDENTIFIER, position, token);
      }

      if (c == '\'') {
        // TODO: include escaped single quotes
        this.parser.consumeExact('\'');
        var token = '\'' + this.parser.consumeWhile(it -> it != '\'') + '\'';
        this.parser.consumeExact('\'');
        return createToken(LITERAL_CHAR, position, token);
      }

      if (c == '"') {
        // TODO: include escaped double quotes
        this.parser.consumeExact('"');
        var token = '"' + this.parser.consumeWhile(it -> it != '"') + '"';
        this.parser.consumeExact('"');
        return createToken(LITERAL_STRING, position, token);
      }

      this.parser.consume();
      return createToken(INVALID, position, c);
    }

    private static Token createToken(TokenType type, TextPosition position, char value) {
      return Token.of(type, position, String.valueOf(value));
    }

    private static Token createToken(TokenType type, TextPosition position, String value) {
      return Token.of(type, position, value);
    }

    private static boolean isJavaNumberLiteralStart(int ch) {
      return (ch >= '0' && ch <= '9') || ch == '+' || ch == '-';
    }

    private static boolean isJavaNumberLiteralPart(int ch) {
      if (isJavaNumberLiteralStart(ch)) return true;
      if (ch >= 'a' && ch <= 'f') return true;
      if (ch >= 'A' && ch <= 'F') return true;
      switch (ch) {
        case '.':
        case '_':
        case 'l':
        case 'L':
        case 'x':
        case 'X':
          return true;
        default:
          return false;
      }
    }
  }
}
