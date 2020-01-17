/*
 * (C) Copyright T-Systems Multimedia Solutions GmbH 2018, ..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Peter Lehmann
 *     pele
 */
package eu.tsystems.mms.tic.testframework.core.test.pageobjects.pagefactory;

import eu.tsystems.mms.tic.testframework.AbstractWebDriverTest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.DesktopWebDriverCapabilities;
import eu.tsystems.mms.tic.testframework.webdrivermanager.DesktopWebDriverRequest;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverManager;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverRequest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.regex.Pattern;

public class DesktopWebDriverFactoryTest extends AbstractWebDriverTest {

    @Test
    public void testT01_BaseURL() throws Exception {
        DesktopWebDriverRequest request = new DesktopWebDriverRequest();
        request.baseUrl = "http://google.de";
        //request.webDriverMode = WebDriverMode.local;
        //request.browser = Browsers.phantomjs;

        WebDriver driver = WebDriverManager.getWebDriver(request);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("google"), "Current URL contains google - actual: " + currentUrl);
    }

    @Test
    public void testT02_BaseURL_NotSet() throws Exception {
        DesktopWebDriverRequest request = new DesktopWebDriverRequest();
        //request.webDriverMode = WebDriverMode.local;
        //request.browser = Browsers.phantomjs;

        WebDriver driver = WebDriverManager.getWebDriver(request);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("localhost"), "Current URL is invalid - actual: " + currentUrl);
    }

    @Test
    public void testT03_EndPointCapabilities() throws Exception {
        DesktopWebDriverRequest request = new DesktopWebDriverRequest();
        request.baseUrl = "http://google.de";
        //request.webDriverMode = WebDriverMode.local;
        //request.browser = Browsers.phantomjs;

        /*
        create caps
         */
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("enableVideo", true);
        caps.setCapability("enableVNC", true);

        // register caps
        DesktopWebDriverCapabilities.registerEndPointCapabilities(Pattern.compile(".*localhost.*"), caps);

        // start session
        WebDriver driver = WebDriverManager.getWebDriver(request);

        WebDriverRequest r = WebDriverManager.getRelatedWebDriverRequest(driver);
        Map<String, Object> sessionCapabilities = ((DesktopWebDriverRequest) r).sessionCapabilities;

        Assert.assertEquals(sessionCapabilities.get("tap:projectId"), caps.getCapability("tap:projectId"), "EndPoint Capability is set");
    }

}