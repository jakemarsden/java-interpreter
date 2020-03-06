package com.jakemarsden.java.lexer;

import static com.jakemarsden.java.lexer.text.TextParser.EOF;
import static java.lang.String.format;
import static org.fissore.slf4j.FluentLoggerFactory.getLogger;

import com.jakemarsden.java.lexer.parser.*;
import com.jakemarsden.java.lexer.text.TextParser;
import com.jakemarsden.java.lexer.token.Token;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.PrimitiveIterator;
import org.fissore.slf4j.FluentLogger;

/**
 * Adapts a sequence of code points, represented by a {@link PrimitiveIterator.OfInt}, into a
 * sequence of tokens, represented by a {@link com.jakemarsden.java.lexer.TokenIterator}.
 */
final class LexingTokenIterator implements TokenIterator {

  private static final FluentLogger LOGGER = getLogger(LexingTokenIterator.class);

  private final TextParser parser;

  private final CommentTokenParser commentParser;
  private final KeywordIdentifierTokenParser keywordIdentifierParser;
  private final NumberLiteralTokenParser numberLiteralParser;
  private final OperatorTokenParser operatorParser;
  private final SeparatorTokenParser separatorParser;
  private final SimpleLiteralTokenParser simpleLiteralParser;
  private final StringCharacterLiteralTokenParser stringCharacterLiteralParser;
  private final WhitespaceTokenParser whitespaceParser;

  LexingTokenIterator(PrimitiveIterator.OfInt codePoints) {
    this.parser = new TextParser(codePoints);
    this.commentParser = new CommentTokenParser(this.parser);
    this.keywordIdentifierParser = new KeywordIdentifierTokenParser(this.parser);
    this.numberLiteralParser = new NumberLiteralTokenParser(this.parser);
    this.operatorParser = new OperatorTokenParser(this.parser);
    this.separatorParser = new SeparatorTokenParser(this.parser);
    this.simpleLiteralParser = new SimpleLiteralTokenParser(this.parser);
    this.stringCharacterLiteralParser = new StringCharacterLiteralTokenParser(this.parser);
    this.whitespaceParser = new WhitespaceTokenParser(this.parser);
  }

  @Override
  public boolean hasNext() {
    return this.parser.hasRemaining();
  }

  @Override
  public Token next() {
    Optional<? extends Token> t;
    if ((t = this.whitespaceParser.maybeConsumeNext()).isPresent()) return t.get();
    if ((t = this.commentParser.maybeConsumeNext()).isPresent()) return t.get();

    if ((t = this.simpleLiteralParser.maybeConsumeNext()).isPresent()) return t.get();
    if ((t = this.numberLiteralParser.maybeConsumeNext()).isPresent()) return t.get();
    if ((t = this.stringCharacterLiteralParser.maybeConsumeNext()).isPresent()) return t.get();

    if ((t = this.keywordIdentifierParser.maybeConsumeNext()).isPresent()) return t.get();
    if ((t = this.separatorParser.maybeConsumeNext()).isPresent()) return t.get();
    if ((t = this.operatorParser.maybeConsumeNext()).isPresent()) return t.get();

    var invalidToken = this.consumeInvalidToken();
    if (invalidToken != null) {
      LOGGER.warn().log(() -> format("Invalid token: %s", invalidToken));
      return invalidToken;
    }

    throw new NoSuchElementException();
  }

  private Token consumeInvalidToken() {
    var startPos = this.parser.getPosition();
    var ch = this.parser.consume();
    if (ch == EOF) return null;
    return Token.invalid(Character.toString(ch), startPos);
  }
}
