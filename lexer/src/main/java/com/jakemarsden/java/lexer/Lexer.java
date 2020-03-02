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

      if (Character.isJavaIdentifierStart(c)) {
        var token = this.parser.consumeWhile(Character::isJavaIdentifierPart);
        return createToken(Keyword.isKeyword(token) ? KEYWORD : IDENTIFIER, position, token);
      }

      if (c == '\'') {
        // TODO: include escaped single quotes
        this.parser.consumeExact('\'');
        var token = '\'' + this.parser.consumeWhile(it -> it != '\'') + '\'';
        this.parser.consumeExact('\'');
        return createToken(QUOTED_SINGLE, position, token);
      }

      if (c == '"') {
        // TODO: include escaped double quotes
        this.parser.consumeExact('"');
        var token = '"' + this.parser.consumeWhile(it -> it != '"') + '"';
        this.parser.consumeExact('"');
        return createToken(QUOTED_DOUBLE, position, token);
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
  }
}
