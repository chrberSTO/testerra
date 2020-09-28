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

import eu.tsystems.mms.tic.testframework.common.Testerra;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.utils.Formatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default implementation of {@link StringAssertion}
 * @author Mike Reiche
 */
public class DefaultStringAssertion<T> extends DefaultQuantityAssertion<T> implements StringAssertion<T>, Loggable {

    private final static Formatter formatter = Testerra.injector.getInstance(Formatter.class);

    public DefaultStringAssertion(AbstractPropertyAssertion parentAssertion, AssertionProvider<T> provider) {
        super(parentAssertion, provider);
    }

    @Override
    public boolean contains(String expected, String failMessage) {
        return testSequence(
                provider,
                (actual) -> assertion.contains(actual.toString(), expected),
                (actual) -> assertion.formatExpectContains(actual.toString(), expected, createFailMessage(failMessage))
        );
    }

    @Override
    public boolean containsNot(String expected, String failMessage) {
        return testSequence(
                provider,
                (actual) -> assertion.containsNot(actual.toString(), expected),
                (actual) -> assertion.formatExpectContainsNot(actual.toString(), expected, createFailMessage(failMessage))
        );
    }

    @Override
    public boolean startsWith(String expected, String failMessage) {
        return testSequence(
                provider,
                (actual) -> assertion.startsWith(actual, expected),
                (actual) -> assertion.formatExpectStartsWith(actual, expected, createFailMessage(failMessage))
        );
    }

    @Override
    public boolean endsWith(String expected, String failMessage) {
        return testSequence(
                provider,
                (actual) -> assertion.endsWith(provider.getActual(), expected),
                (actual) -> assertion.formatExpectEndsWith(provider.getActual(), expected, createFailMessage(failMessage))
        );
    }

    @Override
    public PatternAssertion matches(Pattern pattern) {
        return propertyAssertionFactory.createWithParent(DefaultPatternAssertion.class, this, new AssertionProvider<Matcher>() {
            @Override
            public Matcher getActual() {
                return pattern.matcher(provider.getActual().toString());
            }

            @Override
            public String getSubject() {
                return String.format("\"%s\".matches(pattern: %s)", getStringSubject(), pattern.toString());
            }
        });
    }

    @Override
    public BinaryAssertion<Boolean> containsWords(String... words) {
        final String wordsList = String.join("|", words);
        final Pattern wordsPattern = Pattern.compile("\\b(" + wordsList + ")\\b", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        return propertyAssertionFactory.createWithParent(DefaultBinaryAssertion.class, this, new AssertionProvider<Boolean>() {
            @Override
            public Boolean getActual() {
                int found = 0;
                Matcher matcher = wordsPattern.matcher(provider.getActual().toString());
                while (matcher.find()) found++;
                return found >= words.length;
            }

            @Override
            public String getSubject() {
                return String.format("\"%s\".containsWords(%s)", getStringSubject(), wordsList);
            }
        });
    }

    private String getStringSubject() {
        return formatter.cutString(provider.getActual().toString(), 30);
    }

    @Override
    public QuantityAssertion<Integer> length() {
        return propertyAssertionFactory.createWithParent(DefaultQuantityAssertion.class, this, new AssertionProvider<Integer>() {
            @Override
            public Integer getActual() {
                return provider.getActual().toString().length();
            }

            @Override
            public String getSubject() {
                return String.format("\"%s\".length", getStringSubject());
            }
        });
    }
}
