# QA Automation Framework

UI and API test automation in Java. It tests a public demo web shop with Selenium and a public booking REST API with REST Assured, runs in parallel, and produces Allure reports. The suite runs locally, in Docker and in CI.

[![Tests](https://github.com/TomekWojciechowski/qa-automation-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/TomekWojciechowski/qa-automation-framework/actions/workflows/ci.yml)

## Tech stack

| Area | Tools |
|---|---|
| Language / build | Java 17, Maven |
| UI tests | Selenium 4, TestNG, Page Object Model |
| API tests | REST Assured, Jackson |
| Assertions | AssertJ |
| Reporting | Allure (steps, severity, screenshot on failure, HTTP request/response logs) |
| Execution | Parallel TestNG, Docker + Selenium standalone Chrome |
| CI/CD | GitHub Actions, GitLab CI |

## Systems under test

- **UI:** [saucedemo.com](https://www.saucedemo.com), a demo shop built for test practice. Its login credentials are published on the site.
- **API:** [restful-booker](https://restful-booker.herokuapp.com), a public API for practice with create, read, update and delete on bookings.

## Test coverage

- **Login:** valid user, locked-out user, four invalid-credential variants (data-driven)
- **Product list:** number of products, sorting by price and by name
- **Cart:** badge count, removing products, cart contents
- **Checkout:** complete order end to end, validation of required customer data
- **Booking API:** create and read back, authenticated update, update with an invalid token (403), delete and verify 404

## Project structure

```
src/test/java/dev/tomasz/qa
├── config/    Config: system property > environment variable > config.properties
├── driver/    DriverFactory (local or remote browser), DriverManager (one driver per thread)
├── pages/     Page objects: BasePage, LoginPage, InventoryPage, CartPage, CheckoutPage
├── api/       BookingClient and the Booking model
└── tests/
    ├── ui/    BaseUiTest and UI test classes
    └── api/   API test classes
src/test/resources
├── config.properties   defaults (URLs, timeouts, demo credentials)
└── testng.xml          suite, runs methods in parallel
```

### Design decisions

- **Page objects expose behaviour, not locators.** Tests read as user flows and locator changes stay in one place.
- **One WebDriver per thread.** A single test class instance is shared by parallel methods, so the driver lives in a `ThreadLocal` and is never stored in a test field.
- **Explicit waits only.** `BasePage` wraps all interactions in `WebDriverWait`; there are no `Thread.sleep` calls.
- **Configuration without code changes.** Browser, headless mode and grid URL are switched with `-D` properties or environment variables.

## Run it

Requirements: JDK 17 and Maven. Chrome is used by default; Selenium Manager downloads the matching driver.

```bash
mvn test
```

Useful options:

```bash
mvn test -Dheadless=false            # watch the browser
mvn test -Dbrowser=firefox           # Firefox instead of Chrome
```

### Allure report

```bash
allure serve target/allure-results
```

### Docker

Runs the suite in a container against a Selenium standalone Chrome container:

```bash
docker compose up --build --abort-on-container-exit
```

### CI

- `.github/workflows/ci.yml` runs the suite on every push and pull request and stores the Allure report as an artifact.
- `.gitlab-ci.yml` runs the suite in the Maven image with a Selenium Chrome service container and keeps the Allure results and JUnit report.

## Author

Tomasz Wojciechowski, Test Automation Engineer (Java, Selenium, CI/CD)
