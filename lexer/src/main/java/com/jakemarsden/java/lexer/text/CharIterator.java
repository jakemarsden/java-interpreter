package com.jakemarsden.java.lexer.text;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

/** An {@link Iterator} specialized for {@code char} values. */
public interface CharIterator extends PrimitiveIterator<Character, CharConsumer> {

  /**
   * Returns {@code true} if there are more characters, i.e. if it's safe to call {@link
   * #nextChar()}.
   *
   * @return {@code true} if there are more characters
   */
  @Override
  boolean hasNext();

  /**
   * Returns the next character.
   *
   * @return the next character
   * @throws NoSuchElementException if there are no more characters
   */
  char nextChar();

  /**
   * Returns the next character, <strong>boxed</strong>.
   *
   * <p><strong>Always prefer {@link #nextChar()} when possible</strong>.
   *
   * @return the next character, <strong>boxed</strong>
   * @throws NoSuchElementException if there are no more characters
   */
  @Override
  default Character next() {
    return this.nextChar();
  }

  /**
   * Performs the given action for each remaining character until all characters have been processed
   * or the action throws an exception. Actions are performed in the order of iteration, if that
   * order is specified. Exceptions thrown by the action are relayed to the caller.
   *
   * @param action the action to be performed for each character
   */
  @Override
  default void forEachRemaining(CharConsumer action) {
    requireNonNull(action);
    while (this.hasNext()) action.accept(this.nextChar());
  }

  /**
   * Performs the given action for each remaining <strong>boxed</strong> character until all
   * characters have been processed or the action throws an exception. Actions are performed in the
   * order of iteration, if that order is specified. Exceptions thrown by the action are relayed to
   * the caller.
   *
   * <p><strong>Always prefer {@link #forEachRemaining(CharConsumer)} when possible</strong>.
   *
   * @param action the action to be performed for each <strong>boxed</strong> character
   */
  @Override
  default void forEachRemaining(Consumer<? super Character> action) {
    if (action instanceof CharConsumer) {
      this.forEachRemaining((CharConsumer) action);
      return;
    }
    requireNonNull(action);
    this.forEachRemaining((CharConsumer) action::accept);
  }
}
