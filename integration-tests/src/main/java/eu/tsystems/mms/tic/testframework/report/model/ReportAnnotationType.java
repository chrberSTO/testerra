package eu.tsystems.mms.tic.testframework.report.model;

/**
 * Created by fakr on 30.08.2017
 */
public enum ReportAnnotationType {

    SUPPORT_METHOD("Support Method", "SupportMethod"),
    NEW("New", "New"),
    READY_FOR_APPROVAL("Ready For Approval", "ReadyForApproval");

    private String annotationName;

    private String shortcut;

    ReportAnnotationType(String annotationName, String shortcut) {
        this.annotationName = annotationName;
        this.shortcut = shortcut;
    }

    public String getAnnotationDisplayedName() {
        return this.annotationName;
    }

    public String getTestMethodShortcut() {
        return this.shortcut;
    }
}