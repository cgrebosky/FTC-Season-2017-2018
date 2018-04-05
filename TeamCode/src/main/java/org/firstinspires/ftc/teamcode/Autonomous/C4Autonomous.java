package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous(name="C4Autonomous")
public class C4Autonomous extends LinearOpMode {

    DcMotor FLMotor, BLMotor, FRMotor, BRMotor, liftMotor1, liftMotor2;
    Servo servoArm, servoFlick, servoHoldR, servoHoldL, servoGlyphPan, glyphCollectorTop;
    ColorSensor sensorL, sensorR;
    TankDrive TD;
    FlickerController FC;
    PictographObject PC;
    DigitalChannel touchSensor;


    //Values taken from prop file
    private int[] COLUMN1, OFFSET2, OFFSET3, TURN, TOFFSET2, TOFFSET3, APPROACH1, BACK1, APPROACH2, FORWARD, TURN1, COLUMN1_2, OFFSET2_2, OFFSET3_2, TURN2, APPROACH_2, BACK1_2, APPROACH2_2;
    private double GLYPH_PAN_UP, GLYPH_PAN_MID, GLYPH_PAN_DOWN, GLYPH_PAN_FLAT, GLYPH_TOP_OUT;
    private double R_INIT, L_INIT, RELEASE;

    //Set by controller
    private int team = 0;
    private int pos = 1;

    final int BACK2 = 225;
    final int BLUE = 0;
    final int RED = 1;

    @Override
    public void runOpMode() {
        initPropVars();
        initHardware();
        initSubSystems();
        initServoPos();
        setControllerInputVars();
        moveLiftToDownPosition();

        FC.setRedTeam(team == RED);
        logControllerValues();

        waitForStart();

        int column = PC.getPictographId();
        if (team == 1 && column == 1) column = 3;
        else if (team == 1 && column == 3) column = 1;
        Trace.log("****************** Pic ID (column) = " + column + ", (1 and 3 reversed on red side)");

        // make sure pusher doesn't interfere with glyph pan when tilted
        glyphCollectorTop.setPosition(GLYPH_TOP_OUT);

        // move lift up about an inch so doesn't hit floor when coming off stone
        liftMotor1.setPower(0.5);
        liftMotor2.setPower(0.5);
        sleep(500);
        liftMotor1.setPower(0);
        liftMotor2.setPower(0);
        sleep(500);

        FC.completeArmMovement();
        sleep(500);

        if (!opModeIsActive()) return;

        if (pos == 1 && team == BLUE) {
            if (column == 1 || column == -1) {
                TD.up(COLUMN1[BLUE]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.left(TURN[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                moveLiftToDownPosition();
                sleep(250);
                TD.up(APPROACH1[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_MID);
                servoHoldR.setPosition(RELEASE);
                servoHoldL.setPosition(RELEASE);
                sleep(250);
                TD.down(BACK1[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_UP);
                sleep(250);
                TD.up(APPROACH2[BLUE]);
                if (!opModeIsActive()) return;
            } else if (column == 2) {
                TD.up(COLUMN1[BLUE] + OFFSET2[BLUE]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.left(TURN[BLUE] + TOFFSET2[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                moveLiftToDownPosition();
                sleep(250);
                TD.up(APPROACH1[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_MID);
                servoHoldR.setPosition(RELEASE);
                servoHoldL.setPosition(RELEASE);
                sleep(250);
                TD.down(BACK1[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_UP);
                sleep(250);
                TD.up(APPROACH2[BLUE]);
                if (!opModeIsActive()) return;
            } else if (column == 3) {
                TD.up(COLUMN1[BLUE] + OFFSET3[BLUE]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.left(TURN[BLUE] + TOFFSET3[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                moveLiftToDownPosition();
                sleep(250);
                TD.up(APPROACH1[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_MID);
                servoHoldR.setPosition(RELEASE);
                servoHoldL.setPosition(RELEASE);
                sleep(250);
                TD.down(BACK1[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_UP);
                if (!opModeIsActive()) return;
                sleep(250);
                TD.up(APPROACH2[BLUE]);
                if (!opModeIsActive()) return;
            }
        } else if (pos == 1 && team == RED) {
            if (column == 1 || column == -1) {
                TD.down(COLUMN1[RED]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.left(TURN[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                moveLiftToDownPosition();
                sleep(250);
                TD.up(APPROACH1[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_MID);
                servoHoldR.setPosition(RELEASE);
                servoHoldL.setPosition(RELEASE);
                sleep(250);
                TD.down(BACK1[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_UP);
                sleep(250);
                TD.up(APPROACH2[RED]);
                if (!opModeIsActive()) return;
            } else if (column == 2) {
                TD.down(COLUMN1[RED] + OFFSET2[RED]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.left(TURN[RED] + TOFFSET2[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                moveLiftToDownPosition();
                sleep(250);
                TD.up(APPROACH1[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_MID);
                servoHoldR.setPosition(RELEASE);
                servoHoldL.setPosition(RELEASE);
                sleep(250);
                TD.down(BACK1[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_UP);
                sleep(250);
                TD.up(APPROACH2[RED]);
                if (!opModeIsActive()) return;
            } else if (column == 3) {
                TD.down(COLUMN1[RED] + OFFSET3[RED]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.left(TURN[RED] + TOFFSET3[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                moveLiftToDownPosition();
                sleep(250);
                TD.up(APPROACH1[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_MID);
                servoHoldR.setPosition(RELEASE);
                servoHoldL.setPosition(RELEASE);
                sleep(250);
                TD.down(BACK1[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_UP);
                if (!opModeIsActive()) return;
                sleep(250);
                TD.up(APPROACH2[RED]);
                if (!opModeIsActive()) return;
            }
        } else if (pos == 2 && team == BLUE) {
            if (column == 1 || column == -1) {
                TD.up(FORWARD[BLUE]);
                if (!opModeIsActive()) return;
                sleep(1000);
                TD.right(TURN1[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                moveLiftToDownPosition();
                sleep(250);
                TD.up(COLUMN1_2[BLUE]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.left(TURN2[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                TD.up(APPROACH_2[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_MID);
                servoHoldR.setPosition(RELEASE);
                servoHoldL.setPosition(RELEASE);
                sleep(250);
                TD.down(BACK1_2[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_UP);
                sleep(250);
                TD.up(APPROACH2_2[BLUE]);
                if (!opModeIsActive()) return;
            } else if (column == 2) {
                TD.up(FORWARD[BLUE]);
                if (!opModeIsActive()) return;
                sleep(1000);
                TD.right(TURN1[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                moveLiftToDownPosition();
                sleep(250);
                TD.up(COLUMN1_2[BLUE] + OFFSET2_2[BLUE]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.left(TURN2[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                TD.up(APPROACH_2[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_MID);
                servoHoldR.setPosition(RELEASE);
                servoHoldL.setPosition(RELEASE);
                sleep(250);
                TD.down(BACK1_2[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_UP);
                if (!opModeIsActive()) return;
                sleep(250);
                TD.up(APPROACH2_2[BLUE]);
                if (!opModeIsActive()) return;
            } else if (column == 3) {
                TD.up(FORWARD[BLUE]);
                if (!opModeIsActive()) return;
                sleep(1000);
                TD.right(TURN1[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                moveLiftToDownPosition();
                sleep(250);
                TD.up(COLUMN1_2[BLUE] + OFFSET3_2[BLUE]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.left(TURN2[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                TD.up(APPROACH_2[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_MID);
                servoHoldR.setPosition(RELEASE);
                servoHoldL.setPosition(RELEASE);
                sleep(250);
                TD.down(BACK1_2[BLUE]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_UP);
                sleep(250);
                TD.up(APPROACH2_2[BLUE]);
                if (!opModeIsActive()) return;
            }
        } else if (pos == 2 && team == RED) {
            if (column == 1 || column == -1) {
                TD.down(FORWARD[RED]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.right(TURN1[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                moveLiftToDownPosition();
                sleep(250);
                TD.up(COLUMN1_2[RED]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.right(TURN2[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                TD.up(APPROACH_2[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_MID);
                servoHoldR.setPosition(RELEASE);
                servoHoldL.setPosition(RELEASE);
                sleep(250);
                TD.down(BACK1_2[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_UP);
                sleep(250);
                TD.up(APPROACH2_2[RED]);
                if (!opModeIsActive()) return;
            } else if (column == 2) {
                TD.down(FORWARD[RED]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.right(TURN1[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                moveLiftToDownPosition();
                sleep(250);
                TD.up(COLUMN1_2[RED] + OFFSET2_2[RED]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.right(TURN2[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                TD.up(APPROACH_2[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_MID);
                servoHoldR.setPosition(RELEASE);
                servoHoldL.setPosition(RELEASE);
                sleep(250);
                TD.down(BACK1_2[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_UP);
                sleep(250);
                TD.up(APPROACH2_2[RED]);
                if (!opModeIsActive()) return;
            } else if (column == 3) {
                TD.down(FORWARD[RED]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.right(TURN1[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                moveLiftToDownPosition();
                sleep(250);
                TD.up(COLUMN1_2[RED] + OFFSET3_2[RED]);
                if (!opModeIsActive()) return;
                sleep(500);
                TD.right(TURN2[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                TD.up(APPROACH_2[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_MID);
                servoHoldR.setPosition(RELEASE);
                servoHoldL.setPosition(RELEASE);
                sleep(250);
                TD.down(BACK1_2[RED]);
                if (!opModeIsActive()) return;
                sleep(250);
                servoGlyphPan.setPosition(GLYPH_PAN_UP);
                sleep(250);
                TD.up(APPROACH2_2[RED]);
                if (!opModeIsActive()) return;
            }
        }
        sleep(200);
        TD.down(BACK2);
        if (!opModeIsActive()) return;
        sleep(200);

        moveLiftToDownPosition();
        servoGlyphPan.setPosition(GLYPH_PAN_DOWN);

        telemetry.addData("Stone ", pos);
        telemetry.addData("Team Color", team == 0 ? "Blue" : "Red");
        telemetry.addData("Column ID", column);
        telemetry.update();

        // wait 20 seconds before exiting for telemetry info to remain displayed on driver station
        for (int i = 0; i < 20; i++) {
            if (!opModeIsActive()) return;
            sleep(1000);
        }
    }

    private void moveLiftToDownPosition() {
        double LIFT_DOWN_TIMEOUT_MS = 1000;
        double startTime = System.currentTimeMillis();
        while (!touchSensor.getState() && (System.currentTimeMillis() - startTime < LIFT_DOWN_TIMEOUT_MS)) {
            liftMotor1.setPower(-0.5);
            liftMotor2.setPower(-0.5);
        }
        liftMotor1.setPower(0);
        liftMotor2.setPower(0);
    }

    private void initPropVars() {
        C4PropFile.loadPropFile();

        // Declarations for stone 1 {Team Blue, Team Red}
        COLUMN1 = new int[]{C4PropFile.getInt("COLUMN1BLUE1"), C4PropFile.getInt("COLUMN1RED1")};
        OFFSET2 = new int[]{C4PropFile.getInt("OFFSET2BLUE1"), C4PropFile.getInt("OFFSET2RED1")};
        OFFSET3 = new int[]{C4PropFile.getInt("OFFSET3BLUE1"), C4PropFile.getInt("OFFSET3RED1")};
        TURN = new int[]{C4PropFile.getInt("TURN1BLUE1"), C4PropFile.getInt("TURN1RED1")};
        TOFFSET2 = new int[]{C4PropFile.getInt("TOFFSET2BLUE1"), C4PropFile.getInt("TOFFSET2RED1")};
        TOFFSET3 = new int[]{C4PropFile.getInt("TOFFSET3BLUE1"), C4PropFile.getInt("TOFFSET3RED1")};
        APPROACH1 = new int[]{C4PropFile.getInt("APPROACHBLUE1"), C4PropFile.getInt("APPROACHRED1")};
        BACK1 = new int[]{C4PropFile.getInt("BACKBLUE1"), C4PropFile.getInt("BACKRED1")};
        APPROACH2 = new int[]{C4PropFile.getInt("APPROACH2BLUE1"), C4PropFile.getInt("APPROACH2RED1")};

        //Declarations for stone 2 {Team Blue, Team Red}
        FORWARD = new int[]{C4PropFile.getInt("FORWARDBLUE2"), C4PropFile.getInt("FORWARDRED2")};
        TURN1 = new int[]{C4PropFile.getInt("TURN1BLUE2"), C4PropFile.getInt("TURN1RED2")};
        COLUMN1_2 = new int[]{C4PropFile.getInt("COLUMN1BLUE2"), C4PropFile.getInt("COLUMN1RED2")};
        OFFSET2_2 = new int[]{C4PropFile.getInt("OFFSET2BLUE2"), C4PropFile.getInt("OFFSET2RED2")};
        OFFSET3_2 = new int[]{C4PropFile.getInt("OFFSET3BLUE2"), C4PropFile.getInt("OFFSET3RED2")};
        TURN2 = new int[]{C4PropFile.getInt("TURN2BLUE2"), C4PropFile.getInt("TURN2RED2")};
        APPROACH_2 = new int[]{C4PropFile.getInt("APPROACHBLUE2"), C4PropFile.getInt("APPROACHRED2")};
        BACK1_2 = new int[]{C4PropFile.getInt("BACKBLUE2"), C4PropFile.getInt("BACKRED2")};
        APPROACH2_2 = new int[]{C4PropFile.getInt("APPROACH2BLUE2"), C4PropFile.getInt("APPROACH2RED2")};

        //Glyph pan positions
        GLYPH_PAN_UP = C4PropFile.getDouble("GLYPH_PAN_UP");
        GLYPH_PAN_MID = C4PropFile.getDouble("GLYPH_PAN_MID");
        GLYPH_PAN_DOWN = C4PropFile.getDouble("GLYPH_PAN_DOWN");
        GLYPH_PAN_FLAT = C4PropFile.getDouble("GLYPH_PAN_FLAT");

        // Glyph pusher position
        GLYPH_TOP_OUT = C4PropFile.getDouble("GLYPH_TOP_OUT");

        // Glyph holder positions
        R_INIT = C4PropFile.getDouble("GLYPH_HOLDER_RIGHT_INIT");
        L_INIT = C4PropFile.getDouble("GLYPH_HOLDER_LEFT_INIT");
        RELEASE = C4PropFile.getDouble("GLYPH_HOLDER_RELEASE");
        ;

    }

    private void initHardware() {
        FLMotor = hardwareMap.dcMotor.get("motor_fl");
        BLMotor = hardwareMap.dcMotor.get("motor_bl");
        FRMotor = hardwareMap.dcMotor.get("motor_fr");
        BRMotor = hardwareMap.dcMotor.get("motor_br");
        liftMotor1 = hardwareMap.dcMotor.get("motor_lift1");
        liftMotor2 = hardwareMap.dcMotor.get("motor_lift2");
        servoArm = hardwareMap.servo.get("servo_arm");
        servoFlick = hardwareMap.servo.get("servo_flick");
        servoHoldR = hardwareMap.servo.get("servo_holder_right");
        servoHoldL = hardwareMap.servo.get("servo_holder_left");
        servoGlyphPan = hardwareMap.servo.get("servo_glyph_pan");
        sensorL = hardwareMap.colorSensor.get("color_jewelL");
        sensorR = hardwareMap.colorSensor.get("color_jewelR");
        glyphCollectorTop = hardwareMap.servo.get("servo_glyphCollectorTop");
        touchSensor = hardwareMap.get(DigitalChannel.class, "touch_limit");
        touchSensor.setMode(DigitalChannel.Mode.INPUT);

        FLMotor.setDirection(DcMotor.Direction.REVERSE);
        BLMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    private void initSubSystems() {
        TD = new TankDrive(FLMotor, BLMotor, FRMotor, BRMotor, servoArm, servoFlick, this);
        PC = new PictographObject();
        PC.init(hardwareMap);
        FC = new FlickerController(sensorL, sensorR, servoArm, servoFlick, this);
        FC.init();
    }

    private void initServoPos() {
        servoHoldR.setPosition(R_INIT);
        servoHoldL.setPosition(L_INIT);
        servoGlyphPan.setPosition(GLYPH_PAN_FLAT);
    }

    private void setControllerInputVars() {
        boolean lastA = false;
        boolean lastB = false;
        while (!gamepad1.x) {
            boolean a = gamepad1.a;
            boolean b = gamepad1.b;

            if (a && !lastA)
                team = team == 0 ? 1 : 0;
            if (b && !lastB)
                pos = pos == 1 ? 2 : 1;

            lastA = a;
            lastB = b;

            telemetry.addData("Stone ", pos + "   ('B' to toggle)");
            telemetry.addData("Team", team == BLUE ? "Blue" : "Red" + "   ('A' to toggle)");
            telemetry.addData("Press 'X' to continue", " ");
            telemetry.update();
        }
    }

    private void logControllerValues() {
        Trace.log("************************* INPUTS ****************************");
        if (team == 0) Trace.log("****************** Team = Blue");
        else Trace.log("****************** Team = Red");
        Trace.log("****************** Stone = " + pos);

        telemetry.addData("Stone ", pos);
        if (team == 0) telemetry.addData("Team ", "Blue");
        else telemetry.addData("Team ", "Red");
        telemetry.addData("READY TO START", "");
        telemetry.update();
    }
}