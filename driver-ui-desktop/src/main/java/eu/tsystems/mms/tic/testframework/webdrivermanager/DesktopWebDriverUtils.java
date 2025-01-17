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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.model.NodeInfo;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.utils.JSUtils;
import eu.tsystems.mms.tic.testframework.utils.RESTUtils;
import java.net.URL;
import java.util.Map;

public final class DesktopWebDriverUtils implements Loggable {

    public DesktopWebDriverUtils() {

    }

    public NodeInfo getNodeInfo(URL seleniumUrl, String sessionId) {
        String url = seleniumUrl.toString();

        url = url.replace("/wd/hub", "");

        /*
        grid3 mode
         */
        try {
            String nodeResponse = RESTUtils.requestGET(url + "/host/" + sessionId, 30 * 1000, String.class);
            Gson gson = new GsonBuilder().create();
            Map map = gson.fromJson(nodeResponse, Map.class);
            double port = Double.parseDouble(map.get("Port").toString());
            return new NodeInfo(map.get("Name").toString(), (int) port);
        } catch (Exception e) {
            log().error("Could not get node info: " + e.getMessage());
            return new NodeInfo(seleniumUrl.getHost(), seleniumUrl.getPort());
        }
    }

    public void clickAbsolute(GuiElement guiElement) {
        log().trace("Absolute navigation and click on: " + guiElement.toString());
        JSUtils utils = new JSUtils();
        utils.clickAbsolute(guiElement.getWebDriver(), guiElement.getWebElement());
    }

    public void mouseOverAbsolute2Axis(GuiElement guiElement) {
        JSUtils utils = new JSUtils();
        utils.mouseOverAbsolute2Axis(guiElement.getWebDriver(), guiElement.getWebElement());
    }

    public void mouseOverJS(GuiElement guiElement) {
        JSUtils utils = new JSUtils();
        utils.mouseOver(guiElement.getWebDriver(), guiElement.getWebElement());
    }

    public void clickJS(GuiElement guiElement) {
        JSUtils utils = new JSUtils();
        utils.click(guiElement.getWebDriver(), guiElement.getWebElement());
    }

    public void rightClickJS(GuiElement guiElement) {
        JSUtils utils = new JSUtils();
        utils.rightClick(guiElement.getWebDriver(), guiElement.getWebElement());
    }

    public void doubleClickJS(GuiElement guiElement) {
        JSUtils utils = new JSUtils();
        utils.doubleClick(guiElement.getWebDriver(), guiElement.getWebElement());
    }
}
