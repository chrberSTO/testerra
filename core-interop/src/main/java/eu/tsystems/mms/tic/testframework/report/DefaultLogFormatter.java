/*
 * Testerra
 *
 * (C) 2020, Mike Beuthan, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
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
package eu.tsystems.mms.tic.testframework.report;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;

@Deprecated
public class DefaultLogFormatter implements LogFormatter {

    private final Layout layout;

    {
        PatternLayout.Builder builder = PatternLayout.newBuilder();
        builder.withPattern("%d{dd.MM.yyyy HH:mm:ss.SSS} [%t][%p]: %c{2} - %m");
        layout = builder.build();
    }

    @Override
    public String format(LogEvent event) {
        return new String(layout.toByteArray(event));
    }
}
