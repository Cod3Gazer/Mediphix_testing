package com.Cod3Gazer.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MediphixAppTest {

    @SuppressWarnings("rawtypes")
    private AppiumDriver driver;

    @SuppressWarnings({ "rawtypes", "deprecation" })
    @BeforeEach
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("deviceName", "Android Device");
        caps.setCapability("udid", "R5CWB09J6CP"); // Use actual UDID of your device, if needed
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", "14");
        caps.setCapability("appPackage", "com.example.mediphix_app");
        caps.setCapability("appActivity", ".DisclaimerActivity");
        caps.setCapability("noReset", "true");

        // Make sure Appium Server is running
        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), caps);

        try {
            Runtime.getRuntime().exec("adb shell screenrecord /sdcard/testRecording.mp4 --time-limit 30");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DisclaimerAcceptance() {
        // Locate the disclaimer checkbox
        MobileElement checkboxAgree = (MobileElement) driver.findElementById("com.example.mediphix_app:id/check_agree");
        // Click on the checkbox to select it
        checkboxAgree.click();

        // Locate the 'Next' button
        MobileElement nextButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/NextBtn");
        // Click on the 'Next' button
        nextButton.click();
    }

    @Test
    public void testLogin() {
        DisclaimerAcceptance();

        MobileElement usernameField = (MobileElement) driver.findElementById("com.example.mediphix_app:id/regNumber");
        MobileElement passwordField = (MobileElement) driver.findElementById("com.example.mediphix_app:id/password");
        MobileElement loginButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/loginBtn");

        usernameField.sendKeys("12345");
        passwordField.sendKeys("12345678");
        loginButton.click();

        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("com.example.mediphix_app:id/aboutMediphixBtn")));

        MobileElement aboutMediphixBtn = (MobileElement) driver
                .findElementById("com.example.mediphix_app:id/aboutMediphixBtn");
        Assertions.assertTrue(aboutMediphixBtn.isDisplayed(), "Login message does not match expected value.");
    }

    @SuppressWarnings("deprecation")
    @AfterEach
    public void tearDown() {
        try {
            Runtime.getRuntime().exec("adb pull /sdcard/testRecording.mp4 ./testRecording.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (driver != null) {
            driver.quit();
        }
    }
}
