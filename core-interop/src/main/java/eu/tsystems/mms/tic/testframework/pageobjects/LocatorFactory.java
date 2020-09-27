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
/*
 * Created on 10.02.14
 *
 * Copyright(c) 1995 - 2013 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.pageobjects;

import eu.tsystems.mms.tic.testframework.pageobjects.internal.UiElement;
import org.openqa.selenium.By;

/**
 * A factory for {@link Locator}
 *
 * @author Mike Reiche
 */
public class LocatorFactory {
    public Locator by(By by) {
        return new DefaultLocator(by);
    }

    public Locator by(XPath xPath) {
        return by(new By.ByXPath(xPath.toString()));
    }

    public Locator byQa(String string) {
        return by(XPath.from("*").attribute(UiElement.Properties.QA_ATTRIBUTE.asString()).is(string));
    }
    public PreparedLocator prepare(final String format) {
        return new DefaultPreparedLocator(format);
    }
}
