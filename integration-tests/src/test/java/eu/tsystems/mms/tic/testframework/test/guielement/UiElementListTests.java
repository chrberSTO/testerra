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
package eu.tsystems.mms.tic.testframework.test.guielement;

import eu.tsystems.mms.tic.testframework.AbstractTestSitesTest;
import eu.tsystems.mms.tic.testframework.core.testpage.TestPage;
import eu.tsystems.mms.tic.testframework.pageobjects.Attribute;
import eu.tsystems.mms.tic.testframework.pageobjects.TestableUiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.UiElement;
import eu.tsystems.mms.tic.testframework.test.PageFactoryTest;
import eu.tsystems.mms.tic.testframework.test.core.pageobjects.testdata.UiElementListPage;
import eu.tsystems.mms.tic.testframework.test.core.pageobjects.testdata.components.TableRow;
import org.testng.annotations.Test;

public class UiElementListTests extends AbstractTestSitesTest implements PageFactoryTest {
    public UiElementListPage getPage() {
        return getPageFactory().createPageWithDefaultWebDriver(UiElementListPage.class);
    }

    @Test
    public void test_getSubElement_getList_byTagName() {
        UiElementListPage page = getPage();
        UiElement anchors = page.getNavigationSubElementsByTagName();
        testNavigationAnchors(anchors);
    }

    @Test
    public void test_getSubElement_getList_byChildrenXPath() {
        UiElementListPage page = getPage();
        UiElement anchors = page.getNavigationSubElementsByChildrenXPath();
        testNavigationAnchors(anchors);
    }

    @Test
    public void test_getSubElement_getList_byDescendantsXPath() {
        UiElementListPage page = getPage();
        UiElement anchors = page.getNavigationSubElementsByDescendantsXPath();
        testNavigationAnchors(anchors);
    }

    @Test
    public void test_getList_byAbsoluteChildrenXPath() {
        UiElementListPage page = getPage();
        UiElement anchors = page.getNavigationSubElementsByAbsoluteChildrenXPath();
        testNavigationAnchors(anchors);
    }

    @Test
    public void test_getList_byAbsoluteDescendantsXPath() {
        UiElementListPage page = getPage();
        UiElement anchors = page.getNavigationSubElementsByAbsoluteDescendantsXPath();
        testNavigationAnchors(anchors);
    }

    private void testNavigationAnchors(UiElement anchors) {
        anchors.expectThat().numberOfElements().is(3);

        anchors.list().first().expectThat().text().is("First");
        anchors.list().get(1).expectThat().text().is("Second");
        anchors.list().last().expectThat().text().is("Third");
    }

    private void testTableRowsAndData(TableRow tableRows) {
        tableRows.expectThat().numberOfElements().is(4);
        TestableUiElement tableDataUnspecified = tableRows.columns();
        tableDataUnspecified.expectThat().numberOfElements().is(2);

        TestableUiElement tableDataSpecified = tableRows.list().get(1).columns();
        tableDataSpecified.expectThat().numberOfElements().is(2);
    }

    @Test
    public void test_getSubElement_getList_tableRowsByTagName() {
        UiElementListPage page = getPage();
        TableRow tableRows = page.getTableRowsByTagName();
        testTableRowsAndData(tableRows);

        tableRows.list().first().linkByName().expectThat().value(Attribute.HREF).endsWith("mkri");
        tableRows.list().get(1).linkByName().expectThat().value(Attribute.HREF).endsWith("joku");
        tableRows.list().last().linkByName().expectThat().value(Attribute.HREF).endsWith("erku");
        tableRows.list().forEach(tableRow -> tableRow.linkByName().expectThat().value(Attribute.HREF).startsWith("http"));
    }

    @Test
    public void test_getSubElement_getList_tableRowsByDescendantsXPath() {
        UiElementListPage page = getPage();
        TableRow tableRows = page.getTableRowsByTagName();
        testTableRowsAndData(tableRows);

        tableRows.list().first().linkByXPath().expectThat().value(Attribute.HREF).endsWith("mkri");
        tableRows.list().get(1).linkByXPath().expectThat().value(Attribute.HREF).endsWith("joku");
        tableRows.list().last().linkByXPath().expectThat().value(Attribute.HREF).endsWith("erku");
    }

    @Override
    protected TestPage getTestPage() {
        return TestPage.LIST;
    }
}
