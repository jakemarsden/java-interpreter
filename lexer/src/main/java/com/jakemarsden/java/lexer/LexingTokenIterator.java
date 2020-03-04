package com.jakemarsden.java.lexer;

import static com.jakemarsden.java.lexer.LexerUtils.isNumberLiteral;
import static com.jakemarsden.java.lexer.TokenType.*;
import static com.jakemarsden.java.lexer.text.TextParser.EOF;
import static java.lang.String.format;
import static java.util.function.Predicate.not;
import static org.fissore.slf4j.FluentLoggerFactory.getLogger;

import com.jakemarsden.java.lexer.text.TextParser;
import com.jakemarsden.java.lexer.text.UnmodifiableTextParser;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import org.fissore.slf4j.FluentLogger;

/**
 * Adapts a sequence of code points, represented by a {@link PrimitiveIterator.OfInt}, into a
 * sequence of tokens, represented by a {@link com.jakemarsden.java.lexer.TokenIterator}.
 */
final class LexingTokenIterator implements TokenIterator {

  private static final FluentLogger LOGGER = getLogger(LexingTokenIterator.class);

  private final TextParser parser;

  LexingTokenIterator(PrimitiveIterator.OfInt codePoints) {
    this.parser = new TextParser(codePoints);
  }

  @Override
  public boolean hasNext() {
    return this.parser.hasRemaining();
  }

  @Override
  public Token next() {
    var p = this.parser;
    if (!p.hasRemaining(1)) throw new NoSuchElementException();

    if (this.isWhitespace(p)) return this.consumeWhitespaceToken(p);
    if (this.isLineCommentStart(p)) return this.consumeLineCommentToken(p);
    if (this.isBlockCommentStart(p)) return this.consumeBlockCommentToken(p);
    if (this.isNullLiteral(p)) return this.consumeNullLiteralToken(p);
    if (this.isBooleanLiteral(p)) return this.consumeBooleanLiteralToken(p);
    if (this.isCharLiteralStart(p)) return this.consumeCharLiteralToken(p);
    if (this.isStringLiteralStart(p)) return this.consumeStringLiteralToken(p);
    if (this.isNumberLiteralStart(p)) return this.consumeNumberLiteralToken(p);
    if (this.isIdentifierOrKeywordStart(p)) return this.consumeIdentifierOrKeywordToken(p);

    if (this.isSingleCharToken(p)) return this.consumeSingleCharToken(p);

    var token = this.consumeInvalidToken(p);
    LOGGER.warn().log(() -> format("Invalid token at %s: \"%s\"", token.position(), token.value()));
    return token;
  }

  private Token consumeInvalidToken(TextParser p) {
    var tokenBuf = new StringBuilder();
    var startPos = p.getPosition();
    p.consumeWhile(tokenBuf, not(this::isLiteralEnd));
    return Token.of(INVALID, tokenBuf.toString(), startPos);
  }

  private Token consumeWhitespaceToken(TextParser p) {
    var tokenBuf = new StringBuilder();
    var startPos = p.getPosition();
    p.consumeWhile(tokenBuf, this::isWhitespace);
    return Token.of(WHITESPACE, tokenBuf.toString(), startPos);
  }

  private Token consumeLineCommentToken(TextParser p) {
    // terminating newline is **not** part of the token
    var startPos = p.getPosition();
    var tokenBuf = new StringBuilder();
    p.consumeWhile(tokenBuf, not(this::isLineCommentEnd));
    return Token.of(COMMENT_LINE, tokenBuf.toString(), startPos);
  }

  private Token consumeBlockCommentToken(TextParser p) {
    var startPos = p.getPosition();
    var tokenBuf = new StringBuilder();
    p.consumeExact('/');
    p.consumeExact('*');
    tokenBuf.append('/');
    tokenBuf.append('*');

    var properlyClosed = p.consumeWhile(tokenBuf, not(this::isBlockCommentEnd));
    if (properlyClosed) {
      p.consumeExact('*');
      p.consumeExact('/');
      tokenBuf.append('*');
      tokenBuf.append('/');
    }
    return Token.of(properlyClosed ? COMMENT_BLOCK : INVALID, tokenBuf.toString(), startPos);
  }

  private Token consumeNullLiteralToken(TextParser p) {
    var startPos = p.getPosition();
    p.consumeExact('n');
    p.consumeExact('u');
    p.consumeExact('l');
    p.consumeExact('l');
    return Token.of(LITERAL_NULL, "null", startPos);
  }

  private Token consumeBooleanLiteralToken(TextParser p) {
    var startPos = p.getPosition();
    if (p.peek() == 't') {
      p.consumeExact('t');
      p.consumeExact('r');
      p.consumeExact('u');
      p.consumeExact('e');
      return Token.of(LITERAL_BOOLEAN, "true", startPos);
    } else {
      p.consumeExact('f');
      p.consumeExact('a');
      p.consumeExact('l');
      p.consumeExact('s');
      p.consumeExact('e');
      return Token.of(LITERAL_BOOLEAN, "false", startPos);
    }
  }

  private Token consumeCharLiteralToken(TextParser p) {
    var startPos = p.getPosition();
    var tokenBuf = new StringBuilder();
    p.consumeExact('\'');
    tokenBuf.append('\'');

    var properlyClosed = p.consumeWhile(tokenBuf, not(this::isCharLiteralEnd));
    if (properlyClosed) {
      p.consumeExact('\'');
      tokenBuf.append('\'');
    }
    return Token.of(properlyClosed ? LITERAL_CHAR : INVALID, tokenBuf.toString(), startPos);
  }

  private Token consumeStringLiteralToken(TextParser p) {
    var startPos = p.getPosition();
    var tokenBuf = new StringBuilder();
    p.consumeExact('"');
    tokenBuf.append('"');

    var properlyClosed = p.consumeWhile(tokenBuf, not(this::isStringLiteralEnd));
    if (properlyClosed) {
      p.consumeExact('"');
      tokenBuf.append('"');
    }
    return Token.of(properlyClosed ? LITERAL_STRING : INVALID, tokenBuf.toString(), startPos);
  }

  private Token consumeNumberLiteralToken(TextParser p) {
    var startPos = p.getPosition();
    var tokenBuf = new StringBuilder();
    p.consumeWhile(tokenBuf, not(this::isNumberLiteralEnd));

    var token = tokenBuf.toString();
    if (isNumberLiteral(token)) return Token.of(LITERAL_NUMBER, token, startPos);
    return Token.of(INVALID, token, startPos);
  }

  private Token consumeIdentifierOrKeywordToken(TextParser p) {
    var startPos = p.getPosition();
    var tokenBuf = new StringBuilder();
    p.consumeWhile(tokenBuf, not(this::isIdentifierOrKeywordEnd));

    var token = tokenBuf.toString();
    if (Keyword.isKeyword(token)) return Token.of(KEYWORD, token, startPos);
    if (this.isValidIdentifier(token)) return Token.of(IDENTIFIER, token, startPos);
    return Token.of(INVALID, token, startPos);
  }

  private Token consumeSingleCharToken(TextParser p) {
    var startPos = p.getPosition();
    var ch = p.consume();
    var type =
        TokenType.streamValues()
            .filter(TokenType::isSingleChar)
            .filter(it -> it.getSingleChar() == ch)
            .findAny()
            .orElseThrow();
    return Token.of(type, Character.toString(ch), startPos);
  }

  private boolean isWhitespace(UnmodifiableTextParser p) {
    return Character.isWhitespace(p.peek());
  }

  private boolean isLineCommentStart(UnmodifiableTextParser p) {
    return p.peek() == '/' && p.peek(1) == '/';
  }

  private boolean isLineCommentEnd(UnmodifiableTextParser p) {
    return p.peek() == '\n';
  }

  private boolean isBlockCommentStart(UnmodifiableTextParser p) {
    return p.peek() == '/' && p.peek(1) == '*';
  }

  private boolean isBlockCommentEnd(UnmodifiableTextParser p) {
    return p.peek() == '*' && p.peek(1) == '/';
  }

  private boolean isNullLiteral(UnmodifiableTextParser p) {
    return this.isLiteral(p, "null");
  }

  private boolean isBooleanLiteral(UnmodifiableTextParser p) {
    return this.isLiteral(p, "true") || this.isLiteral(p, "false");
  }

  private boolean isLiteral(UnmodifiableTextParser p, String literal) {
    for (int charIdx = 0; charIdx < literal.length(); charIdx++) {
      if (p.peek(charIdx) != literal.charAt(charIdx)) return false;
    }
    return isLiteralEnd(p.peek(literal.length()));
  }

  private boolean isLiteralEnd(UnmodifiableTextParser p) {
    return this.isLiteralEnd(p.peek());
  }

  private boolean isLiteralEnd(int ch) {
    return Character.isWhitespace(ch) || this.isSingleCharToken(ch) || ch == EOF;
  }

  private boolean isCharLiteralStart(UnmodifiableTextParser p) {
    return p.peek() == '\'';
  }

  private boolean isCharLiteralEnd(UnmodifiableTextParser p) {
    // TODO: Account for escaped single quotes
    return p.peek() == '\'';
  }

  private boolean isStringLiteralStart(UnmodifiableTextParser p) {
    return p.peek() == '"';
  }

  private boolean isStringLiteralEnd(UnmodifiableTextParser p) {
    // TODO: Account for escaped double quotes
    return p.peek() == '"';
  }

  private boolean isNumberLiteralStart(UnmodifiableTextParser p) {
    return Character.isDigit(p.peek()) || p.peek() == '.' && Character.isDigit(p.peek(1));
  }

  private boolean isNumberLiteralEnd(UnmodifiableTextParser p) {
    return this.isLiteralEnd(p) && p.peek() != '.';
  }

  private boolean isIdentifierOrKeywordStart(UnmodifiableTextParser p) {
    return Character.isJavaIdentifierStart(p.peek());
  }

  private boolean isIdentifierOrKeywordEnd(UnmodifiableTextParser p) {
    return this.isLiteralEnd(p);
  }

  private boolean isValidIdentifier(String str) {
    return str.codePoints().allMatch(Character::isJavaIdentifierPart);
  }

  private boolean isSingleCharToken(UnmodifiableTextParser p) {
    return this.isSingleCharToken(p.peek());
  }

  private boolean isSingleCharToken(int ch) {
    return TokenType.streamValues()
        .filter(TokenType::isSingleChar)
        .anyMatch(it -> it.getSingleChar() == ch);
  }
}
