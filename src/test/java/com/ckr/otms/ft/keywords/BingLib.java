package com.ckr.otms.ft.keywords;

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
        System.out.println("*INFO* go to page " + pageNo );
        System.out.println("*HTML* <img src=\"http://g.hiphotos.baidu.com/baike/whfpf%3D72%2C72%2C0/sign=c18f90a3aeec8a13144f04a0913ea6bd/e824b899a9014c08f8a59ce90d7b02087af4f4e4.jpg\" alt=\"机器人操作系统\"/>");
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

        try {
            //this is used for demo only. During demo, I want people can see what happen so that just
            //stop for 2 seconds.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
