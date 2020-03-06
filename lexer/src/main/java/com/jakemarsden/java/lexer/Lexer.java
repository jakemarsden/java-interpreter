package com.jakemarsden.java.lexer;

import com.jakemarsden.java.lexer.token.Token;
import java.util.PrimitiveIterator;

/**
 * Generates a sequence of language tokens from a sequence of code points.
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jls/se11/html/jls-3.html">The Java&reg;
 *     Language Specification: Java SE 11 Edition - Chapter 3. Lexical Structure</a>
 */
public final class Lexer {

  /**
   * Returns an iterator over a sequence of {@link Token}s, which are generated lazily by lexing
   * code points taken from the {@code source}.
   *
   * <p>Example usage:
   *
   * <pre><code>
   * String sourceCode = ...
   * var sourceCodeItr = sourceCode.codePoints().iterator();
   *
   * var lexer = new Lexer();
   * var tokens = lexer.lex(sourceCodeItr);
   * tokens.forEachRemaining(...);
   * </code></pre>
   *
   * @param source where to retrieve the characters for lexing into tokens
   * @return an iterator over the tokens found in the specified {@code source}
   */
  public TokenIterator lex(PrimitiveIterator.OfInt source) {
    return new LexingTokenIterator(source);
  }
}
