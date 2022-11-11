package com.lambdatest;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class LambdaAssignmentTest {

	private RemoteWebDriver driver;
	private String Status = "failed";
	SoftAssert sf = new SoftAssert();
	public String URL = "https://www.lambdatest.com/selenium-playground";

	@BeforeMethod
	public void setup(Method m, ITestContext ctx) throws MalformedURLException {
		String username = System.getenv("LT_USERNAME") == null ? "Your LT Username" : System.getenv("LT_USERNAME");
		String authkey = System.getenv("LT_ACCESS_KEY") == null ? "Your LT AccessKey" : System.getenv("LT_ACCESS_KEY");
		;
		String hub = "@hub.lambdatest.com/wd/hub";

		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("platform", "MacOS Catalina");
		caps.setCapability("browserName", "Chrome");
		caps.setCapability("version", "latest");
		caps.setCapability("build", "TestNG With Java");
		caps.setCapability("name", m.getName() + this.getClass().getName());
		caps.setCapability("plugin", "git-testng");

		String[] Tags = new String[] { "Feature", "Tag", "Moderate" };
		caps.setCapability("tags", Tags);

		driver = new RemoteWebDriver(new URL("https://" + username + ":" + authkey + hub), caps);
	}

	@Test
	public void testScenario1() {
		System.out.println("Opening LambdaTest’s Selenium playground");
		driver.get(URL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		System.out.println("Clicking on simple form demo and clicking on it");
		driver.findElement(By.xpath("//a[normalize-space()='Simple Form Demo']")).click();
		System.out.println("validating url navigation");
		sf.assertTrue(driver.getCurrentUrl().contains("simple-form-demo"), "url not matched");
		String SampleMsg = "Welcome to LambdaTest";
		System.out.println("Finding the input box and passing the values");
		driver.findElement(By.xpath("//p[text()='Enter Message']/following-sibling::input")).sendKeys(SampleMsg);
		System.out.println("Clicking on the input box click");
		driver.findElement(By.id("showInput")).click();

		System.out.println("Capturing the displayed message");
		String DisplayedMsg = driver.findElement(By.xpath("//div[@id='user-message']/p[@id='message']")).getText();
		sf.assertEquals(DisplayedMsg, SampleMsg);
		sf.assertAll();
		Status = "passed";

	}

	@Test
	public void testScenario3() throws InterruptedException {

		System.out.println("Opening LambdaTest’s Selenium playground");
		driver.get(URL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		System.out.println("Clicking on Input Form Submit");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		driver.findElement(By.linkText("Input Form Submit")).click();
		js.executeScript("arguments[0].scrollIntoView();",
				driver.findElement(By.xpath("//button[normalize-space()='Submit']")));
		js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("inputCity")));
		driver.findElement(By.xpath("//button[normalize-space()='Submit']")).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		Assert.assertEquals(wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='name']")))
				.getAttribute("validationMessage"), "Please fill out this field.");

		// Fill the form
		System.out.println("Filling the form and confirming success message");
		int randNumber = (int) (Math.random() * 100);
		driver.findElement(By.cssSelector("#name")).sendKeys("Niks");
		driver.findElement(By.cssSelector("#inputEmail4")).sendKeys("niks" + randNumber + "@gmail.com");
		driver.findElement(By.cssSelector("#inputPassword4")).sendKeys("test1234");
		driver.findElement(By.cssSelector("#company")).sendKeys("testComp");
		driver.findElement(By.cssSelector("#websitename")).sendKeys("www.google.com");
		Select select = new Select(driver.findElement(By.cssSelector("select[name='country']")));
		select.selectByVisibleText("United States");
		driver.findElement(By.cssSelector("#inputCity")).sendKeys("jaipur");
		driver.findElement(By.cssSelector("#inputAddress1")).sendKeys("test");
		driver.findElement(By.cssSelector("#inputAddress2")).sendKeys("test2");
		driver.findElement(By.cssSelector("#inputState")).sendKeys("testState");
		driver.findElement(By.cssSelector("#inputZip")).sendKeys("1234");
		driver.findElement(By.xpath("//button[normalize-space()='Submit']")).click();

		Assert.assertEquals(driver.findElement(By.cssSelector(".success-msg.hidden")).getText(),
				"Thanks for contacting us, we will get back to you shortly.");
		Status = "passed";

	}

	@Test
	public void testScenario2() throws InterruptedException {

		System.out.println("Opening LambdaTest’s Selenium playground");
		driver.get(URL);
		System.out.println("Clicking on Drag & Drop Sliders and clicking on it");
		driver.findElement(By.linkText("Drag & Drop Sliders")).click();

		driver.findElement(By.xpath("//input[@value='15']")).click();

		Actions act = new Actions(driver);
//		Point point = driver.findElement(By.xpath("//input[@value='15']")).getLocation();

		System.out.println("Validating 95 in scroller");
		act.moveToElement(driver.findElement(By.xpath("//input[@value='15']")), 119, 5).click();
		act.build().perform();
		String OutPutRange = driver.findElement(By.xpath("//input[@value='15']/following-sibling::output")).getText();
		Assert.assertEquals(OutPutRange, "95");
		Thread.sleep(1000);

		Status = "passed";

	}

	@AfterMethod
	public void tearDown() {
		driver.executeScript("lambda-status=" + Status);
		driver.quit();
	}

}
