package com.Cod3Gazer.app;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


@TestMethodOrder(OrderAnnotation.class)
public class MediphixAppTest_IndividualRun {

    private static AppiumDriver<MobileElement> driver;
    private static Process screenRecordProcess;

    @SuppressWarnings({ "deprecation" })
    @BeforeAll
    public static void startRecording() {
        try {
            // Start screen recording
            screenRecordProcess = Runtime.getRuntime().exec("adb shell screenrecord /sdcard/testRecording.mp4 --time-limit 120");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
    @BeforeEach
    public void setUp() throws MalformedURLException {
        //Setting up the capabilities for starting the appium server
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

        DisclaimerAcceptance();
    }

    public static void DisclaimerAcceptance() {
        // Locate the disclaimer checkbox
        MobileElement checkboxAgree = (MobileElement) driver.findElementById("com.example.mediphix_app:id/check_agree");
        // Click on the checkbox to select it
        checkboxAgree.click();

        // Locate the 'Next' button
        MobileElement nextButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/NextBtn");
        // Click on the 'Next' button
        nextButton.click();
    }

    public void Login(String username, String password, int successfulLoginCheck) {

        MobileElement usernameField = (MobileElement) driver.findElementById("com.example.mediphix_app:id/regNumber");
        MobileElement passwordField = (MobileElement) driver.findElementById("com.example.mediphix_app:id/password");
        MobileElement loginButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/loginBtn");

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
        if(successfulLoginCheck == 1) {
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("com.example.mediphix_app:id/aboutMediphixBtn")));
        }
    }

    /*
     * Test desc :- To test login with empty password field on login screen.
     * Project requirement - Password is a mandatory field on login screen.
     * Test Expected Output :- Android Toast message as "Please enter the Username and Password",
     */
    @Test
    @Order(1)
    public void testLogin_Empty_Passwod() {
        Login("12user", "", 0);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Please enter the Username and Password", toastMessageElement.getText(), "Alert message text is not as expected.");
    }

    /*
     * Test desc :- To test login with empty username field on login screen.
     * Project requirement - Username is a mandatory field on login screen.
     * Test Expected Output :- Android Toast message as "Please enter the Username and Password",
     */
    @Test
    @Order(2)
    public void testLogin_Empty_Username() {
        Login("", "12345678", 0);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Please enter the Username and Password", toastMessageElement.getText(), "Alert message text is not as expected.");
    }

    /*
     * Test desc :- To test login with empty username and password fields on login screen.
     * Project requirement - Username and Password are mandatory fields on login screen.
     * Test Expected Output :- Android Toast message as "Please enter the Username and Password",
     */
    @Test
    @Order(3)
    public void testLogin_Empty_Username_and_Password() {
        Login("", "", 0);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Please enter the Username and Password", toastMessageElement.getText(), "Alert message text is not as expected.");
    }

    /*
     * Test desc :- To test login with incorrect username on login screen.
     * Project requirement - Username should exist in database for successful login.
     * Test Expected Output :- Android Toast message as "User Doesn't Exist",
     */
    @Test
    @Order(4)
    public void testLogin_Incorrect_Username() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Login("12user", "12345678", 0);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("User Doesn't Exist", toastMessageElement.getText(), "Alert message text is not as expected.");
    }

    /*
     * Test desc :- To test login with incorrect password on login screen.
     * Project requirement - Password should match with stored password in database for respective user for successful login.
     * Test Expected Output :- Android Toast message as "Password Is Incorrect",
     */
    @Test
    @Order(5)
    public void testLogin_Incorrect_Password() {
        Login("12345", "12345", 0);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Password Is Incorrect", toastMessageElement.getText(), "Alert message text is not as expected.");
    }

    /*
     * Test desc :- To test registration with empty first name field.
     * Project requirement - First Name is a required field for nurse registration.
     * Test Expected Output :- Android Toast message as "Nurse First name is required."
     */
    @Test
    @Order(6)
    public void testRegistration_With_Empty_FirstName() {
        MobileElement registrationFromLoginBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");
        registrationFromLoginBtn.click();
        MobileElement firstNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/firstName");
        MobileElement lastNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/lastName");
        MobileElement registrationNumberTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/regNumber");
        MobileElement passwordTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/password");
        MobileElement RegistrationBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");

        firstNameTxtBox.sendKeys("");
        lastNameTxtBox.sendKeys("Sharma");
        registrationNumberTxtBox.sendKeys("6789");
        passwordTxtBox.sendKeys("12345678");
        RegistrationBtn.click();
        RegistrationBtn.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Nurse First name is required.", toastMessageElement.getText(), "Alert message text is not as expected.");
    }

    /*
     * Test desc :- To test registration with empty Last name field.
     * Project requirement - Last Name is not a required field for nurse registration.
     * Test Expected Output :- Android Toast message as "Nurse successfully saved".
     */
    @Test
    @Order(7)
    public void testRegistration_With_Empty_LastName() {
        MobileElement registrationFromLoginBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");
        registrationFromLoginBtn.click();
        MobileElement firstNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/firstName");
        MobileElement lastNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/lastName");
        MobileElement registrationNumberTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/regNumber");
        MobileElement passwordTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/password");
        MobileElement RegistrationBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");

        firstNameTxtBox.sendKeys("Sumit");
        lastNameTxtBox.sendKeys("");
        registrationNumberTxtBox.sendKeys("6790");
        passwordTxtBox.sendKeys("12345678");
        RegistrationBtn.click();
        RegistrationBtn.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Nurse successfully saved", toastMessageElement.getText(), "Alert message text is not as expected.");
    }

    /*
     * Test desc :- To test registration with empty First name and Last name field.
     * Project requirement - First Name is a required field for nurse registration, whereas Last name is not a required field.
     * Test Expected Output :- Android Toast message as "Nurse First name is required."
     */
    @Test
    @Order(8)
    public void testRegistration_With_Empty_FirstName_And_LastName() {
        MobileElement registrationFromLoginBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");
        registrationFromLoginBtn.click();
        MobileElement firstNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/firstName");
        MobileElement lastNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/lastName");
        MobileElement registrationNumberTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/regNumber");
        MobileElement passwordTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/password");
        MobileElement RegistrationBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");

        firstNameTxtBox.sendKeys("");
        lastNameTxtBox.sendKeys("");
        registrationNumberTxtBox.sendKeys("6791");
        passwordTxtBox.sendKeys("12345678");
        RegistrationBtn.click();
        RegistrationBtn.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Nurse First name is required.", toastMessageElement.getText(), "Alert message text is not as expected.");
    }

    /*
     * Test desc :- To test registration with empty registration number field.
     * Project requirement - Registration is a required field for nurse registration.
     * Test Expected Output :- Android Toast message as "Nurse registration number is required."
     */
    @Test
    @Order(9)
    public void testRegistration_With_Empty_Registration_Number() {
        MobileElement registrationFromLoginBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");
        registrationFromLoginBtn.click();
        MobileElement firstNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/firstName");
        MobileElement lastNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/lastName");
        MobileElement registrationNumberTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/regNumber");
        MobileElement passwordTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/password");
        MobileElement RegistrationBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");

        firstNameTxtBox.sendKeys("Sumit");
        lastNameTxtBox.sendKeys("Sharma");
        registrationNumberTxtBox.sendKeys("");
        passwordTxtBox.sendKeys("12345678");
        RegistrationBtn.click();
        RegistrationBtn.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Nurse registration number is required.", toastMessageElement.getText(), "Alert message text is not as expected.");
    }

    /*
     * Test desc :- To test registration with user details which is already existing.
     * Project requirement - Application should not allow multiple nurse records with same registration number i.e., registration number should be unique.
     * Test Expected Output :- Android Toast message as "Registration number is already in use",
     */
    @Test
    @Order(10)
    public void testRegistration_With_Registration_Number_Already_Exist() {
        MobileElement registrationFromLoginBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");
        registrationFromLoginBtn.click();
        MobileElement firstNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/firstName");
        MobileElement lastNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/lastName");
        MobileElement registrationNumberTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/regNumber");
        MobileElement passwordTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/password");
        MobileElement RegistrationBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");

        firstNameTxtBox.sendKeys("Sumit");
        lastNameTxtBox.sendKeys("Sharma");
        registrationNumberTxtBox.sendKeys("12345");
        passwordTxtBox.sendKeys("12345678");
        RegistrationBtn.click();
        RegistrationBtn.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Registration number is already in use", toastMessageElement.getText(), "Alert message text is not as expected.");
    }

    /*
     * Test desc :- To test registration with short password less than 8 characters.
     * Project requirement - Password should be greater than equal to 8 and less than equal to 12.
     * Test Expected Output :- Android Toast message as "Password is too short",
     */
    @Test
    @Order(11)
    public void testRegistration_Short_Password() {
        MobileElement registrationFromLoginBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");
        registrationFromLoginBtn.click();
        MobileElement firstNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/firstName");
        MobileElement lastNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/lastName");
        MobileElement registrationNumberTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/regNumber");
        MobileElement passwordTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/password");
        MobileElement RegistrationBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");

        firstNameTxtBox.sendKeys("Sumit");
        lastNameTxtBox.sendKeys("Sharma");
        registrationNumberTxtBox.sendKeys("170896");
        passwordTxtBox.sendKeys("1234567");
        RegistrationBtn.click();
        RegistrationBtn.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Password is too short", toastMessageElement.getText(), "Alert message text is not as expected.");
    }

    /*
     * Test desc :- To test registration with long password greater than 12 characters.
     * Project requirement - Password should be greater than equal to 8 and less than equal to 12.
     * Test Expected Output :- Android Toast message as "Password is too long",
     */
    @Test
    @Order(12)
    public void testRegistration_Long_Password() {
        MobileElement registrationFromLoginBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");
        registrationFromLoginBtn.click();
        MobileElement firstNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/firstName");
        MobileElement lastNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/lastName");
        MobileElement registrationNumberTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/regNumber");
        MobileElement passwordTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/password");
        MobileElement RegistrationBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");

        firstNameTxtBox.sendKeys("Sumit");
        lastNameTxtBox.sendKeys("Sharma");
        registrationNumberTxtBox.sendKeys("17089");
        passwordTxtBox.sendKeys("1234567891023");
        RegistrationBtn.click();
        RegistrationBtn.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Password is too long", toastMessageElement.getText(), "Alert message text is not as expected.");
    }

     /*
     * Test desc :- To test successful registration with all validation passed 
     * Test Expected Output :- Android Toast message "Nurse successfully saved"
     */
    @Test
    @Order(13)
    public void testRegistration_Success() {
        MobileElement registrationFromLoginBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");
        registrationFromLoginBtn.click();
        MobileElement firstNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/firstName");
        MobileElement lastNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/lastName");
        MobileElement registrationNumberTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/regNumber");
        MobileElement passwordTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/password");
        MobileElement RegistrationBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerBtn");

        firstNameTxtBox.sendKeys("Sumit");
        lastNameTxtBox.sendKeys("Sharma");
        registrationNumberTxtBox.sendKeys("1708");
        passwordTxtBox.sendKeys("12345678");
        RegistrationBtn.click();
        RegistrationBtn.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Nurse successfully saved", toastMessageElement.getText(), "Alert message text is not as expected.");
    }

    /*
     * Test desc :- To test login with correct username and password on login screen.
     * Project requirement - Password should match with stored password in database for respective user for successful login.
     * Test Expected Output :- Home page of application will be shown to user, asserting with About Mediphix button is visible on screen. 
     *                          Which is visible only after successful authentication.
     */
    @Test
    @Order(14)
    public void testLogin_Success() {
        //MobileElement LoginFromRegisterBtn = (MobileElement) driver.findElementById("com.example.mediphix_app:id/loginBtn");
        //LoginFromRegisterBtn.click();    
        Login("12345", "12345678", 1);
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.id("com.example.mediphix_app:id/aboutMediphixBtn")));
        MobileElement aboutMediphixBtn = (MobileElement) driver
                .findElementById("com.example.mediphix_app:id/aboutMediphixBtn");
        Assertions.assertTrue(aboutMediphixBtn.isDisplayed(), "Login unsuccessful.");
    }

    /*
     * Test desc :- To test adding drug without selecting storage location from dropdown.
     * Project requirement - Storage Location is a mandatory field.
     * Test Expected Output :- Android Toast message as "Please select storage location.",
     */
    @Test
    @Order(15)
    public void testAddDrug_With_Empty_Storage_Location() {
        Login("12345", "12345678", 1);
        MobileElement addNewDrugButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/newDrug");
        addNewDrugButton.click();

        MobileElement drugNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/DrugName");
        drugNameTxtBox.sendKeys("CELEBREX");
        MobileElement drugNumberTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/idNumber");
        drugNumberTxtBox.sendKeys("45634");
        MobileElement drugExpiryTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/drugExpiry");
        drugExpiryTxtBox.sendKeys("04/05/2024");
        MobileElement drugSaveButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerDrugBtn");
        drugSaveButton.click();
        
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Please select storage location.", toastMessageElement.getText(), "Alert message text is not as expected.");   
    }

    /*
     * Test desc :- To test adding drug with empty drug name field.
     * Project requirement - Drug name is a mandatory field.
     * Test Expected Output :- Android Toast message as "Please enter name of drug.",
     */
    @Test
    @Order(16)
    public void testAddDrug_With_Empty_DrugName() {
        Login("12345", "12345678", 1);
        MobileElement addNewDrugButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/newDrug");
        addNewDrugButton.click();

        MobileElement drugNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/DrugName");
        drugNameTxtBox.sendKeys("");
        MobileElement drugNumberTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/idNumber");
        drugNumberTxtBox.sendKeys("45633");
        MobileElement storageLocationDropDown = (MobileElement) driver.findElementById("com.example.mediphix_app:id/drugStorage");
        storageLocationDropDown.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Ward Office']")));
        MobileElement storageLocationDropDownValue = (MobileElement) driver.findElementByXPath("//*[@text='Ward Office']");
        storageLocationDropDownValue.click();
        MobileElement drugExpiryTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/drugExpiry");
        drugExpiryTxtBox.sendKeys("04/05/2024");
        MobileElement drugSaveButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerDrugBtn");
        drugSaveButton.click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Please enter name of drug.", toastMessageElement.getText(), "Alert message text is not as expected.");    
    }

    /*
     * Test desc :- To test adding drug with empty drug Id field.
     * Project requirement - Drug Id is a mandatory field.
     * Test Expected Output :- Android Toast message as "Please enter id of drug.",
     */
    @Test
    @Order(17)
    public void testAddDrug_With_Empty_DrugId() {
        Login("12345", "12345678", 1);
        //MobileElement homeButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/home");
        //homeButton.click();
        MobileElement addNewDrugButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/newDrug");
        addNewDrugButton.click();

        MobileElement drugNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/DrugName");
        drugNameTxtBox.sendKeys("CELEBREX");
        MobileElement drugNumberTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/idNumber");
        drugNumberTxtBox.sendKeys("");
        MobileElement storageLocationDropDown = (MobileElement) driver.findElementById("com.example.mediphix_app:id/drugStorage");
        storageLocationDropDown.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Ward Office']")));
        MobileElement storageLocationDropDownValue = (MobileElement) driver.findElementByXPath("//*[@text='Ward Office']");
        storageLocationDropDownValue.click();
        MobileElement drugExpiryTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/drugExpiry");
        drugExpiryTxtBox.sendKeys("04/05/2024");
        MobileElement drugSaveButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerDrugBtn");
        drugSaveButton.click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Please enter id of drug.", toastMessageElement.getText(), "Alert message text is not as expected.");    
    }

    /*
     * Test desc :- To test adding drug with empty expiry date field.
     * Project requirement - Expiry Date is a mandatory field.
     * Test Expected Output :- Android Toast message as "Please enter expiry date of drug.",
     */
    @Test
    @Order(18)
    public void testAddDrug_With_Empty_Expiry_Date() {
        Login("12345", "12345678", 1);
        MobileElement addNewDrugButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/newDrug");
        addNewDrugButton.click();

        MobileElement drugNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/DrugName");
        drugNameTxtBox.sendKeys("CELEBREX");
        MobileElement drugNumberTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/idNumber");
        drugNumberTxtBox.sendKeys("45633");
        MobileElement storageLocationDropDown = (MobileElement) driver.findElementById("com.example.mediphix_app:id/drugStorage");
        storageLocationDropDown.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Ward Office']")));
        MobileElement storageLocationDropDownValue = (MobileElement) driver.findElementByXPath("//*[@text='Ward Office']");
        storageLocationDropDownValue.click();
        MobileElement drugExpiryTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/drugExpiry");
        drugExpiryTxtBox.sendKeys("");
        MobileElement drugSaveButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerDrugBtn");
        drugSaveButton.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Please enter expiry date of drug.", toastMessageElement.getText(), "Alert message text is not as expected.");    
    }

    /*
     * Test desc :- To test adding drug with passing all validation.
     * Project requirement - Drug should be added if all validation is passed on drug add screen.
     * Test Expected Output :- Android Toast message as "Drug successfully saved",
     */
    @Test
    @Order(19)
    public void testAddDrug() {
        Login("12345", "12345678", 1);
        MobileElement addNewDrugButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/newDrug");
        addNewDrugButton.click();

        MobileElement drugNameTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/DrugName");
        drugNameTxtBox.sendKeys("CELEBREX");
        MobileElement drugNumberTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/idNumber");
        drugNumberTxtBox.sendKeys("45633");
        MobileElement storageLocationDropDown = (MobileElement) driver.findElementById("com.example.mediphix_app:id/drugStorage");
        storageLocationDropDown.click();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Ward Office']")));
        MobileElement storageLocationDropDownValue = (MobileElement) driver.findElementByXPath("//*[@text='Ward Office']");
        storageLocationDropDownValue.click();
        MobileElement drugExpiryTxtBox = (MobileElement) driver.findElementById("com.example.mediphix_app:id/drugExpiry");
        drugExpiryTxtBox.sendKeys("04/05/2024");
        MobileElement drugSaveButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/registerDrugBtn");
        drugSaveButton.click();
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//android.widget.Toast)[1]")));
        MobileElement toastMessageElement = (MobileElement) driver.findElement(By.xpath("(//android.widget.Toast)[1]"));
        Assertions.assertEquals("Drug successfully saved", toastMessageElement.getText(), "Alert message text is not as expected.");    
    }

    /*
     * Test desc :- To test drug added in test case 19 is available in Drug store.
     * Project requirement - Newly added drug should be listed under List of Drugs screen with correct information.
     * Test Expected Output :- Newly added drug should be listed with correct drug information.
     */
    @Test
    @Order(20)
    public void testDrugAddedInDrugStore(){
        Login("12345", "12345678", 1);     
        MobileElement homeButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/home");
        homeButton.click();
        MobileElement listDrugsButton = (MobileElement) driver.findElementById("com.example.mediphix_app:id/listOfDrugsBtn");
        listDrugsButton.click();
        MobileElement drugName = (MobileElement) driver.findElementByXPath("(//android.widget.TextView[@resource-id=\"com.example.mediphix_app:id/drug_name\"])[4]");
        MobileElement drugId = (MobileElement) driver.findElementByXPath("//android.widget.TextView[@resource-id=\"com.example.mediphix_app:id/drug_number\" and @text=\"ID: 45633\"]");
        MobileElement drugDate = (MobileElement) driver.findElementByXPath("//android.widget.TextView[@resource-id=\"com.example.mediphix_app:id/drug_expiry\" and @text=\"04/05/2024\"]");
	
        Assertions.assertEquals("CELEBREX", drugName.getText(), "Drug Name doesn\'t match."); 
        Assertions.assertEquals("ID: 45633", drugId.getText(), "Alert message text is not as expected."); 
        Assertions.assertEquals("04/05/2024", drugDate.getText(), "Alert message text is not as expected."); 
    }

    @AfterEach
    public void tearDown() {        
        if (driver != null) {
            driver.quit();
        }
    }

    @SuppressWarnings("deprecation")
    @AfterAll
    public static void StopRecording() {
        if (screenRecordProcess != null) {
            try {
                screenRecordProcess.waitFor(120, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            screenRecordProcess.destroy();
        }

        // Pull the recording from the device to the local machine
        try {
            Runtime.getRuntime().exec("adb pull /sdcard/testRecording.mp4 ./testRecording.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
