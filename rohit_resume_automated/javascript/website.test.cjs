const test = require('node:test');
const assert = require('node:assert/strict');
const { Builder, By, until, error } = require('selenium-webdriver');
const chrome = require('selenium-webdriver/chrome');

const URL = 'https://rohit-jagdale.vercel.app/';
const TIMEOUT_MS = 15_000;

async function withWebsite(check) {
  const builder = new Builder().forBrowser('chrome');
  if (process.env.CHROMEDRIVER_PATH) {
    builder.setChromeService(new chrome.ServiceBuilder(process.env.CHROMEDRIVER_PATH));
  }
  const driver = await builder.build();
  try {
    await driver.manage().setTimeouts({ pageLoad: 30_000 });
    await driver.get(URL);
    await driver.wait(until.elementLocated(By.id('hero-title')), TIMEOUT_MS);
    await check(driver);
  } finally {
    await driver.quit();
  }
}

async function scrollIntoView(driver, locator) {
  const element = await driver.findElement(locator);
  await driver.executeScript(
    "arguments[0].scrollIntoView({block: 'center', behavior: 'auto'});",
    element
  );
  return element;
}

async function visibleText(driver, locator) {
  const element = await scrollIntoView(driver, locator);
  return driver.wait(async () => {
    const text = (await element.getText()).replace(/\s+/g, ' ').trim();
    return text || false;
  }, TIMEOUT_MS);
}

async function clickWhenReady(driver, locator) {
  const element = await scrollIntoView(driver, locator);
  await driver.wait(until.elementIsVisible(element), TIMEOUT_MS);
  await driver.wait(until.elementIsEnabled(element), TIMEOUT_MS);
  await driver.wait(async () => {
    try {
      await element.click();
      return true;
    } catch (caught) {
      if (caught instanceof error.ElementClickInterceptedError) return false;
      throw caught;
    }
  }, TIMEOUT_MS);
}

test('home page shows the expected introduction', { timeout: 60_000 }, async () => {
  await withWebsite(async (driver) => {
    assert.equal(await driver.getTitle(), 'Rohit Jagdale — QA Automation & AI Quality Engineer');
    assert.equal(await visibleText(driver, By.id('hero-title')), 'Confidence in every release.');
    assert.match(await visibleText(driver, By.id('home')),
      /I’m Rohit Jagdale\. I test the workflows, integrations, and AI responses that people depend on\./);
  });
});

test('Work navigation opens the selected work section', { timeout: 60_000 }, async () => {
  await withWebsite(async (driver) => {
    await clickWhenReady(driver, By.css("a[href='#work']"));
    await driver.wait(async () => (await driver.getCurrentUrl()).includes('#work'), TIMEOUT_MS);
    assert.equal(await visibleText(driver, By.id('work-title')),
      'Quality goes deeper than a passing test.');
  });
});

test('AI response tab shows its explanation', { timeout: 60_000 }, async () => {
  await withWebsite(async (driver) => {
    await clickWhenReady(driver, By.id('tab-responses'));
    const tab = await driver.findElement(By.id('tab-responses'));
    await driver.wait(async () => (await tab.getAttribute('aria-selected')) === 'true', TIMEOUT_MS);
    const panelText = await visibleText(driver, By.id('panel-responses'));
    assert.match(panelText, /A response should make sense, too\./);
    assert.match(panelText, /Check that explanations address the observed sales movement/);
  });
});

test('API work details expand and show expected text', { timeout: 60_000 }, async () => {
  await withWebsite(async (driver) => {
    const details = await driver.findElement(By.css('#work details'));
    assert.equal(await details.getAttribute('open'), null);
    await clickWhenReady(driver, By.css('#work details summary'));
    await driver.wait(async () => (await details.getAttribute('open')) !== null, TIMEOUT_MS);
    assert.match(await visibleText(driver, By.css('#work details')),
      /Automated API regression and sanity tests with Karate/);
  });
});

test('Automation filter shows only automation categories', { timeout: 60_000 }, async () => {
  await withWebsite(async (driver) => {
    const filter = await driver.findElement(By.css("button[data-filter='automation']"));
    await clickWhenReady(driver, By.css("button[data-filter='automation']"));
    await driver.wait(async () => (await filter.getAttribute('aria-pressed')) === 'true', TIMEOUT_MS);
    const rows = await driver.findElements(By.css('#expertise .skill-row:not([hidden])'));
    assert.equal(rows.length, 2);
    assert.equal(await rows[0].findElement(By.tagName('h3')).getText(), 'Web & mobile automation');
    assert.equal(await rows[1].findElement(By.tagName('h3')).getText(), 'API testing');
    assert.equal((await driver.findElement(By.id('filter-status'))
      .getAttribute('textContent')).trim(), '2 expertise categories shown.');
  });
});

test('Contact navigation shows text and email link', { timeout: 60_000 }, async () => {
  await withWebsite(async (driver) => {
    await clickWhenReady(driver, By.css("a[href='#contact']"));
    await driver.wait(async () => (await driver.getCurrentUrl()).includes('#contact'), TIMEOUT_MS);
    assert.equal(await visibleText(driver, By.id('contact-title')),
      'Building something that needs to work?');
    const email = await driver.findElement(
      By.css("#contact a[href='mailto:rohitjagdale24@gmail.com']")
    );
    assert.match(await email.getText(), /rohitjagdale24@gmail\.com/);
    assert.match(await email.getAttribute('href'), /^mailto:rohitjagdale24@gmail\.com/);
  });
});
