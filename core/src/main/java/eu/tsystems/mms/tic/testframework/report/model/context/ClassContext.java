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
 *     erku <Eric.Kubenka@t-systems.com>
 *     pele <p.lehmann@t-systems.com>
 */
/*
 * Created on 25.01.2011
 *
 * Copyright(c) 2011 - 2011 T-Systems Multimedia Solutions GmbH
 * Riesaer Str. 5, 01129 Dresden
 * All rights reserved.
 */
package eu.tsystems.mms.tic.testframework.report.model.context;

import eu.tsystems.mms.tic.testframework.annotations.FennecClassContext;
import eu.tsystems.mms.tic.testframework.exceptions.FennecSystemException;
import eu.tsystems.mms.tic.testframework.report.FailureCorridor;
import eu.tsystems.mms.tic.testframework.report.TestStatusController;
import eu.tsystems.mms.tic.testframework.report.model.MethodType;
import eu.tsystems.mms.tic.testframework.report.utils.TestNGHelper;
import eu.tsystems.mms.tic.testframework.utils.ArrayUtils;
import org.testng.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds the informations of a test class.
 *
 * @author pele
 */
public class ClassContext extends Context implements SynchronizableContext {

    final List<MethodContext> methodContexts = new LinkedList<>();
    public String fullClassName;
    public String simpleClassName;
    public final String uuid = UUID.randomUUID().toString();
    public final TestContext testContext;
    public final RunContext runContext;
    public FennecClassContext FennecClassContext = null;
    public boolean merged = false;

    public ClassContext(TestContext testContext, RunContext runContext) {
        this.testContext = testContext;
        this.runContext = runContext;
    }

    public MethodContext findTestMethodContainer(String methodName) {
        return getContext(MethodContext.class, methodContexts, methodName, false, null);
    }

    public MethodContext getMethodContext(ITestResult testResult, ITestContext iTestContext, IInvokedMethod invokedMethod) {
        ITestNGMethod testMethod = TestNGHelper.getTestMethod(testResult, iTestContext, invokedMethod);
        String name = testMethod.getMethodName();

        synchronized (methodContexts) {
            List<MethodContext> collect = methodContexts.stream()
                    .filter(methodContext -> testResult == methodContext.testResult)
                    .collect(Collectors.toList());

            MethodContext methodContext;
            if (collect.isEmpty()) {
                /*
                create new one
                 */
                MethodType methodType;

                final boolean isTest;
                if (testResult.getMethod() != null) {
                    isTest = testResult.getMethod().isTest();
                }
                else if (invokedMethod != null) {
                    isTest = invokedMethod.isTestMethod();
                }
                else {
                    throw new FennecSystemException("Error getting method infos, seems like a TestNG bug.\n" + ArrayUtils.join(new Object[]{testResult, iTestContext},"\n"));
                }

                if (isTest) {
                    methodType = MethodType.TEST_METHOD;
                } else {
                    methodType = MethodType.CONFIGURATION_METHOD;
                }
                methodContext = new MethodContext(name, methodType, this, runContext);
                fillBasicContextValues(methodContext, name);
                methodContext.testResult = testResult;

                /*
                also check for annotations
                 */
                Method method = testMethod.getConstructorOrMethod().getMethod();
                if (method.isAnnotationPresent(FailureCorridor.High.class)) {
                    methodContext.failureCorridorValue = FailureCorridor.Value.High;
                } else if (method.isAnnotationPresent(FailureCorridor.Mid.class)) {
                    methodContext.failureCorridorValue = FailureCorridor.Value.Mid;
                } else if (method.isAnnotationPresent(FailureCorridor.Low.class)) {
                    methodContext.failureCorridorValue = FailureCorridor.Value.Low;
                }

                /*
                add to method contexts
                 */
                methodContexts.add(methodContext);
            }
            else {
                if (collect.size() > 1) {
                    LOGGER.error("INTERNAL ERROR: Found " + collect.size() + " " + MethodContext.class.getSimpleName() + "s with name " + name + ", picking first one");
                }
                methodContext = collect.get(0);
            }
            return methodContext;
        }
    }

    public List<MethodContext> copyOfMethodContexts() {
        synchronized (methodContexts) {
            return new LinkedList<>(methodContexts);
        }
    }

    public MethodContext safeAddSkipMethod(ITestResult testResult, IInvokedMethod invokedMethod) {
        MethodContext methodContext = getMethodContext(testResult, null, invokedMethod);
        methodContext.setThrowable(null, new SkipException("Skipped"));
        methodContext.status = TestStatusController.Status.SKIPPED;
        return methodContext;
    }

    @Override
    public TestStatusController.Status getStatus() {
        return getStatusFromContexts(getRepresentationalMethods());
    }

    public Context[] getRepresentationalMethods() {
        List<MethodContext> methodContexts = copyOfMethodContexts();
        Context[] contexts = methodContexts.stream().filter(MethodContext::isRepresentationalTestMethod).toArray(Context[]::new);
        return contexts;
    }

    public Map<TestStatusController.Status, Integer> getMethodStats(boolean includeTestMethods, boolean includeConfigMethods) {
        Map<TestStatusController.Status, Integer> counts = new LinkedHashMap<>();

        // initialize with 0
        Arrays.stream(TestStatusController.Status.values()).forEach(status -> counts.put(status, 0));

        List<MethodContext> methodContexts = copyOfMethodContexts();
        methodContexts.stream().filter(mc -> (includeTestMethods && mc.isTestMethod()) || (includeConfigMethods && mc.isConfigMethod())).forEach(methodContext -> {
            TestStatusController.Status status = methodContext.getStatus();
            int value = 0;
            if (counts.containsKey(status)) {
                value = counts.get(status);
            }

            counts.put(status, value + 1);
        });

        return counts;
    }

    public List<MethodContext> getTestMethodsWithStatus(TestStatusController.Status status) {
        List<MethodContext> methodContexts = new LinkedList<>();
        copyOfMethodContexts().forEach(methodContext -> {
            if (methodContext.isTestMethod() && status == methodContext.status) {
                methodContexts.add(methodContext);
            }
        });
        return methodContexts;
    }
}