# JavaScript Selenium tests

This is the JavaScript version of the six Java functional tests in the parent folder. It uses Selenium WebDriver, Chrome, and Node.js's built-in test runner.

Requirements: Node.js 22 or newer, npm, Google Chrome, and internet access for the website and ChromeDriver if Selenium Manager needs to download it.

Run from this `javascript` folder:

```powershell
npm install
npm test
```

The test runner prints a result for each test case and a final pass/fail summary. Selenium Manager handles ChromeDriver when a compatible driver is not already installed.

If Windows blocks Selenium Manager, set `CHROMEDRIVER_PATH` to an existing ChromeDriver executable compatible with your Chrome version before running `npm test`. For example:

```powershell
$env:CHROMEDRIVER_PATH = 'C:\path\to\chromedriver.exe'
npm test
```
