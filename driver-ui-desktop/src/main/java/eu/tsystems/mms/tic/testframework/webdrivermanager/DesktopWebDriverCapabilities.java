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
 *     Peter Lehmann <p.lehmann@t-systems.com>
 *     pele <p.lehmann@t-systems.com>
 */
package eu.tsystems.mms.tic.testframework.webdrivermanager;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.constants.Browsers;
import eu.tsystems.mms.tic.testframework.constants.ErrorMessages;
import eu.tsystems.mms.tic.testframework.exceptions.FennecRuntimeException;
import eu.tsystems.mms.tic.testframework.exceptions.FennecSystemException;
import eu.tsystems.mms.tic.testframework.utils.StringUtils;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

/**
 * Created by pele on 08.01.2015.
 */
final class DesktopWebDriverCapabilities extends WebDriverCapabilities {

    static void addContextCapabilities(DesiredCapabilities baseCapabilities, DesktopWebDriverRequest desktopWebDriverRequest) {
        /*
        priority:

        baseCapabilities
        overwritten by GLOBAL CAPS
        overwritten by THREAD CAPS
        overwritten by session caps from map
        overwritten by session caps desired

        ** overwritten if not empty
         */

        if (baseCapabilities == null) {
            baseCapabilities = new DesiredCapabilities();
        }

        /*
        add global caps
         */
        for (String key : GLOBALCAPABILITIES.keySet()) {
            Object value = GLOBALCAPABILITIES.get(key);
            baseCapabilities.setCapability(key, value);
        }

        /*
        add thread local caps
         */
        Map<String, Object> threadLocalCaps = THREAD_CAPABILITIES.get();
        if (threadLocalCaps != null) {
            for (String key : threadLocalCaps.keySet()) {
                Object value = threadLocalCaps.get(key);
                baseCapabilities.setCapability(key, value);
            }
        }

        /*
        add session caps
         */
        if (desktopWebDriverRequest.sessionCapabilities != null) {
            for (String key : desktopWebDriverRequest.sessionCapabilities.keySet()) {
                Object value = desktopWebDriverRequest.sessionCapabilities.get(key);
                baseCapabilities.setCapability(key, value);
            }
        }

        if (desktopWebDriverRequest.desiredCapabilities != null) {
            Map<String, ?> stringMap = desktopWebDriverRequest.desiredCapabilities.asMap();
            for (String key : stringMap.keySet()) {
                Object value = stringMap.get(key);
                baseCapabilities.setCapability(key, value);
            }
        }

    }

    static DesiredCapabilities createCapabilities(final WebDriverManagerConfig config, DesktopWebDriverRequest desktopWebDriverRequest) {
        String browser = desktopWebDriverRequest.browser;
        if (browser == null) {
            throw new FennecRuntimeException(
                    "Browser is not set correctly");
        }
        final DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        switch (browser) {
            case Browsers.htmlunit:
                LOGGER.info("Creating capabilities for HtmlUnitDriver");
                // need to enable Javascript
                desiredCapabilities.setBrowserName(BrowserType.HTMLUNIT);
                desiredCapabilities.setJavascriptEnabled(false);
                break;
            case Browsers.phantomjs:
                LOGGER.info("Creating capabilities for PhantomJS");
                // need to enable Javascript
                desiredCapabilities.setBrowserName(BrowserType.PHANTOMJS);
                desiredCapabilities.setJavascriptEnabled(true);
                break;
            case Browsers.firefox:
                desiredCapabilities.setBrowserName(BrowserType.FIREFOX);

                FirefoxOptions firefoxOptions = new FirefoxOptions();
                desiredCapabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
                break;
            case Browsers.safari:
                LOGGER.info("Creating capabilities for SafariDriver");
                desiredCapabilities.setBrowserName(BrowserType.SAFARI);
                break;
            case Browsers.edge:
                LOGGER.info("Creating capabilities for Edge");
                desiredCapabilities.setBrowserName(BrowserType.EDGE);

                EdgeOptions edgeOptions = new EdgeOptions();
                final String platform = null;
                edgeOptions.setCapability("platform", platform);

                desiredCapabilities.setCapability("edgeOptions", edgeOptions);
//                desiredCapabilities.setCapability(EdgeOptions.CAPABILITY, edgeOptions);
                break;
            case Browsers.chrome:
            case Browsers.chromeHeadless:
                LOGGER.info("Creating capabilities for ChromeDriver");
                desiredCapabilities.setBrowserName(BrowserType.CHROME);
                {
                    ChromeOptions chromeOptions = new ChromeOptions();
                    desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                }
                break;
            case Browsers.ie:
                LOGGER.info("Creating capabilities for InternetExplorerDriver");
                desiredCapabilities.setBrowserName(BrowserType.IEXPLORE);

                InternetExplorerOptions options = new InternetExplorerOptions();
                options.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

                desiredCapabilities.setCapability("se:ieOptions", options);
                break;
            default:
                throw new FennecSystemException(ErrorMessages.browserNotSupportedHere(browser));
        }

        /*
        set browser version
         */
        // load global setting
        String version = null;

        // load explicit, browser specific version setting, if present
        final String explicitVersion = PropertyManager.getProperty(browser + ".version", null);
        if (!StringUtils.isStringEmpty(explicitVersion)) {
            version = explicitVersion;
        }

        // overload with global browser.version value, if present
        if (!StringUtils.isStringEmpty(config.browserVersion)) {
            version = config.browserVersion;
        }

        // overload with explicit session request setting, if present
        if (desktopWebDriverRequest.browserVersion != null) {
            version = desktopWebDriverRequest.browserVersion;
        }

        // set into capabilities
        if (!StringUtils.isStringEmpty(version)) {
            WebDriverManagerUtils.addBrowserVersionToCapabilities(desiredCapabilities, version);
        }

        /*
         * add own desired capabilities
         */
        addContextCapabilities(desiredCapabilities, desktopWebDriverRequest);

        /*
        add some hidden configs
         */
        if (Browsers.chromeHeadless.equalsIgnoreCase(browser)) {
            ChromeOptions chromeOptions = (ChromeOptions) desiredCapabilities.getCapability(ChromeOptions.CAPABILITY);
            if (chromeOptions == null) {
                chromeOptions = new ChromeOptions();
            }
            chromeOptions.addArguments("--headless", "--disable-gpu");
            desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        }

        return desiredCapabilities;
    }
}
