package eu.tsystems.mms.tic.testframework.test.core.pageobjects.testdata.components;

import eu.tsystems.mms.tic.testframework.pageobjects.AbstractComponent;
import eu.tsystems.mms.tic.testframework.pageobjects.TestableUiElement;
import eu.tsystems.mms.tic.testframework.pageobjects.UiElement;
import org.openqa.selenium.By;

public class TableRow extends AbstractComponent<TableRow> {
    public TableRow(UiElement rootElement) {
        super(rootElement);
    }

    @Override
    protected TableRow self() {
        return this;
    }

    public TestableUiElement linkByName() {
        return find(By.tagName("a"));
    }

    public TestableUiElement linkByXPath() {
        return find(By.xpath(".//a"));
    }
}