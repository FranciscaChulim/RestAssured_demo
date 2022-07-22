
#API Automation testing with Rest assured

## Tech Stack

- Java
- Rest assured
- Maven
- TestNG

## Project Structure

```bash
|---
|--- src
|    |--- main
|    |--- test
|         |--- java
|              |--- api_demo     # Java test files
|              |--- dataentities # De-serialization files.
|--- testng.xml                  # Suite to run all test by clasess
|--- pom.xml                     # pom file contains dependencies 
|---...
```

## Running Tests 

To run all the tests from the suite on testng.xml

```bash
  mvn test
```

To run a specific class 
```bash
  mvn -Dtest=TaskScenarios test
```