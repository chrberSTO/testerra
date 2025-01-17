/*
 * Testerra
 *
 * (C) 2020, Peter Lehmann, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
 package eu.tsystems.mms.tic.testframework.webdrivermanager;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.constants.TesterraProperties;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.utils.StringUtils;
import eu.tsystems.mms.tic.testframework.webdrivermanager.desktop.WebDriverMode;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class DesktopWebDriverRequest extends AbstractWebDriverRequest implements Loggable, Serializable {

    private URL seleniumServerURL;
    private WebDriverMode webDriverMode;

    public URL getSeleniumServerUrl() {
        if (seleniumServerURL == null) {
            try {
                setSeleniumServerUrl(StringUtils.getFirstValidString(
                        PropertyManager.getProperty(TesterraProperties.SELENIUM_SERVER_URL),
                        "http://" + StringUtils.getFirstValidString(PropertyManager.getProperty(TesterraProperties.SELENIUM_SERVER_HOST), "localhost") + ":" + StringUtils.getFirstValidString(PropertyManager.getProperty(TesterraProperties.SELENIUM_SERVER_PORT), "4444") + "/wd/hub"
                ));
            } catch (MalformedURLException e) {
                log().error("Unable to retrieve default Selenium URL from properties", e);
            }
        }
        return seleniumServerURL;
    }

    public DesktopWebDriverRequest setSeleniumServerUrl(String url) throws MalformedURLException {
        this.seleniumServerURL = new URL(url);
        return this;
    }

    public DesktopWebDriverRequest setSeleniumServerUrl(URL url) {
        this.seleniumServerURL = url;
        return this;
    }

    public WebDriverMode getWebDriverMode() {
        return webDriverMode;
    }

    public DesktopWebDriverRequest setWebDriverMode(WebDriverMode webDriverMode) {
        this.webDriverMode = webDriverMode;
        return this;
    }
}
