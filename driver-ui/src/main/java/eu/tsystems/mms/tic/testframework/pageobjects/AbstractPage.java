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
package eu.tsystems.mms.tic.testframework.pageobjects;

import eu.tsystems.mms.tic.testframework.annotations.PageOptions;
import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.enums.CheckRule;
import eu.tsystems.mms.tic.testframework.exceptions.PageNotFoundException;
import eu.tsystems.mms.tic.testframework.exceptions.TesterraRuntimeException;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.FieldAction;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.FieldWithActionConfig;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.SetGuiElementTimeoutFieldAction;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.action.SetNameFieldAction;
import eu.tsystems.mms.tic.testframework.report.model.context.MethodContext;
import eu.tsystems.mms.tic.testframework.report.utils.ExecutionContextController;
import eu.tsystems.mms.tic.testframework.webdrivermanager.WebDriverManager;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Provides basic {@link PageObject} related features:
 *      Supports page caching by {@link #store()}
 *      Supports element {@link Check}
 *      Supports {@link PageOptions}
 *      Supports deprecated custom page checks by {@link #checkPage(boolean, boolean)}
 *      Supports custom page load callbacks like {@link #assertPageIsShown()} and {@link #waitForPageToLoad()}
 * @see {https://martinfowler.com/bliki/PageObject.html}
 * @author Peter Lehmann
 * @author Mike Reiche
 */
public abstract class AbstractPage extends AbstractPageObject implements Loggable {
    /**
     * The webdriver object.
     * @todo This should be final
     */
    protected WebDriver driver;

    /**
     * Element timeout in seconds (int).
     */
    protected int elementTimeoutInSeconds = Testerra.injector.getInstance(PageOverrides.class).getTimeoutSeconds();

    /**
     * @deprecated Use {@link Loggable} instead
     */
    @Deprecated
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Page storage.
     */
    @Deprecated
    private static final ThreadLocal<AbstractPage> STORED_PAGES = new ThreadLocal<>();

    @Deprecated
    private boolean forcedGuiElementStandardAsserts = false;

    /**
     * Restore a stored page class.
     * <p/>
     * A page class can be stored with a Page.store() call.
     * <p/>
     * The current page object is then stored thread safe and can be reloaded with a Page.restore(T) call, where T is a
     * class of expected page type T. If a correct object is stored, you will get it.
     *
     * @param c   class of expected page type
     * @param <T> expected page type
     * @return stored instance
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public static <T extends Page> T restore(final Class<T> c) {
        AbstractPage page = STORED_PAGES.get();

        if (page == null) {
            throw new TesterraRuntimeException("There is no page object stored. Call store() before!");
        }

        if (c.isInstance(page)) {
            page.handleDemoMode(WebDriverManager.getWebDriver());
            return (T) page;
        } else {
            throw new TesterraRuntimeException("The page object is not of expected type.");
        }
    }

    /**
     * Setter.
     *
     * @param newElementTimeout a new timeout in seconds
     * @deprecated Use {@link PageOptions} instead
     */
    @Deprecated
    public void setElementTimeoutInSeconds(int newElementTimeout) {
        elementTimeoutInSeconds = newElementTimeout;
    }

    /**
     * Calls the assertPageIsShown method.
     */
    private void checkAdditional(boolean findNot) {
        try {
            if (findNot) {
                assertPageIsNotShown();
            } else {
                assertPageIsShown();
            }
        } catch (final Exception t) {
            /*
            set readable message
             */
            String message = "Page check failed on " + this.getClass().getSimpleName();

            MethodContext methodContext = ExecutionContextController.getCurrentMethodContext();
            if (methodContext != null) {
                methodContext.errorContext().setThrowable(message, t);
            }

            /*
            exception
             */
            throw new PageNotFoundException(message, t);
        }
    }

    /**
     * @deprecated Use {@link Check}
     */
    @Deprecated
    public void forceGuiElementStandardAsserts() {
        forcedGuiElementStandardAsserts = true;
    }

    @Override
    public PageObject checkGuiElements(CheckRule checkRule) {
        switch (checkRule) {
            case IS_NOT_PRESENT:
            case IS_NOT_DISPLAYED:
                pCheckPage(true, false, true);
                break;
            default:
                pCheckPage(false, false, true);
        }
        return this;
    }

    /**
     * @deprecated Use {@link #checkGuiElements(CheckRule)} instead
     */
    @Deprecated
    public final void checkPage(final boolean inverse, final boolean fast) {
        pCheckPage(inverse, fast, true);
    }

    @Deprecated
    protected void pCheckPage(final boolean findNot, final boolean fast, final boolean checkCaller) {

        /**
         * @todo This whole checkCaller block may be removed safely
         */
        if (checkCaller) {
        /*
        Check for class inheritance
         */
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            // The calling class always sits at index "2" (Constructor). "0" is the getStacktrace() and "1" is the checkPage() call.
            StackTraceElement stackTraceElement = stackTrace[3];
            String callingMethodName = stackTraceElement.getMethodName();
            if ("<init>".equals(callingMethodName)) {
                logger.debug("checkPage() was called from constructor. Please use PageFactory.create() and remove checkPage() from constructors.");
            }
            // Class.forName(stackTraceElement.getClassName()).isAssignableFrom(this.getClass())
            String thisClassName = this.getClass().getName();
            String callingClassName = stackTraceElement.getClassName();

            // Ensure that checkPage was not called from any parent page class.
            // If so, skip the check to avoid NPE on the not yet fully instantiated instance.
            // Other classes outside the Page hierarchy are allowed to call checkPage though (no risk of NPE due to unfinished initialization).
            Class<?> classCallingCheckPage = null;
            try {
                classCallingCheckPage = Class.forName(stackTraceElement.getClassName());
            } catch (ClassNotFoundException e) {
                logger.debug("Internal error: Failed to load class that called checkPage, identified by name from stacktrace.", e);
            }
            /**
             * We have explicitly check for {@link Page} here,
             * because {@link AbstractComponent} is also an {@link AbstractPage} which is legal to have.
             */
            if (classCallingCheckPage != null && Page.class.isAssignableFrom(classCallingCheckPage)) {
                if (!callingClassName.equals(thisClassName)) {
                    logger.debug("Not performing checkPage() for " + callingClassName + ", because the calling instance is of class " + thisClassName + ".");
                    return;
                }
            }
        }

        /*
        Logging and demo mode
         */
        String classSimpleName = this.getClass().getSimpleName();
        logger.info("Checking mandatory elements for " + classSimpleName);

        handleDemoMode(this.driver);

        /*
        page checks
         */
        checkPagePreparation();
        try {
            checkAnnotatedFields(findNot, fast);
            checkAdditional(findNot);
        } catch (Throwable throwable) {
            try {
                // call page error state logic
                checkPageErrorState(throwable);

                // if nothing is checked then the orig throwable is thrown
                throw throwable;
            } catch (Throwable importantThrowable) {
                String message = importantThrowable.getMessage();
                if (message == null) {
                    message = "Page not found: " + classSimpleName;
                }

                /*
                set readable message
                 */
                String throwableMessage = throwable.getMessage();
                if (throwableMessage == null) {
                    throwableMessage = message;
                }

                MethodContext methodContext = ExecutionContextController.getCurrentMethodContext();
                if (methodContext != null) {
                    methodContext.errorContext().setThrowable(throwableMessage, importantThrowable);
                }

                if (importantThrowable instanceof Error) {
                    throw (Error) importantThrowable;
                } else if (importantThrowable instanceof RuntimeException) {
                    throw (RuntimeException) importantThrowable;
                } else {
                    throw new PageNotFoundException(message, importantThrowable);
                }
            }
        }

        logger.info("Checking mandatory elements done for: " + classSimpleName);
        logger.info("Page load successful: " + classSimpleName);
    }

    protected void checkPagePreparation() {
        // allow pages to run code before performing checkpage
    }

    protected void handleDemoMode(WebDriver webDriver) {

    }

    /**
     * Method entered when checkPage runs into an error (catching any throwable). You can throw a new throwable that
     * should be stacked onto the checkpage error (like new RuntimeException("bla", throwable) ).
     *
     * @param throwable .
     * @throws Throwable .
     */
    protected void checkPageErrorState(Throwable throwable) throws Throwable {
    }

    /**
     * Shortcut for throwing a TimeoutException.
     *
     * @param message .
     */
    @Deprecated
    public void exitWithTimeoutException(final String message) {
        throw new TimeoutException(message);
    }

    public int getElementTimeoutInSeconds() {
        return elementTimeoutInSeconds;
    }

    /**
     * Store a page class.
     * <p/>
     * The current page object is then stored thread safe and can be reloaded with a Page.restore(T) call, where T is a
     * class of expected page type T. If a correct object is stored, you will get it.
     */
    @Deprecated
    public void store() {
        STORED_PAGES.set(this);
    }

    private PageOptions getPageOptions(List<Class<? extends AbstractPage>> allClasses) {
        PageOptions pageOptions = null;
        for (Class<? extends AbstractPage> c : allClasses) {
            if (c.isAnnotationPresent(PageOptions.class)) {
                pageOptions = c.getAnnotation(PageOptions.class);
            }
        }
        return pageOptions;
    }

    /**
     * Gets all @Check annotated fields of a class and executes a webdriver find().
     *
     * @param findNot flag for "findNot" - verify fields are NOT shown
     * @param fast    Fast search (minimal timeout)
     */
    private void checkAnnotatedFields(final boolean findNot, final boolean fast) {
        List<Class<? extends AbstractPage>> allClasses = collectAllClasses(findNot);

        /*
        get and apply PageOptions
         */
        PageOptions pageOptions = getPageOptions(allClasses);
        if (pageOptions != null) {
            applyPageOptions(pageOptions);
        }

        List<FieldWithActionConfig> fields = getFields(allClasses, findNot, fast);
        List<FieldAction> fieldActions = getFieldActions(fields, this);

        Set<Field> fieldsMadeAccessible = makeFieldsAccessible(fields);

        for (FieldAction fieldAction : fieldActions) {
            fieldAction.run();
        }

        for (Field field : fieldsMadeAccessible) {
            field.setAccessible(false);
        }
    }

    private void applyPageOptions(PageOptions pageOptions) {
        if (pageOptions.elementTimeoutInSeconds() >= 0) {
            logger.info("Applying timeout value for this page object: " + pageOptions.elementTimeoutInSeconds() + "s");
            setElementTimeoutInSeconds(pageOptions.elementTimeoutInSeconds());
        }
    }

    abstract protected void addCustomFieldAction(FieldWithActionConfig field, List<FieldAction> fieldActions, AbstractPage declaringPage);

    private List<FieldAction> getFieldActions(List<FieldWithActionConfig> fields, AbstractPage declaringPage) {
        List<FieldAction> fieldActions = new ArrayList<>();
        for (FieldWithActionConfig field : fields) {
            GuiElementCheckFieldAction guiElementCheckFieldAction = new GuiElementCheckFieldAction(field, declaringPage);
            SetNameFieldAction setNameFieldAction = new SetNameFieldAction(field.field, declaringPage);

            /*
            Priority List!!
             */
            fieldActions.add(setNameFieldAction);
            fieldActions.add(new SetGuiElementTimeoutFieldAction(field.field, declaringPage));
            addCustomFieldAction(field, fieldActions, declaringPage);
            fieldActions.add(guiElementCheckFieldAction);
        }
        return fieldActions;
    }

    private Set<Field> makeFieldsAccessible(List<FieldWithActionConfig> fields) {
        Set<Field> fieldsMadeAccessible = new HashSet<Field>();
        for (FieldWithActionConfig fieldWithActionConfig : fields) {
            if (!fieldWithActionConfig.field.isAccessible()) {
                fieldWithActionConfig.field.setAccessible(true);
                fieldsMadeAccessible.add(fieldWithActionConfig.field);
            }
        }
        return fieldsMadeAccessible;
    }

    private List<FieldWithActionConfig> getFields(List<Class<? extends AbstractPage>> allClasses, boolean findNot, boolean fast) {
        ArrayList<FieldWithActionConfig> fieldToChecks = new ArrayList<FieldWithActionConfig>();
        for (final Class<? extends AbstractPage> cl : allClasses) {
            for (final Field field : cl.getDeclaredFields()) {
                fieldToChecks.add(new FieldWithActionConfig(field, findNot, fast, forcedGuiElementStandardAsserts));
            }
        }
        return fieldToChecks;
    }

    private List<Class<? extends AbstractPage>> collectAllClasses(boolean findNot) {
        final LinkedList<Class<? extends AbstractPage>> allClasses = new LinkedList<>();
        allClasses.add(this.getClass());

        /*
         * Find all classes in the class hierarchy.
         */
        if (!findNot) {
            /*
             * don't find superclasses when executing findNot
             */
            Class<?> clazz = this.getClass();
            boolean running = true;
            /*
             * Find all superclasses.
             */
            while (running) {
                clazz = clazz.getSuperclass();
                try {
                    if (clazz == null) {
                        running = false;
                    } else {
                        if (clazz != AbstractPage.class) {
                            @SuppressWarnings("unchecked")
                            final Class<? extends AbstractPage> pageClass = (Class<? extends AbstractPage>) clazz;
                            allClasses.add(pageClass);
                        }
                    }
                } catch (final ClassCastException e) {
                    running = false;
                }
            }
        }
          /*
        Revert classes order to bottom up
         */
        Collections.reverse(allClasses);
        return allClasses;
    }

    /**
     * @deprecated This method should not be public
     */
    @Deprecated
    public abstract void waitForPageToLoad();

    /**
     * Empty method to be overriden. Can perform some (additional) checks on page objects.
     */
    @Deprecated
    public void assertPageIsShown() {
    }

    @Deprecated
    public void assertPageIsNotShown() {
    }

    @Deprecated
    public WebDriver getDriver() {
        return getWebDriver();
    }

    @Override
    public WebDriver getWebDriver() {
        return driver;
    }
}
