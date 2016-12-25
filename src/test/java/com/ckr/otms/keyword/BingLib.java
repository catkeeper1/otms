package com.ckr.otms.keyword;

import com.mysql.jdbc.TimeUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ruoli.chen on 23/12/2016.
 */
public class BingLib {
    private WebDriver driver;

    private WebDriver getDriver(){
        if(driver == null){
            System.setProperty("webdriver.ie.driver","D:/IEDriverServer.exe");
            driver = new InternetExplorerDriver();
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        }

        return driver;
    }

    public void openBing(){
        getDriver().get("http://www.bing.com");
    }

    public void search(String qwords){
        WebElement input = getDriver().findElement(By.xpath("//input[@name='q']"));
        input.sendKeys(qwords);

        WebElement goBtn = getDriver().findElement(By.xpath("//input[@name='go']"));
        goBtn.click();
    }

    public void goToPage(Integer pageNo){

        getDriver().findElement(By.tagName("body"));

        WebElement nav = getDriver().findElement(By.xpath("//nav[@role='navigation']"));
        List<WebElement> links = nav.findElements(By.tagName("li"));
        links.get(pageNo - 1).click();
    }
}
