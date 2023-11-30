# Union Class

A container object which will contain a value of one of two possible types. Instances of this class are either left or right. A left instance contains a value of type `T`, and a right instance contains a value of type `U`.

## Usage

### Creating Instances

```java
// Creating a Union instance with a left value
Union<String, Integer> leftUnion = Union.ofLeft("Left value");

// Creating a Union instance with a right value
Union<String, Integer> rightUnion = Union.ofRight(42);
```

### Checking Type and Retrieving Values

```java
// Checking if the Union instance is left or right
if (leftUnion.isLeft()) {
    // Handling left value
    String leftValue = leftUnion.getLeft();
    System.out.println("Left value: " + leftValue);
} else {
    // Handling right value
    Integer rightValue = rightUnion.getRight();
    System.out.println("Right value: " + rightValue);
}
// Outputs "Left value: Left value" to System.out
```

### Mapping and Transformation

```java
// Applying a mapping function to the left value
Union<Integer, Integer> mappedUnion = leftUnion.mapLeft(Integer::length);

// Applying a mapping function to the right value
Union<String, String> mappedRightUnion = rightUnion.mapRight(String::valueOf);
```

## License

This project is licensed under the MIT License - see the [LICENSE](/LICENSE) file for details.
