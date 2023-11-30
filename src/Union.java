import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A container object which will contain a value of one of two possible types.
 * Instances of this class are either left or right. A left instance contains a
 * value of type {@code T}, and a right instance contains a value of type
 * {@code U}.
 *
 * @param <T> The type of the left value.
 * @param <U> The type of the right value.
 */
public final class Union<T, U> {

    private final Object value;
    private final boolean isLeft;

    /**
     * Constructs a {@code Union} instance with a left value.
     *
     * @param leftValue The value to be contained in the left instance.
     * @param <T>       The type of the left value.
     * @param <U>       The type of the right value.
     * @return A {@code Union} instance with the given left value.
     * @throws NullPointerException if the left value is {@code null}.
     */
    public static <T, U> Union<T, U> ofLeft(T leftValue) { return new Union<>(leftValue, true); }

    /**
     * Constructs a {@code Union} instance with a right value.
     *
     * @param rightValue The value to be contained in the right instance.
     * @param <T>        The type of the left value.
     * @param <U>        The type of the right value.
     * @return A {@code Union} instance with the given right value.
     * @throws NullPointerException if the right value is {@code null}.
     */
    public static <T, U> Union<T, U> ofRight(U rightValue) { return new Union<>(rightValue, false); }

    /**
     * Private constructor for creating a {@code Union} instance.
     *
     * @param value  The value to be contained in the {@code Union} instance.
     * @param isLeft {@code true} if the instance is left; {@code false} if right.
     * @throws NullPointerException if {@code value} is {@code null}.
     */
    private Union(Object value, boolean isLeft) {
        Objects.requireNonNull(value);
        this.value = value;
        this.isLeft = isLeft;
    }

    /**
     * Checks if this {@code Union} instance contains a left value.
     *
     * @return {@code true} if this instance contains a left value; {@code false}
     *         otherwise.
     */
    public boolean isLeft() { return isLeft; }

    /**
     * Checks if this {@code Union} instance contains a right value.
     *
     * @return {@code true} if this instance contains a right value; {@code false}
     *         otherwise.
     */
    public boolean isRight() { return !isLeft; }

    /**
     * Retrieves the left value from this {@code Union} instance.
     *
     * @return The left value.
     * @throws IllegalStateException If this instance contains a right value.
     */
    @SuppressWarnings("unchecked")
    public T getLeft() {
        if (isRight())
            throw new IllegalStateException("Not a left value");
        return (T) value;
    }

    /**
     * Retrieves the right value from this {@code Union} instance.
     *
     * @return The right value.
     * @throws IllegalStateException If this instance contains a left value.
     */
    @SuppressWarnings("unchecked")
    public U getRight() {
        if (isLeft())
            throw new IllegalStateException("Not a right value");
        return (U) value;
    }

    /**
     * Applies a mapping function to the left value if present, returning a new
     * {@code Union} instance.
     *
     * @param mapper The mapping function to apply to the left value.
     * @param <R>    The type of the result of the mapping function.
     * @return A new {@code Union} instance containing the result of the mapping, or
     *         this instance if right.
     */
    @SuppressWarnings("unchecked")
    public <R> Union<R, U> mapLeft(Function<T, R> mapper) {
        if (isLeft())
            return Union.ofLeft(mapper.apply(getLeft()));
        return (Union<R, U>) this;
    }

    /**
     * Applies a mapping function to the right value if present, returning a new
     * {@code Union} instance.
     *
     * @param mapper The mapping function to apply to the right value.
     * @param <R>    The type of the result of the mapping function.
     * @return A new {@code Union} instance containing the result of the mapping, or
     *         this instance if left.
     */
    @SuppressWarnings("unchecked")
    public <R> Union<T, R> mapRight(Function<U, R> mapper) {
        if (isLeft())
            return Union.ofRight(mapper.apply(getRight()));
        return (Union<T, R>) this;
    }

    /**
     * Applies a mapping function to the left value if present, returning a new
     * {@code Union} instance. The mapping function returns a {@code Union} instance
     * itself, resulting in a flattened structure.
     *
     * @param mapper The mapping function to apply to the left value.
     * @param <R>    The type of the left value in the result {@code Union}
     *               instance.
     * @return A new {@code Union} instance resulting from the flattened mapping, or
     *         this instance if right.
     */
    @SuppressWarnings("unchecked")
    public <R> Union<R, U> flatMapLeft(Function<T, Union<R, U>> mapper) {
        if (isLeft())
            return mapper.apply(getLeft());
        return (Union<R, U>) this;
    }

    /**
     * Applies a mapping function to the right value if present, returning a new
     * {@code Union} instance. The mapping function returns a {@code Union} instance
     * itself, resulting in a flattened structure.
     *
     * @param mapper The mapping function to apply to the right value.
     * @param <R>    The type of the right value in the result {@code Union}
     *               instance.
     * @return A new {@code Union} instance resulting from the flattened mapping, or
     *         this instance if left.
     */
    @SuppressWarnings("unchecked")
    public <R> Union<T, R> flatMapRight(Function<U, Union<T, R>> mapper) {
        if (isLeft())
            return mapper.apply(getRight());
        return (Union<T, R>) this;
    }

    /**
     * Performs the given action on the left value if present.
     *
     * @param consumer The action to perform on the left value.
     */
    public void ifLeft(Consumer<T> consumer) {
        if (isLeft())
            consumer.accept(getLeft());
    }

    /**
     * Performs the given action on the right value if present.
     *
     * @param consumer The action to perform on the right value.
     */
    public void ifRight(Consumer<U> consumer) {
        if (isRight())
            consumer.accept(getRight());
    }

    /**
     * Returns the left value if present, otherwise returns the specified default
     * value.
     *
     * @param other The default value to return if this instance contains a right
     *              value.
     * @return The left value if present, otherwise the specified default value.
     */
    public T getLeftOrElse(T other) {
        if (isLeft())
            return getLeft();
        return other;
    }

    /**
     * Returns the right value if present, otherwise returns the specified default
     * value.
     *
     * @param other The default value to return if this instance contains a left
     *              value.
     * @return The right value if present, otherwise the specified default value.
     */
    public U getRightOrElse(U other) {
        if (isRight())
            return getRight();
        return other;
    }

    /**
     * Returns the left value if present, otherwise returns the result of invoking
     * the specified Supplier.
     *
     * @param otherSupplier The Supplier to invoke if this instance contains a right
     *                      value.
     * @return The left value if present, otherwise the result of invoking the
     *         specified Supplier.
     */
    public T getLeftOrElseGet(Supplier<T> otherSupplier) {
        if (isLeft())
            return getLeft();
        return otherSupplier.get();
    }

    /**
     * Returns the right value if present, otherwise returns the result of invoking
     * the specified Supplier.
     *
     * @param otherSupplier The Supplier to invoke if this instance contains a left
     *                      value.
     * @return The right value if present, otherwise the result of invoking the
     *         specified Supplier.
     */
    public U getRightOrElseGet(Supplier<U> otherSupplier) {
        if (isRight())
            return getRight();
        return otherSupplier.get();
    }

    /**
     * Returns the left value if present, otherwise throws an exception produced by
     * the specified Supplier.
     *
     * @param exceptionSupplier The Supplier producing the exception to be thrown.
     * @param <X>               Type of the exception to be thrown.
     * @return The left value if present.
     * @throws X If this instance contains a right value.
     */
    public <X extends Exception> T getLeftOrElseThrow(Supplier<X> exceptionSupplier) throws X {
        if (isLeft())
            return getLeft();
        throw exceptionSupplier.get();
    }

    /**
     * Returns the right value if present, otherwise throws an exception produced by
     * the specified Supplier.
     *
     * @param exceptionSupplier The Supplier producing the exception to be thrown.
     * @param <X>               Type of the exception to be thrown.
     * @return The right value if present.
     * @throws X If this instance contains a left value.
     */
    public <X extends Exception> U getRightOrElseThrow(Supplier<X> exceptionSupplier) throws X {
        if (isRight())
            return getRight();
        throw exceptionSupplier.get();
    }

    /**
     * Returns the hash code of the value contained in this {@code Union} instance.
     *
     * @return The hash code of the value.
     */
    @Override
    public int hashCode() { return value.hashCode(); }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj The reference object with which to compare.
     * @return {@code true} if this object is the same as the {@code obj} argument;
     *         {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Union<?, ?> other && Objects.equals(value, other.value))
            return true;
        return Objects.equals(value, obj);
    }
}
