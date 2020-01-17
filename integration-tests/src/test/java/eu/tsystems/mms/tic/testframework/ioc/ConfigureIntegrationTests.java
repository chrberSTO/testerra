package eu.tsystems.mms.tic.testframework.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import eu.tsystems.mms.tic.testframework.pageobjects.PageObjectFactory;
import eu.tsystems.mms.tic.testframework.pageobjects.ResponsivePageFactory;

public class ConfigureIntegrationTests extends AbstractModule {
    protected void configure() {
        bind(PageObjectFactory.class).to(ResponsivePageFactory.class).in(Scopes.SINGLETON);
    }
}