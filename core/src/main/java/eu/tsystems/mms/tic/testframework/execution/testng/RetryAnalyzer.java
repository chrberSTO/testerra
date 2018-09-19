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
package eu.tsystems.mms.tic.testframework.execution.testng;

import eu.tsystems.mms.tic.testframework.annotations.Fails;
import eu.tsystems.mms.tic.testframework.annotations.Retry;
import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.constants.FennecProperties;
import eu.tsystems.mms.tic.testframework.exceptions.TimeoutException;
import eu.tsystems.mms.tic.testframework.exceptions.FennecInheritedFailedException;
import eu.tsystems.mms.tic.testframework.exceptions.FennecSystemException;
import eu.tsystems.mms.tic.testframework.report.TestStatusController;
import eu.tsystems.mms.tic.testframework.report.utils.ExecutionUtils;
import eu.tsystems.mms.tic.testframework.report.model.context.MethodContext;
import eu.tsystems.mms.tic.testframework.report.utils.ExecutionContextController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IResultMap;
import org.testng.IRetryAnalyzer;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Testng Retry Analyzer.
 *
 * @author pele
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RetryAnalyzer.class);

    private static final List<AdditionalRetryAnalyzer> ADDITIONAL_RETRY_ANALYZERS = Collections.synchronizedList(new LinkedList<>());

    /**
     * Classes list.
     */
    private static final List<Class> CLASSES_LIST = new ArrayList<>();

    /**
     * Messages list.
     */
    private static final List<String> MESSAGES_LIST = new ArrayList<>();

    private static final List<MethodContext> RETRIED_METHODS = Collections.synchronizedList(new LinkedList<>());

    static {
        final String classes = PropertyManager.getProperty(FennecProperties.FAILED_TESTS_IF_THROWABLE_CLASSES);
        if (classes != null) {
            String[] split = classes.split(",");
            for (String clazz : split) {
                try {
                    Class<?> aClass = Class.forName(clazz.trim());
                    CLASSES_LIST.add(aClass);
                } catch (ClassNotFoundException e) {
                    LOGGER.error("Error finding class", e);
                }
            }
        }

        final String messages = PropertyManager.getProperty(FennecProperties.FAILED_TESTS_IF_THROWABLE_MESSAGES);
        if (messages != null) {
            String[] split = messages.split(",");
            for (String message : split) {
                MESSAGES_LIST.add(message.trim());
            }
        }
    }

    /**
     * The retry counter.
     */
    private static Map<String, Integer> retryCounters = Collections.synchronizedMap(new HashMap<String, Integer>());

    /**
     * The maximum number of retries. Loading every time to be able to set programmatically with System.setProperty().
     * A static final definition would be loaded to early (@ suite init).
     */
    private static int getMaxRetries(ITestResult testResult) {
        if (testResult != null) {
            // check for annotation
            Method method = testResult.getMethod().getConstructorOrMethod().getMethod();
            if (method.isAnnotationPresent(Retry.class)) {
                Retry retry = method.getAnnotation(Retry.class);
                return retry.maxRetries();
            }
        }

        return PropertyManager.getIntProperty(FennecProperties.FAILED_TESTS_MAX_RETRIES, 1);

    }

    @Override
    public boolean retry(final ITestResult testResult) {
        String retryReason = null;
        boolean retry = false;

        MethodContext methodContext = ExecutionContextController.getMethodContextFromTestResult(testResult);
        final String testMethodName = methodContext.getName();

        /*
        check retry counter
         */
        int retryCounter = getRetryCounter(testResult);
        final int maxRetries = getMaxRetries(testResult);
        final String retryMessageString = "(" + (retryCounter + 1) + "/" + (maxRetries + 1) + ")";

        if (retryCounter >= maxRetries) {
            raiseCounterAndChangeMethodName(testResult, maxRetries);
            LOGGER.warn("Not retrying " + testMethodName + " because run limit (" + maxRetries + ")");
            return false;
        }

        /*
         * no retry when FennecInheritedExceptions raise
         */
        final Throwable throwable1 = testResult.getThrowable();
        if (throwable1 != null && throwable1 instanceof FennecInheritedFailedException) {
            return false;
        }

        /*
        no retry for tests with expected Fails annotation
         */
        if (testResult.getMethod().getConstructorOrMethod().getMethod().isAnnotationPresent(Fails.class)) {
            LOGGER.warn("Not retrying this method, because test is @Fails annotated.");
            return false;
        }

        /*
        no retry for tests with fails annotaion in stacktrace
         */
        if (throwable1 != null && ExecutionUtils.getFailsAnnotationInStackTrace(throwable1.getStackTrace()) != null) {
            LOGGER.warn("Not retrying this method, because a method in stacktrace is @Fails annotated.");
            return false;
        }

        boolean containingFilteredThrowable = isTestResultContainingFilteredThrowable(testResult);
        if (containingFilteredThrowable) {
            /*
             * does the throwable filter match?
             */
            retryReason = "Found filtered throwable";
            retry = true;
        }

        /*
         * process retry
         */
        if (retry) {
            methodContext = raiseCounterAndChangeMethodName(testResult, maxRetries);

            Method realMethod = testResult.getMethod().getConstructorOrMethod().getMethod();
            // explicitly set status if it is not set atm
            TestStatusController.setMethodStatus(methodContext, TestStatusController.Status.FAILED_RETRIED, realMethod);

            // explicitly set status if it is not set atm
            if (methodContext.getThrowable() == null) {
                final Throwable throwable = testResult.getThrowable();
                final String message = "Retrying this method " + retryMessageString;
                if (throwable != null) {
                    methodContext.setThrowable(message, throwable);
                } else {
                    methodContext.setThrowable(message, new FennecSystemException(message));
                }
            }

            RETRIED_METHODS.add(methodContext);

            LOGGER.error(retryReason + ", send signal for retrying the test " + retryMessageString +
                    "\n" + methodContext);

            IResultMap skippedTests = testResult.getTestContext().getFailedTests();
            skippedTests.removeResult(testResult);
            return true;
        }

        // Do not retry
        return false;
    }

    private MethodContext raiseCounterAndChangeMethodName(final ITestResult testResult, final int maxRetries) {
        // raise counter
        int retryCounter = raiseRetryCounter(testResult);

        // reset method name
        MethodContext methodContext = ExecutionContextController.getMethodContextFromTestResult(testResult);
        if (maxRetries > 0) {

            String retryLog = "(" + retryCounter + "/" + (maxRetries + 1) + ")";

            /*
             * Rename originally executed method in report and set evaluate flag to false for this method.
             */
            String testMethodName = methodContext.getName();
            testMethodName += " " + retryLog;
            methodContext.name = testMethodName;
        }

        return methodContext;
    }

    private static int raiseRetryCounter(ITestResult iTestResult) {
        return getRetryCounter(iTestResult, true);
    }

    private static int getRetryCounter(ITestResult iTestResult) {
        return getRetryCounter(iTestResult, false);
    }

    private static int getRetryCounter(ITestResult testResult, boolean raise) {
        ITestNGMethod testNGMethod = testResult.getMethod();
        Method method = testNGMethod.getConstructorOrMethod().getMethod();

        String id = testResult.getTestContext().getCurrentXmlTest().getName() + "/" +
                testNGMethod.getRealClass().getName() + "." +
                testNGMethod.getMethodName();

        Object[] parameters = testResult.getParameters();
        if (parameters != null && parameters.length > 0) {
            for (Object parameter : parameters) {
                id += "#" + parameter.toString();
            }
        }

        if (!retryCounters.containsKey(id)) {
            retryCounters.put(id, 0);
        }

        // raise
        Integer counter = retryCounters.get(id);
        if (raise) {
            counter++;
            retryCounters.remove(id);
            retryCounters.put(id, counter);
        }
        LOGGER.debug("Retry counter = " + counter + " for " + id);
        return counter;
    }

    /**
     * checks if test results contain throwable results
     *
     * @param testResult .
     * @return .
     */
    public static boolean isTestResultContainingFilteredThrowable(final ITestResult testResult) {
        if (testResult.getStatus() != ITestResult.FAILURE) {
            return false;
        }
        if (!testResult.isSuccess()) {
            Throwable throwable = testResult.getThrowable();

            if (throwable == null) {
                return false;
            }

            Throwable retryCause = checkThrowable(throwable);

            if (retryCause != null) {
                MethodContext methodContext = ExecutionContextController.getMethodContextFromTestResult(testResult);
                if (methodContext != null) {
                    methodContext.addPriorityMessage("Retry Cause:\n" + retryCause.toString());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a retryCause when a stacktrace match was found, otherwise null, which means, that we do not want a retry.
     * @param throwable The throwable to check.
     * @return a cause or null
     */
    private static Throwable checkThrowable(Throwable throwable) {
        /*
         * Make a list of throwables
         */
        List<Throwable> throwables = new ArrayList<Throwable>();
        throwables.add(throwable);
        Throwable cause = throwable.getCause();
        while (cause != null) {
            throwables.add(cause);
            cause = cause.getCause();
        }

        Throwable retryCause = null;
        for (Throwable t : throwables) {
            String tMessage = t.getMessage();

            if (t instanceof TimeoutException) {
                if (tMessage != null) {
                    if (tMessage.contains("Timed out waiting for page load")) {
                        retryCause = t;
                    }
                }
            }

            if (retryCause == null) {
                for (AdditionalRetryAnalyzer additionalRetryAnalyzer : ADDITIONAL_RETRY_ANALYZERS) {
                    if (retryCause == null) {
                        retryCause = additionalRetryAnalyzer.analyzeThrowable(t, tMessage);
                    }
                }
            }

            for (Class aClass : CLASSES_LIST) {
                if (t.getClass() == aClass) {
                    LOGGER.info("Retrying test because of: " + aClass);
                    retryCause = t;
                }
            }

            for (String message : MESSAGES_LIST) {
                if (tMessage != null && tMessage.contains(message)) {
                    LOGGER.info("Retrying test because of: " + message);
                    retryCause = t;
                }
            }
        }

        return retryCause;
    }

    public static List<MethodContext> getRetriedMethods() {
        return RETRIED_METHODS;
    }

    public static void registerAdditionalRetryAnalyzer(AdditionalRetryAnalyzer additionalRetryAnalyzer) {
        ADDITIONAL_RETRY_ANALYZERS.add(additionalRetryAnalyzer);
    }
}