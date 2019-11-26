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
package eu.tsystems.mms.tic.testframework.pageobjects.internal.core;

import eu.tsystems.mms.tic.testframework.internal.ExecutionLog;
import eu.tsystems.mms.tic.testframework.logging.LogLevel;
import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Locate;
import eu.tsystems.mms.tic.testframework.pageobjects.POConfig;
import eu.tsystems.mms.tic.testframework.pageobjects.WebDriverRetainer;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.Nameable;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.TimerWrapper;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.frames.IFrameLogic;
import eu.tsystems.mms.tic.testframework.utils.StringUtils;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Holds the state of the GuiElement to interchange with any other
 * GuiElement compatible class like {@link GuiElementCore}
 */
public class GuiElementData implements
    Nameable<GuiElementData>,
    WebDriverRetainer
{
    private static final int timerSleepTimeInMs = 500;

    private final Locate locate;
    private final WebDriver webDriver;
    /**
     * @deprecated Should not be public
     */
    public final ExecutionLog executionLog;
    private final GuiElementData parent;
    private final int index;
    private GuiElement guiElement;
    private String name;
    private WebElement webElement;
    private IFrameLogic frameLogic;
    private TimerWrapper timerWrapper;
    private LogLevel logLevel = LogLevel.DEBUG;
    private LogLevel storedLogLevel = logLevel;

    /**
     * @todo Add accessor methods
     */
    public boolean shadowRoot = false;
    public boolean sensibleData = false;

    /**
     * Creates a state based on another state
     */
    public GuiElementData(GuiElementData parent, Locate locate) {
        this(
            parent.getWebDriver(),
            locate,
            parent,
            -1
        );
        frameLogic = parent.frameLogic;
        guiElement = parent.guiElement;
    }

    /**
     * Creates a state as iteration of another state
     */
    public GuiElementData(GuiElementData parent, int index) {
        this(
            parent.webDriver,
            parent.locate,
            parent,
            index
        );
        frameLogic = parent.frameLogic;
        guiElement = parent.guiElement;
    }

    public GuiElementData(
        WebDriver webDriver,
        Locate locate
    ) {
        this(webDriver, locate, null, -1);
    }

    private GuiElementData(
        WebDriver webDriver,
        Locate locate,
        GuiElementData parent,
        int index
    ) {
        this.webDriver = webDriver;
        this.locate = locate;
        this.executionLog = new ExecutionLog();
        this.parent = parent;
        if (parent == null) index = -1;
        this.index = index;
    }

    public GuiElementData setGuiElement(GuiElement guiElement) {
        this.guiElement = guiElement;
        return this;
    }

    public GuiElementData setFrameLogic(IFrameLogic frameLogic) {
        this.frameLogic = frameLogic;
        return this;
    }

    public GuiElement getGuiElement() {
        return guiElement;
    }

    public IFrameLogic getFrameLogic() {
        return frameLogic;
    }

    public GuiElementData setWebElement(WebElement webElement) {
        this.webElement = webElement;
        return this;
    }

    public WebElement getWebElement() {
        return this.webElement;
    }

    public TimerWrapper getTimerWrapper() {
        if (timerWrapper==null) {
            timerWrapper = new TimerWrapper(timerSleepTimeInMs, POConfig.getUiElementTimeoutInSeconds(), webDriver, executionLog);
        }
        return timerWrapper;
    }

    public GuiElementData getParent() {
        return parent;
    }

    public Locate getLocate() {
        return locate;
    }

    public String getBrowser() {
        return WebDriverManager.getRelatedWebDriverRequest(webDriver).browser;
    }

    @Override
    public WebDriver getWebDriver() {
        return webDriver;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean detailed) {
        String toString = "";
        if (parent != null) {
            toString = parent+".";
        }
        if (hasName()) {
            toString += name;
        }
        if (!hasName() || detailed) {
            toString += "GuiElement("+locate.toString()+")";
        }
        //toString+="("+guiElement.getLocate().toString();
//        if (hasFrameLogic()) {
//            String frameString = ", frames={";
//            if (frameLogic.hasFrames()) {
//                for (IGuiElement frame : frameLogic.getFrames()) {
//                    frameString += frame.toString() + ", ";
//                }
//            } else {
//                frameString += "autodetect, ";
//            }
//            frameString = frameString.substring(0, frameString.length() - 2);
//            toString = toString + frameString + "}";
//        }
        return toString;
    }

    public int getTimeoutInSeconds() {
        return getTimerWrapper().getTimeoutInSeconds();
    }

    public void setTimeoutInSeconds(int timeoutInSeconds) {
        getTimerWrapper().setTimeoutInSeconds(timeoutInSeconds);
    }

    public boolean hasName() {
        return !StringUtils.isEmpty(name);
    }

    public boolean hasFrameLogic() {
        return frameLogic != null;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        // store previous log level away
        this.storedLogLevel = this.logLevel;
        // set new log level
        this.logLevel = logLevel;
    }

    public void resetLogLevel() {
        this.logLevel = this.storedLogLevel;
    }

    @Override
    public GuiElementData setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
