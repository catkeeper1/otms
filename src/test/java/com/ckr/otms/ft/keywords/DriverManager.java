package com.ckr.otms.ft.keywords;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Private WebDriver instance for other keyword implemented in JAVA.
 * This class will cache WebDriver in thread local object so that different keywords run in the same
 * thread can get the same instance of WebDriver.
 * All keywords should get WebDriver instance from this class but not create them with "new".
 */
public class DriverManager {

    private static final String DEFAULT_BROWSER_NAME = "DEFAULT_BROWSER_NAME";

    private static ThreadLocal<Map<String, WebDriver>> driverCache = new ThreadLocal<>();
    private static ThreadLocal<String> currentBrowserName = new ThreadLocal<>();

    static {
        driverCache.set(new HashMap<>());
        currentBrowserName.set(DEFAULT_BROWSER_NAME);
    }


    /**
     * Get an instance of WebDriver from cache. If there there is no instance in the cache, create a new one.
     * This method will switch the cached browser name to parameter browserName.
     *
     * @param browserName It possible that one test case manipulate multiple browsers so that it is necessary
     *                     to assign a name that link to the returned WebDriver. When a WebDriver is created with a
     *                     name, it can be retrieved from the cache with the same name later.
     *
     * @return A WebDriver instance that is available for testing.
     * @see DriverManager#switchToBrowser(String)
     */
    public static WebDriver getCurrentDriver(String browserName){

        Map<String,WebDriver> driverMap = driverCache.get();

        WebDriver driver = driverMap.get(browserName);

        if(driver == null){
            driver = new InternetExplorerDriver();

            //if the element is not availabe, the findElement API should be return in very short time
            //If you need to wait for some elements, please use explicit waiting.
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.MICROSECONDS);

            driverMap.put(browserName, driver);
        }

        switchToBrowser(browserName);

        return driver;
    }

    /**
     * Use the current browser name in cache to call {@link DriverManager#getCurrentDriver(String)}.
     *
     */
    public static WebDriver getCurrentDriver(){
        return getCurrentDriver(currentBrowserName.get());
    }

    /**
     * It is possible that one test case need to manipulate multiple browser
     * This class cache a current browser name in thread local. This method is used to change the browser name in
     * cache. Once browser name is changed the {@link DriverManager#getCurrentDriver()} will always use the
     * cached browser name to retrieve WebDriver instance from cache.
     * @param browserName the browser name that will be saved in cache.
     *
     */
    public static void switchToBrowser(String browserName){
        if(browserName == null){
            currentBrowserName.set(DEFAULT_BROWSER_NAME);
        } else {
            currentBrowserName.set(browserName);
        }
    }

    /**
     * Close all WebDriver instance in cache and clear the cache.
     * Usually, this method should be called in tear down processing.
     */
    public void closeAllBrowser(){
        Map<String, WebDriver> driverMap = driverCache.get();

        driverMap.values().stream().forEach(WebDriver::close);

        driverMap.clear();
    }

}
