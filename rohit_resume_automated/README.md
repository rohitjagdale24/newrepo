# Java Selenium example

This project opens `https://rohit-jagdale.vercel.app/` in Chrome. `WebsiteCheck` is a simple runnable example. `WebsiteFunctionalTest` contains six functional tests that validate the site's visible text and interactions.

## Requirements

- JDK 17 or newer
- Maven
- Google Chrome
- Internet access for the website and, if needed, Selenium Manager's ChromeDriver download

Run from this folder:

```powershell
mvn test
```

To run the original one-page example:

```powershell
mvn compile exec:java
```

Selenium Manager handles ChromeDriver automatically when a compatible driver is not already installed.

## JavaScript version

The same six functional checks are available in [`javascript/`](javascript/README.md). Install Node.js 22 or newer, then follow that folder's README to run them with `npm test`.
