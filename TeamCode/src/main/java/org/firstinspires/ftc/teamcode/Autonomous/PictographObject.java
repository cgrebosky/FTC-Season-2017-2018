package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

public class PictographObject {
    VuforiaLocalizer vuforia;
    VuforiaTrackables relicTrackables;
    VuforiaTrackable relicTemplate;

    public void init( HardwareMap hardwareMap ) {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        // license key obtained 9/16/2017
        parameters.vuforiaLicenseKey = "AUp8513/////AAAAGffGlTeGS0y5jdCg+hqUsU5irSiMX77QAx1HWFN3/yECDlvELLP7Tn51vCzkd0SdX/hdnWEahxiezieBybAGrQ8R/5f5DOVAQPogRfMC5eanZGFDYxPhnMyuP0cicI+nH3MuQC0n2NxILVMRoH3CbA3ZfQjN6xfKppEDJiwh0WAEH4roArWTcrv3zb+HBPtbl32LUNylxhBZD0TfcGiYJtRp6rUsqRJME9NaTIEYBFURW0gBxbO1gvnm8+iR/v/ZTgQq9ZFfpAWg21zmzn8GlEHt1NcCOrFgHZxEn0Gab+CWRd2PXEb+H/QPDGsnVQM+UMeIydWLU26gVyLXZVoF+2eonG90FDA1cI2DapX+pJE4";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        parameters.useExtendedTracking = false;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        // this is done after waitForStart() in sample code
        relicTrackables.activate();
    }

    // Returns 1 (left), 2 (center), or 3 (right) if pictograph seen, else returns -1 if no pictograph detected
    public int getPictographId() {
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        if ( vuMark == RelicRecoveryVuMark.LEFT ) return 1;
        else if ( vuMark == RelicRecoveryVuMark.CENTER ) return 2;
        else if ( vuMark == RelicRecoveryVuMark.RIGHT ) return 3;
        else return -1; // no pictograph found
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }
}
