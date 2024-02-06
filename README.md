# returns

![CI](https://github.com/enr/returns/workflows/CI/badge.svg)

[![](https://jitpack.io/v/enr/returns.svg)](https://jitpack.io/#enr/returns)

Types to return in your methods 

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
