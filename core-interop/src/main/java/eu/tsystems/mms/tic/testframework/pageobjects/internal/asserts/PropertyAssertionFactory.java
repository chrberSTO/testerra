/*
 * Testerra
 *
 * (C) 2020, Mike Reiche, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
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
 */

package eu.tsystems.mms.tic.testframework.pageobjects.internal.asserts;

/**
 * Creates preconfigured generic property assertions
 * @author Mike Reiche
 */
public interface PropertyAssertionFactory {
    <ASSERTION extends AbstractPropertyAssertion, TYPE> ASSERTION create(
        Class<ASSERTION> assertionClass,
        AbstractPropertyAssertion parentAssertion,
        AssertionProvider<TYPE> provider
    );
    default <ASSERTION extends AbstractPropertyAssertion, TYPE> ASSERTION create(
        Class<ASSERTION> assertionClass,
        AssertionProvider<TYPE> provider
    ) {
        return create(assertionClass, null, provider);
    }
    PropertyAssertionFactory setThrowErrors(boolean throwErrors);
}
