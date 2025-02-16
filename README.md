# Returns: Java Library for Result Handling

![CI](https://github.com/enr/returns/workflows/CI/badge.svg)

[![](https://jitpack.io/v/enr/returns.svg)](https://jitpack.io/#enr/returns)


## Overview

`returns` is a Java library that provides result types to indicate whether an operation was successful, failed, or skipped. 
This allows for clear and structured error handling without relying on exceptions for control flow.

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

`returns` provides a `Result<T>` type to represent different outcomes:

```java
Result<String> failure = Result.failure(/*Throwable*/ cause, "An error occurred");
Result<String> skip = Result.skip("Operation skipped");
Result<String> success = Result.success("The actual result");
```

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

## Development

Build:

```
mvn install
```

Full check (test and formatting):

```
mvn -Pci
```

Repair formatting:

```
mvn -Pfmt
```

Fast build (skip any check and file generation):

```
mvn -Pfast
```
