# McKoon Spider

A simple web crawler that visits pages within a domain and adds them to Elasticsearch for searching.

## Getting Started

### Environment

* Install JDK 8+.
* Install Elasticsearch (tested with version 6.1.2 on Ubuntu 17.10) for indexing pages.

### Setting Up IDE

If using IntelliJ, "Import Project", and select the build.gradle file.
This will build and set up IntelliJ for the project.

## Building the Application

Building this application will create an executable jar file that contains all of the
application's dependencies.

Build command from the root of spider repository:
```
./gradlew shadowJar
```

## Running the Console Application

From within the project directory, run the jar from the command-line.

```
java -jar ./build/libs/spider.jar
```

## Front-end

The [spider-client](https://github.com/mckoon/spider-client) is a simple front-end created for querying
the populated Elasticsearch cluster.
