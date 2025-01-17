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
package eu.tsystems.mms.tic.testframework.report;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.constants.TesterraProperties;
import eu.tsystems.mms.tic.testframework.logging.Loggable;
import eu.tsystems.mms.tic.testframework.report.model.context.MethodContext;
import eu.tsystems.mms.tic.testframework.report.model.context.Screenshot;
import eu.tsystems.mms.tic.testframework.report.model.context.Video;
import eu.tsystems.mms.tic.testframework.report.utils.ExecutionContextController;
import eu.tsystems.mms.tic.testframework.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class DefaultReport implements Report, Loggable {

    private File REPORT_DIRECTORY;
    private final String BASE_DIR = PropertyManager.getProperty(TesterraProperties.REPORTDIR, "test-report");

    public DefaultReport() {
        FileUtils fileUtils = new FileUtils();
        REPORT_DIRECTORY = fileUtils.createTempDir(BASE_DIR);
        log().debug("Prepare report in " + REPORT_DIRECTORY.getAbsolutePath());
    }



    private File addFile(File sourceFile, File directory, FileMode fileMode) {
        try {
            switch (fileMode) {
                case COPY:
                    FileUtils.copyFileToDirectory(sourceFile, directory, true);
                    break;
                default:
                case MOVE:
                    FileUtils.moveFileToDirectory(sourceFile, directory, true);
                    break;
            }
        } catch (IOException e) {
            log().error(e.getMessage());
        }
        return new File(directory, sourceFile.getName());
    }

    public File finalizeReport() {
        File finalReportDirectory = new File(BASE_DIR);
        try {
            if (finalReportDirectory.exists()) {
                FileUtils.deleteDirectory(finalReportDirectory);
            }

            if (REPORT_DIRECTORY.exists()) {
                FileUtils.moveDirectory(REPORT_DIRECTORY, finalReportDirectory);
                REPORT_DIRECTORY = finalReportDirectory;
                log().info("Report written to " + finalReportDirectory.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not move report dir: " + e.getMessage(), e);
        }
        return finalReportDirectory;
    }

    private void addScreenshotFiles(Screenshot screenshot, FileMode fileMode) {
        File screenshotsDirectory = getReportDirectory(SCREENSHOTS_FOLDER_NAME);
        if (screenshot.getScreenshotFile() != null) {
            screenshot.setFile(addFile(screenshot.getScreenshotFile(), screenshotsDirectory, fileMode));
        }

        if (screenshot.getPageSourceFile() != null) {
            screenshot.setPageSourceFile(addFile(screenshot.getPageSourceFile(), screenshotsDirectory, fileMode));
        }
    }

    @Override
    public Report addScreenshot(Screenshot screenshot, FileMode fileMode) {
        addScreenshotFiles(screenshot, fileMode);
        return this;
    }

    @Override
    public Screenshot provideScreenshot(File file, FileMode fileMode) {
        Screenshot screenshot = new Screenshot(file, null);
        addScreenshotFiles(screenshot, fileMode);
        return screenshot;
    }

    @Override
    public Report addVideo(Video video, FileMode fileMode) {
        File videoDirectory = getReportDirectory(VIDEO_FOLDER_NAME);
        video.setFile(addFile(video.getVideoFile(), videoDirectory, fileMode));
        return this;
    }

    @Override
    public Video provideVideo(File file, FileMode fileMode)  {
        Video video = new Video(file);
        addVideo(video, fileMode);
        return video;
    }

    /**
     * @return Final report directory defined by the user
     */
    public File getReportDirectory() {
        return REPORT_DIRECTORY;
    }
    /**
     * @return Final report directory defined by the user
     */
    public File getFinalReportDirectory() {
        return new File(BASE_DIR);
    }

    @Override
    public String getRelativePath(File file) {
        return file.getAbsolutePath().replace(getReportDirectory().getAbsolutePath(), "");
    }
}
