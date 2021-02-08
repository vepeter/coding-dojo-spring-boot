
## Tasks to complete
- fix the Docker image generation
- optimize RestTemplate used to load data from OpenWeatherMap
- clean up *OpenWeatherMap* model classes (see the `com.assignment.spring.application.dataloader.openweathermap.model` package)
- add javadocs
- convert `OpenWeatherMapIntegrationTest` to use [WireMock](http://wiremock.org/) instead of connecting directly to the OpenWeatherMap API from the test
- generate [OpenAPI](http://spec.openapis.org/oas/v3.0.3) documentation during the build
- add Kubernetes manifests
- migrate to Spring WebFlux and R2DBC
- migrate to the GraalVM JVM (very often it gives better performance)
- add native image generation using GraalVM

## Notes
I assumed that DB schema and REST interface could not be changed: it is not specified that those interfaces could be changed so potentially they can be used by other systems (despite it is a very bad practice for sharing DB).

The project was tested with Postgres 10.5 as the latest minor version of the version 10, Postgres 10 is the minimal major version required by the provided schema (see `DEFAULT AS IDENTITY`)

The project was migrated to the *2.3* version of *Spring Boot* and to a newer version of `org.postgresql:postgresql`.

Added additional dependencies (`lombok`, `spring-cloud-starter-netflix-hystrix` etc) as well as various test libraries.


Spring Boot Coding Dojo
---

Welcome to the Spring Boot Coding Dojo!

### Introduction

This is a simple application that requests its data from [OpenWeather](https://openweathermap.org/) and stores the result in a database. The current implementation has quite a few problems making it a non-production ready product.

### The task

As the new engineer leading this project, your first task is to make it production-grade, feel free to refactor any piece
necessary to achieve the goal.

### How to deliver the code

Please send an email containing your solution with a link to a public repository.

>**DO NOT create a Pull Request with your solution** 

### Footnote
It's possible to generate the API key going to the [OpenWeather Sign up](https://openweathermap.org/appid) page.
