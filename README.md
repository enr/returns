# Returns: Java Library for Result Handling

[![CI](https://github.com/enr/returns/actions/workflows/ci.yml/badge.svg)](https://github.com/enr/returns/actions/workflows/ci.yml)

[![](https://jitpack.io/v/enr/returns.svg)](https://jitpack.io/#enr/returns)


## Overview

`returns` is a Java library that provides result types to indicate whether an operation was successful, failed, or skipped.

This allows for clear and structured error handling without relying on exceptions for control flow.

Requires Java 17 or later. The library is a Java module named `returns`.

## Usage

To get this project into your build:

Add the JitPack repository to your build file

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Add the dependency

```xml
<dependency>
  <groupId>com.github.enr</groupId>
  <artifactId>returns</artifactId>
  <version>${returns.version}</version>
</dependency>
```

`returns.version` is a release tag (for example `v0.2.1`, see the JitPack badge above for the latest one).

If you use the module path, add `requires returns;` to your `module-info.java`.

### Results

`returns` provides a `Result<T>` type to represent different outcomes:

```java
Result<String> failure = Result.failure(/*Throwable*/ cause, "An error occurred");
Result<String> skip = Result.skip("Operation skipped");
Result<String> success = Result.success("The actual result");
Result<Nothing> done = Result.success(); // success without a value
```

A successful result always holds a non-null value: `Result.success(null)` throws `NullPointerException`.

Each `Result<T>` instance provides methods to check its status.

By using `Result<T>`, developers can clearly communicate the outcome of operations and handle them effectively:

```java
if (result.isSuccessful()) {
  // get the actual result
  System.out.println("Success: " + result.unwrap());
} else if (result.isFailed()) {
  System.err.println("Failure: " + result.explanation());
} else if (result.isSkipped()) {
  System.out.println("Skipped: " + result.explanation());
}
```

Other ways to get the value:

```java
String value = result.orElse("fallback");     // fallback if not successful
String value = result.orFail("no value");     // IllegalStateException if not successful
Optional<String> value = result.toOptional(); // empty if not successful
result.ifSuccess(v -> System.out.println(v)); // called only if successful
```

`unwrap()` on a non-successful result throws `NoSuchElementException`.

### Failures

There are several factory methods for failures:

```java
Result.failure("An error occurred");
Result.failure(cause);                                  // the message is taken from the exception
Result.failure(cause, "An error occurred");
Result.failure("Error reading %s", path);               // formatted message
Result.failure("Error reading %s: %s", path, cause);    // a trailing Throwable is also used as cause
Result.failure(cause, "Error reading %s", path);
```

The cause, if any, is available as `Optional<Throwable>` via `result.cause()`.

Failures can be classified using an `ErrorKind`, usually an enum:

```java
enum FileError implements ErrorKind {
  NOT_FOUND, PERMISSION_DENIED
}

Result<String> result = Result.failure(FileError.NOT_FOUND, "File not found");

result.isFailureOfKind(FileError.class); // true
FileError kind = result.kind();          // FileError.NOT_FOUND
```

Failures created without a kind are "unclassified" (`result.isUnclassified()` is `true`).

To propagate an unsuccessful result with a different value type use `Result.copyUnsuccessful`, which keeps
explanation, cause and error kind:

```java
Result<Config> config = loadConfig();
if (config.isUnsuccessful()) {
  return Result.copyUnsuccessful(config); // Result<Server>
}
```

`Success`, `Failure` and `Skip` implement `equals` and `hashCode`.

### Composite results

`CompositeResult<T>` groups many `Result<T>`; it is itself a `Result<Collection<T>>` whose status is decided by policies:

```java
CompositeResult<String> results = CompositeResult.empty(CompositeResultPolicies.all());
results.add(Result.success("a"));
results.add(Result.failure("oops"));

results.isPartial();   // true: some, but not all, results are successful
results.unwrap();      // ["a"]: the values of the successful results
results.explanation(); // the failures messages
```

Built-in policies:

- `CompositeResultPolicies.all()`: successful, failed or skipped only if *all* the results are respectively successful, failed or skipped
- `CompositeResultPolicies.atLeastOne()`: successful, failed or skipped if *at least one* result is respectively successful, failed or skipped

Custom combinations can be created with `new CompositeResultPolicies<>(success, skip, failure)`,
using the policies in `CompositeResultSuccessPolicy` (`all`, `atLeastOne`, `noError`), `CompositeResultSkipPolicy`
and `CompositeResultFailurePolicy`, or lambdas.

The statuses are mutually exclusive: failure takes precedence over success and success takes precedence over skip.
For example, with `atLeastOne()` a composite having a success and a failure is only failed.

An empty composite is neither successful, nor failed, nor skipped.

### Tuples

The `com.github.enr.returns.tuple` package contains simple immutable value types:

```java
Pair<String, Integer> pair = Pair.of("a", 1);
Triplet<String, Integer, Boolean> triplet = Triplet.of("a", 1, true);

Either<String, Integer> either = Either.right(1);
if (either.isRight()) {
  Integer value = either.right();
}
```

All of them implement `equals` and `hashCode`.

## Development

The build uses the Maven wrapper and needs the JitPack repository for some build plugins configuration,
declared in `.mvn/settings.xml`.

Build:

```sh
./mvnw -s .mvn/settings.xml install
```

Full check (test, coverage, mutation testing and formatting), as run by CI:

```sh
.sdlc/ci
```

Repair formatting:

```sh
.sdlc/fmt
```

Fast build (skip any check and file generation):

```sh
./mvnw -s .mvn/settings.xml -Pfast
```

Release:

```sh
.sdlc/release <release-version> <next-snapshot-version>
# e.g. .sdlc/release 0.3.0 0.4.0
```
