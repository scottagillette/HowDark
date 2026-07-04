# Developers Guide

---

## Prerequisites
Install and set up the following tools:
- [sdkman](https://sdkman.io/install/)

## Initial setup
Run the following command to set up java

```shell
sdk env install
```

## Project Commands

To run tests:
```shell
./gradlew check
```

To run project:
```shell
./gradlew gauntlet
```

To check dependencies:
```shell
./gradlew dependencies > build/dep.txt
```
