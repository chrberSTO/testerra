/*
 * Testerra
 *
 * (C) 2020, Peter Lehmann, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
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
 package eu.tsystems.mms.tic.testframework.interop;

import eu.tsystems.mms.tic.testframework.report.model.context.Video;

import java.util.List;

public interface VideoCollector extends Collector {

    /**
     * @deprecated Use {@link #collectVideos()} instead
     */
    List<Video> getVideos();

    /**
     * Collects the videos and add them
     * @return
     */
    default List<Video> collectVideos() {
        return getVideos();
    }
}
