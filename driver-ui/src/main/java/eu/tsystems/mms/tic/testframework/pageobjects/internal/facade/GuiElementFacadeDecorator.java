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
 package eu.tsystems.mms.tic.testframework.pageobjects.internal.facade;

import eu.tsystems.mms.tic.testframework.pageobjects.GuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.Locate;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.core.GuiElementData;
import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * This is meant as template. If you want to implement a Decorator, copy this one and add your decorations.
 * Obviously, the Constructor has to be made public, or any other construction method has to be offered.
 * <p>
 * DO NOT EXTEND THIS DECORATOR BY DEFAULT!!
 * If all, or nearly all methods are decorated, implement the interface instead to ensure that all methods are covered.
 * <p>
 * Created by rnhb on 11.08.2015.
 */
public abstract class GuiElementFacadeDecorator implements GuiElementFacade {

    final GuiElementFacade decoratedFacade;
    private final GuiElementData guiElementData;

    public GuiElementFacadeDecorator(GuiElementFacade decoratedFacade, GuiElementData guiElementData) {
        this.decoratedFacade = decoratedFacade;
        this.guiElementData = guiElementData;
    }

    /**
     * called before any delegation.
     */
    private void beforeDelegation(String methodName) {
        beforeDelegation(methodName, "");
    }

    protected abstract void beforeDelegation(String methodName, String parameterInfo);

    /**
     * called after any delegation.
     *
     * @param result Result of delegation or null
     */
    protected abstract void afterDelegation(String result);

    /**
     * called only before methods that perform an action
     *
     * @param message description of method to delegate
     */
    protected void beforeActionDelegation(String message) {

    }

    /**
     * called only after methods that perform an action
     */
    protected void afterActionDelegation() {

    }

    private void afterDelegation() {
        afterDelegation(null);
    }

    @Override
    public int getNumberOfFoundElements() {
        beforeDelegation("getNumberOfFoundElements");
        int numberOfFoundElements = decoratedFacade.getNumberOfFoundElements();
        afterDelegation();
        return numberOfFoundElements;
    }

    @Override
    public WebElement getWebElement() {
        beforeDelegation("getWebElement");
        WebElement webElement = decoratedFacade.getWebElement();
        afterDelegation();
        return webElement;
    }

    @Override
    public By getBy() {
        By by = decoratedFacade.getBy();
        return by;
    }

    @Override
    @Deprecated
    public void scrollToElement(int yOffset) {
        beforeActionDelegation("scrollToElement");
        beforeDelegation("scrollToElement");
        decoratedFacade.scrollToElement(yOffset);
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void scrollIntoView(Point offset) {
        beforeActionDelegation("center");
        beforeDelegation("center");
        decoratedFacade.scrollIntoView(offset);
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void select() {
        beforeActionDelegation("select");
        beforeDelegation("select");
        decoratedFacade.select();
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void deselect() {
        beforeActionDelegation("deselect");
        beforeDelegation("deselect");
        decoratedFacade.deselect();
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void type(String text) {
        final String msg = "\"" + obfuscateIfSensible(text) + "\"";
        beforeActionDelegation("type " + msg);
        beforeDelegation("type", msg);
        decoratedFacade.type(text);
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void click() {
        beforeActionDelegation("click");
        beforeDelegation("click");
        decoratedFacade.click();
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void clickJS() {
        beforeActionDelegation("clickJS");
        beforeDelegation("clickJS");
        decoratedFacade.clickJS();
        afterDelegation();
        afterActionDelegation();
    }


    @Override
    public void rightClick() {
        beforeActionDelegation("rightClick");
        beforeDelegation("rightClick");
        decoratedFacade.rightClick();
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void rightClickJS() {
        beforeActionDelegation("rightClickJS");
        beforeDelegation("rightClickJS");
        decoratedFacade.rightClickJS();
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void clickAbsolute() {
        beforeActionDelegation("clickAbsolute");
        beforeDelegation("clickAbsolute");
        decoratedFacade.clickAbsolute();
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void mouseOverAbsolute2Axis() {
        beforeActionDelegation("mouseOverAbsolute2Axis");
        beforeDelegation("mouseOverAbsolute2Axis");
        decoratedFacade.mouseOverAbsolute2Axis();
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void submit() {
        beforeActionDelegation("submit");
        beforeDelegation("submit");
        decoratedFacade.submit();
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        String chars = "";

        if (charSequences == null) {
            return;
        }

        if (charSequences.length > 1) {
            for (CharSequence charSequence : charSequences) {
                chars += "[" + charSequence + "] ";
            }
        }
        else {
            chars += charSequences[0];
        }

        final String message = "\"" + obfuscateIfSensible(chars) + "\"";
        beforeActionDelegation("sendKeys " + message);
        beforeDelegation("sendKeys", message);
        decoratedFacade.sendKeys(charSequences);
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void clear() {
        beforeActionDelegation("clear");
        beforeDelegation("clear");
        decoratedFacade.clear();
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public String getTagName() {
        beforeDelegation("getTagName");
        String tagName = decoratedFacade.getTagName();
        afterDelegation("getTagName() = " + tagName);
        return tagName;
    }

    @Override
    public String getAttribute(String attributeName) {
        beforeDelegation("getAttribute", "\"" + attributeName + "\"");
        String attribute = decoratedFacade.getAttribute(attributeName);
        afterDelegation("getAttribute(" + attributeName + ") = " + attribute);
        return attribute;
    }

    @Override
    public boolean isSelected() {
        beforeDelegation("isSelected");
        boolean selected = decoratedFacade.isSelected();
        afterDelegation();
        return selected;
    }

    @Override
    public boolean isEnabled() {
        beforeDelegation("isEnabled");
        boolean enabled = decoratedFacade.isEnabled();
        afterDelegation();
        return enabled;
    }

    @Override
    public String getText() {
        beforeDelegation("getText");
        String text = decoratedFacade.getText();
        afterDelegation("getText() = " + text);
        return text;
    }

    @Override
    public GuiElement getSubElement(By byLocator, String description) {
        beforeDelegation("getSubElement", ">" + description + "< " + byLocator);
        GuiElement subElement = decoratedFacade.getSubElement(byLocator, description);
        afterDelegation("getSubElement(" + byLocator + ", " + description + ") = " + subElement);
        return subElement;
    }

    @Override
    public GuiElement getSubElement(Locate locator) {
        return decoratedFacade.getSubElement(locator);
    }

    @Override
    public GuiElement getSubElement(By by) {
        return decoratedFacade.getSubElement(by);
    }

    @Override
    public boolean isDisplayed() {
        beforeDelegation("isDisplayed");
        boolean displayed = decoratedFacade.isDisplayed();
        afterDelegation("isDisplayed() = " + displayed);
        return displayed;
    }

    @Override
    public boolean isVisible(boolean complete) {
        beforeDelegation("isVisible");
        boolean visible = decoratedFacade.isVisible(complete);
        afterDelegation("isVisible() = " + visible);
        return visible;
    }

    @Override
    public boolean isSelectable() {
        beforeDelegation("isSelectable");
        boolean selectable = decoratedFacade.isSelectable();
        afterDelegation("isSelectable() = " + selectable);
        return selectable;
    }

    @Override
    public Point getLocation() {
        beforeDelegation("getLocation");
        Point location = decoratedFacade.getLocation();
        afterDelegation("getLocation() = " + location);
        return location;
    }

    @Override
    public Dimension getSize() {
        beforeDelegation("getSize");
        Dimension size = decoratedFacade.getSize();
        afterDelegation("getSize() = " + size);
        return size;
    }

    @Override
    public String getCssValue(String cssIdentifier) {
        beforeDelegation("getCssValue", "\"" + cssIdentifier + "\"");
        String cssValue = decoratedFacade.getCssValue(cssIdentifier);
        afterDelegation("getCssValue(" + cssIdentifier + ") = " + cssValue);
        return cssValue;
    }

    @Override
    public void mouseOver() {
        beforeActionDelegation("mouseOver");
        beforeDelegation("mouseOver", "mouseOver");
        decoratedFacade.mouseOver();
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void mouseOverJS() {
        beforeActionDelegation("mouseOverJS");
        beforeDelegation("mouseOverJS");
        decoratedFacade.mouseOverJS();
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public boolean isPresent() {
        beforeDelegation("isPresent");
        boolean present = decoratedFacade.isPresent();
        afterDelegation("isPresent() = " + present);
        return present;
    }

    @Override
    public Select getSelectElement() {
        beforeDelegation("getSelectElement");
        Select selectElement = decoratedFacade.getSelectElement();
        afterDelegation("getSelectElement() = " + selectElement);
        return selectElement;
    }

    @Override
    public List<String> getTextsFromChildren() {
        beforeDelegation("getTextsFromChildren");
        List<String> textsFromChildren = decoratedFacade.getTextsFromChildren();
        afterDelegation("getTextsFromChildren() = " + textsFromChildren);
        return textsFromChildren;
    }

    @Override
    public boolean anyFollowingTextNodeContains(String contains) {
        beforeDelegation("anyFollowingTextNodeContains", "\"" + contains + "\"");
        boolean b = decoratedFacade.anyFollowingTextNodeContains(contains);
        afterDelegation("anyFollowingTextNodeContains(" + contains + ") = " + b);
        return b;
    }

    @Override
    public void doubleClick() {
        beforeActionDelegation("doubleClick");
        beforeDelegation("doubleClick");
        decoratedFacade.doubleClick();
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void doubleClickJS() {
        beforeActionDelegation("doubleClickJS");
        beforeDelegation("doubleClickJS");
        decoratedFacade.doubleClickJS();
        afterDelegation();
        afterActionDelegation();
    }

    @Override
    public void highlight(Color color) {
        beforeDelegation("highlight");
        decoratedFacade.highlight(color);
        afterDelegation();
    }

    @Override
    public int getLengthOfValueAfterSendKeys(String textToInput) {
        beforeDelegation("getLengthOfValueAfterSendKeys", "\"" + textToInput + "\"");
        int lengthOfValueAfterSendKeys = decoratedFacade.getLengthOfValueAfterSendKeys(textToInput);
        afterDelegation("getLengthOfValueAfterSendKeys(" + textToInput + ") = " + lengthOfValueAfterSendKeys);
        return lengthOfValueAfterSendKeys;
    }

    @Override
    public boolean waitForIsPresent() {
        beforeDelegation("waitForIsPresent");
        boolean b = decoratedFacade.waitForIsPresent();
        afterDelegation("waitForIsPresent() = " + b);
        return b;
    }

    @Override
    public boolean waitForIsNotPresent() {
        beforeDelegation("waitForIsNotPresent");
        boolean b = decoratedFacade.waitForIsNotPresent();
        afterDelegation("waitForIsNotPresent() = " + b);
        return b;
    }

    @Override
    public boolean waitForIsEnabled() {
        beforeDelegation("waitForIsEnabled");
        boolean b = decoratedFacade.waitForIsEnabled();
        afterDelegation("waitForIsEnabled() = " + b);
        return b;
    }

    @Override
    public boolean waitForIsDisabled() {
        beforeDelegation("waitForIsDisabled");
        boolean b = decoratedFacade.waitForIsDisabled();
        afterDelegation("waitForIsDisabled() = " + b);
        return b;
    }

    @Override
    public boolean waitForAnyFollowingTextNodeContains(String contains) {
        beforeDelegation("waitForAnyFollowingTextNodeContains", "\"" + contains + "\"");
        boolean b = decoratedFacade.waitForAnyFollowingTextNodeContains(contains);
        afterDelegation("waitForAnyFollowingTextNodeContains(" + contains + ") = " + b);
        return b;
    }

    @Override
    public boolean waitForIsDisplayed() {
        beforeDelegation("waitForIsDisplayed");
        boolean b = decoratedFacade.waitForIsDisplayed();
        afterDelegation("waitForIsDisplayed() = " + b);
        return b;
    }

    @Override
    public boolean waitForIsNotDisplayed() {
        beforeDelegation("waitForIsNotDisplayed");
        boolean b = decoratedFacade.waitForIsNotDisplayed();
        afterDelegation("waitForIsNotDisplayed() = " + b);
        return b;
    }

    @Override
    public boolean waitForIsVisible(boolean complete) {
        beforeDelegation("waitForIsVisible");
        boolean b = decoratedFacade.waitForIsVisible(complete);
        afterDelegation("waitForIsVisible() = " + b);
        return b;
    }

    @Override
    public boolean waitForIsNotVisible() {
        beforeDelegation("waitForIsNotVisible");
        boolean b = decoratedFacade.waitForIsNotVisible();
        afterDelegation("waitForIsNotVisible() = " + b);
        return b;
    }

    @Override
    public boolean waitForIsDisplayedFromWebElement() {
        beforeDelegation("waitForIsDisplayedFromWebElement");
        boolean b = decoratedFacade.waitForIsDisplayedFromWebElement();
        afterDelegation("waitForIsDisplayedFromWebElement() = " + b);
        return b;
    }

    @Override
    public boolean waitForIsNotDisplayedFromWebElement() {
        beforeDelegation("waitForIsNotDisplayedFromWebElement");
        boolean b = decoratedFacade.waitForIsNotDisplayedFromWebElement();
        afterDelegation("waitForIsNotDisplayedFromWebElement() = " + b);
        return b;
    }

    @Override
    public boolean waitForIsSelected() {
        beforeDelegation("waitForIsSelected");
        boolean b = decoratedFacade.waitForIsSelected();
        afterDelegation("waitForIsSelected() = " + b);
        return b;
    }

    @Override
    public boolean waitForIsNotSelected() {
        beforeDelegation("waitForIsNotSelected");
        boolean b = decoratedFacade.waitForIsNotSelected();
        afterDelegation("waitForIsNotSelected() = " + b);
        return b;
    }

    @Override
    public boolean waitForText(String text) {
        beforeDelegation("waitForText", "\"" + text + "\"");
        boolean b = decoratedFacade.waitForText(text);
        afterDelegation("waitForIsPresent(" + text + ") = " + b);
        return b;
    }

    @Override
    public boolean waitForTextContains(String... text) {
        beforeDelegation("waitForTextContains", "\"" + Arrays.toString(text) + "\"");
        boolean b = decoratedFacade.waitForTextContains(text);
        afterDelegation("waitForIsPresent(" + Arrays.toString(text) + ") = " + b);
        return b;
    }

    @Override
    public boolean waitForTextContainsNot(String... text) {
        beforeDelegation("waitForTextContainsNot", "\"" + Arrays.toString(text) + "\"");
        boolean b = decoratedFacade.waitForTextContainsNot(text);
        afterDelegation("waitForIsGone(" + Arrays.toString(text) + ") = " + b);
        return b;
    }

    @Override
    public boolean waitForAttribute(String attributeName) {
        beforeDelegation("waitForAttribute", "\"" + attributeName + "\"");
        boolean b = decoratedFacade.waitForAttribute(attributeName);
        afterDelegation("waitForIsPresent(" + attributeName + ") = " + b);
        return b;
    }

    @Override
    public boolean waitForAttribute(String attributeName, String value) {
        beforeDelegation("waitForAttribute", "\"" + attributeName + "\" = \"" + value + "\"");
        boolean b = decoratedFacade.waitForAttribute(attributeName, value);
        afterDelegation("waitForAttribute(" + attributeName + ", " + value + ") = " + b);
        return b;
    }

    @Override
    public boolean waitForAttributeContains(String attributeName, String value) {
        beforeDelegation("waitForAttributeContains", "\"" + attributeName + "\" = \"" + value + "\"");
        boolean b = decoratedFacade.waitForAttributeContains(attributeName, value);
        afterDelegation("waitForAttributeContains(" + attributeName + ", " + value + ") = " + b);
        return b;
    }

    @Override
    public boolean waitForIsSelectable() {
        beforeDelegation("waitForIsSelectable");
        boolean b = decoratedFacade.waitForIsSelectable();
        afterDelegation("waitForIsSelectable() = " + b);
        return b;
    }

    @Override
    public boolean waitForIsNotSelectable() {
        beforeDelegation("waitForIsNotSelectable");
        boolean b = decoratedFacade.waitForIsNotSelectable();
        afterDelegation("waitForIsNotSelectable() = " + b);
        return b;
    }

    @Override
    public boolean waitForAttributeContainsNot(final String attributeName, final String value) {
        beforeDelegation("waitForAttributeContainsNot");
        boolean b = decoratedFacade.waitForAttributeContainsNot(attributeName, value);
        afterDelegation("waitForAttributeContainsNot() = " + b);
        return b;
    }

    @Override
    public boolean waitForCssClassIsPresent(final String className) {
        beforeDelegation("waitForCssClassIsPresent");
        boolean b = decoratedFacade.waitForCssClassIsPresent(className);
        afterDelegation("waitForCssClassIsPresent() = " + b);
        return b;
    }

    @Override
    public boolean waitForCssClassIsNotPresent(final String className) {
        beforeDelegation("waitForCssClassIsNotPresent");
        boolean b = decoratedFacade.waitForCssClassIsNotPresent(className);
        afterDelegation("waitForCssClassIsNotPresent() = " + b);
        return b;
    }


    @Override
    public String toString() {
        String s = decoratedFacade.toString();
        return s;
    }

    private String obfuscateIfSensible(final String data) {
        if (guiElementData.sensibleData) {
            return "*****************";
        }
        return data;
    }

    @Override
    public void swipe(int offsetX, int offSetY) {
        decoratedFacade.swipe(offsetX, offSetY);
    }


    @Override
    public File takeScreenshot() {
        return decoratedFacade.takeScreenshot();
    }
}
