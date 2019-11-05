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
/*
 * Created on 04.01.2013
 *
 * Copyright(c) 2011 - 2012 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.pageobjects;

import eu.tsystems.mms.tic.testframework.pageobjects.internal.BasicGuiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.asserts.GuiElementAssert;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.asserts.IBinaryPropertyAssertion;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.asserts.IBoundingBoxAssertion;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.asserts.IImageAssertion;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.asserts.IStringPropertyAssertion;
import org.openqa.selenium.WebElement;

/**
 * Components implementation
 * @author Mike Reiche
 */
public abstract class Component<SELF extends Component<SELF>> extends AbstractFluentPage<SELF> implements IComponent {

    protected final IGuiElement rootElement;
    private final Finder defaultFinder;

    public Component(IGuiElement rootElement) {
        super(rootElement.getWebDriver());
        this.rootElement = rootElement;
        defaultFinder = withAncestor(rootElement);
    }

    @Override
    public IImageAssertion screenshot() {
        return rootElement.screenshot();
    }

    @Override
    public BasicGuiElement scrollTo(int yOffset) {
        return rootElement.scrollTo(yOffset);
    }

    @Override
    public BasicGuiElement highlight() {
        return rootElement.highlight();
    }

    @Override
    public Locate getLocate() {
        return rootElement.getLocate();
    }

    @Override
    public WebElement getWebElement() {
        return rootElement.getWebElement();
    }

    protected IGuiElement find(Locate locator) {
        return defaultFinder.find(locator);
    }

    @Override
    @Deprecated
    public Page refresh() {
        return super.refresh();
    }

    @Override
    @Deprecated
    public GuiElementAssert nonFunctionalAsserts() {
        return rootElement.nonFunctionalAsserts();
    }

    @Override
    @Deprecated
    public GuiElementAssert asserts() {
        return rootElement.asserts();
    }

    @Override
    @Deprecated
    public GuiElementAssert instantAsserts() {
        return rootElement.instantAsserts();
    }

    @Override
    public IBinaryPropertyAssertion<Boolean> present() {
        return rootElement.present();
    }

    @Override
    public IBinaryPropertyAssertion<Boolean> visible(boolean complete) {
        return rootElement.visible(complete);
    }

    @Override
    public IStringPropertyAssertion<String> tagName() {
        return rootElement.tagName();
    }

    @Override
    public IBoundingBoxAssertion boundingBox() {
        return rootElement.boundingBox();
    }

    @Override
    public IBinaryPropertyAssertion<Boolean> displayed() {
        return rootElement.displayed();
    }
}
