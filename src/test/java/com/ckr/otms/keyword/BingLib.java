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
            //System.setProperty("webdriver.ie.driver","D:/IEDriverServer.exe");
            driver = new InternetExplorerDriver();
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        }

        return driver;
    }

    public void openBingPage(){
        getDriver().get("http://www.bing.com");
    }

    public void search(String qwords){
        WebElement input = getDriver().findElement(By.xpath("//input[@name='q']"));
        input.clear();
        input.sendKeys(qwords);

        WebElement goBtn = getDriver().findElement(By.xpath("//input[@name='go']"));
        goBtn.click();
    }

    public void goToPage(Integer pageNo){

        List<WebElement> navs = getDriver().findElements(By.xpath("//nav[@role='navigation']"));
        List<WebElement> links = navs.get(1).findElements(By.xpath("./ul/li/a"));

        for(WebElement link : links){


            if(link.getText().indexOf(pageNo.toString()) >= 0){
                link.click();
                return;
            }
        }
        throw new RuntimeException("cannot find page " + pageNo);
    }

    public void canSeeResult(Integer from, Integer to){
        WebElement div = getDriver().findElement(By.id("b_tween"));
        WebElement i = div.findElement(By.xpath("//span[@class='sb_count']"));


        if(i.getText().indexOf(from + "") == -1 || i.getText().indexOf(to + "") == -1){
            throw new RuntimeException("cannot find search result from record " + from + " to " + to);
        }
    }

}
