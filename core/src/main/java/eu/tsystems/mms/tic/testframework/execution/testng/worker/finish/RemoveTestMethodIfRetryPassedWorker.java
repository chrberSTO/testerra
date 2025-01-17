/*
 * Testerra
 *
 * (C) 2020, Eric Kubenka, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
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

package eu.tsystems.mms.tic.testframework.execution.testng.worker.finish;

import com.google.common.eventbus.Subscribe;
import eu.tsystems.mms.tic.testframework.events.MethodEndEvent;
import eu.tsystems.mms.tic.testframework.report.TestStatusController;
import eu.tsystems.mms.tic.testframework.report.model.context.MethodContext;
import org.testng.ITestResult;

/**
 * RemoveTestMethodIfRetryPassedWorker
 * Remove retried method results if passed in second try to allow testng to execute testcases that depends on retried method
 * <p>
 * Date: 13.12.2019
 * Time: 11:13
 *
 * @author Eric Kubenka
 */
public class RemoveTestMethodIfRetryPassedWorker implements MethodEndEvent.Listener {

    @Subscribe
    @Override
    public void onMethodEnd(MethodEndEvent event) {
        MethodContext methodContext = event.getMethodContext();
        ITestResult testResult = event.getTestResult();

        if (testResult.isSuccess()) {
            if (methodContext.getStatus().equals(TestStatusController.Status.PASSED_RETRY)) {
                methodContext.readDependsOnMethodContexts()
                        .filter(dependsOnMethodContexts -> dependsOnMethodContexts.isSame(methodContext) && dependsOnMethodContexts.isRetry())
                        .forEach(dependsOnMethodContexts -> {
                            testResult.getTestContext().getFailedTests().removeResult(dependsOnMethodContexts.testResult);
                            testResult.getTestContext().getSkippedTests().removeResult(dependsOnMethodContexts.testResult);
                        });
            }
        }
    }
}
