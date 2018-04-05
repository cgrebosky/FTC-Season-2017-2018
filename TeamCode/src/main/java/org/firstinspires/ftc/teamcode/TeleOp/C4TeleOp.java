package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Autonomous.C4PropFile;

@TeleOp(name="C4TeleOp")
public class C4TeleOp extends LinearOpMode {

    double GLYPH_PAN_UP, GLYPH_PAN_MID, GLYPH_PAN_DOWN, GLYPH_PAN_FLAT;
    double GLYPH_TOP_OUT, GLYPH_TOP_IN;
    double RELEASE;
    double COLLECTOR_CLOCKWISE, COLLECTOR_COUNTERCLOCKWISE, COLLECTOR_STOP;
    double FLICKER_RIGHT, ARM_UP;

    DcMotor FLMotor, BLMotor, FRMotor, BRMotor, liftMotor1, liftMotor2;
    Servo servoArm, servoFlick, servoGlyphPan, glyphCollectorL, glyphCollectorR, glyphCollectorTop, servoHoldR, servoHoldL;
    RelicController RC;
    DigitalChannel touchSensor;
    double pos;
    boolean aFlag = false;

    /**
     * Gamepad1 Controls:<br/>
     * 
     * a: Toggle glyph pan up/down positions<br/>
     * x: Set glyph pan to mid<br/>
     * y: Toggle glyph top in/out positions<br/>
     * left_stick_y: Control left wheel<br/>
     * right_stick_y: Control right wheel<br/>
     * left_bumper: Collect glyph<br/>
     * right_bumper: push out glyph<br/>
     * left_trigger: Lower lift<br/>
     * right_trigger: Raise lift<br/>
     * left_stick_y: Control left wheels<br/>
     * right_stick_y: Control right wheels<br/>
     * 
     * 
     * Gamepad2 Controls:<br/>
     * y: Push in collectorTop servo<br/>
     * a: Toggle x in/out position<br/>
     * b: Reset y position<br/>
     * right_stick_y: Move along Y-axis<br/>
     * left_stick_x: Move along X-axis<br/>
     * right_trigger: Extend arm<br/>
     * left_trigger: Retract arm<br/>
     * right_bumper: Toggle claw open/close<br/>
     *
     */
    @Override
    public void runOpMode() {
        initPropVars();
        initHardware();

        waitForStart();

        initServoPositions();
        while (opModeIsActive()) {
            controlTD();
            controlLift();
            controlGlyphPan();
            controlCollector();
            RC.control();
        }

        // Put this here to try to prevent glyph collector servos from running when teleop stops (happens intermittently)
        glyphCollectorR.setPosition(COLLECTOR_STOP);
        glyphCollectorL.setPosition(COLLECTOR_STOP);
    }

    private void initHardware() {
        FLMotor         = hardwareMap.dcMotor.get("motor_fl");
        BLMotor         = hardwareMap.dcMotor.get("motor_bl");
        FRMotor         = hardwareMap.dcMotor.get("motor_fr");
        BRMotor         = hardwareMap.dcMotor.get("motor_br");
        liftMotor1      = hardwareMap.dcMotor.get("motor_lift1");
        liftMotor2      = hardwareMap.dcMotor.get("motor_lift2");
        servoArm        = hardwareMap.servo.get("servo_arm");
        servoFlick      = hardwareMap.servo.get("servo_flick");
        servoGlyphPan   = hardwareMap.servo.get("servo_glyph_pan");
        glyphCollectorL = hardwareMap.servo.get("servo_glyphCollectorL");
        glyphCollectorR = hardwareMap.servo.get("servo_glyphCollectorR");
        glyphCollectorTop = hardwareMap.servo.get("servo_glyphCollectorTop");
        servoHoldR      = hardwareMap.servo.get("servo_holder_right");
        servoHoldL      = hardwareMap.servo.get("servo_holder_left");
        touchSensor     = hardwareMap.get(DigitalChannel.class, "touch_limit");

        touchSensor.setMode(DigitalChannel.Mode.INPUT);
        FLMotor.setDirection(DcMotor.Direction.REVERSE);
        BLMotor.setDirection(DcMotor.Direction.REVERSE);
        FLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BLMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BRMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RC = new RelicController(gamepad2,telemetry);
        RC.init(hardwareMap);
    }

    private void initServoPositions() {
        servoArm.setPosition(ARM_UP);
        servoFlick.setPosition(FLICKER_RIGHT);
        servoGlyphPan.setPosition(GLYPH_PAN_DOWN);
        glyphCollectorR.setPosition(COLLECTOR_STOP);
        glyphCollectorL.setPosition(COLLECTOR_STOP);
        glyphCollectorTop.setPosition(GLYPH_TOP_OUT);
        servoHoldR.setPosition(RELEASE);
        servoHoldL.setPosition(RELEASE);
    }

    private void initPropVars() {
        C4PropFile.loadPropFile();
        GLYPH_PAN_UP = C4PropFile.getDouble("GLYPH_PAN_UP");
        GLYPH_PAN_MID = C4PropFile.getDouble("GLYPH_PAN_MID");
        GLYPH_PAN_DOWN = C4PropFile.getDouble("GLYPH_PAN_DOWN");
        pos = GLYPH_PAN_DOWN;
        GLYPH_PAN_FLAT = C4PropFile.getDouble("GLYPH_PAN_FLAT");
        GLYPH_TOP_IN = C4PropFile.getDouble("GLYPH_TOP_IN");
        GLYPH_TOP_OUT = C4PropFile.getDouble("GLYPH_TOP_OUT");
        RELEASE = C4PropFile.getDouble("GLYPH_HOLDER_RELEASE");
        COLLECTOR_CLOCKWISE = C4PropFile.getDouble("COLLECTOR_CLOCKWISE");
        COLLECTOR_COUNTERCLOCKWISE  = C4PropFile.getDouble("COLLECTOR_COUNTERCLOCKWISE");
        COLLECTOR_STOP = C4PropFile.getDouble("COLLECTOR_STOP");
        FLICKER_RIGHT = C4PropFile.getDouble("FLICKER_RIGHT");
        ARM_UP = C4PropFile.getDouble("ARM_UP");
    }

    private void controlTD() {
        double Lpower = scaleInput(gamepad1.right_stick_y, 1.0);
        double Rpower = scaleInput(gamepad1.left_stick_y, 1.0);

        FLMotor.setPower(Lpower);
        BLMotor.setPower(Lpower);
        FRMotor.setPower(Rpower);
        BRMotor.setPower(Rpower);
    }

    private void controlLift() {
        if (gamepad1.right_trigger > 0.1) {
            liftMotor1.setPower(gamepad1.right_trigger);
            liftMotor2.setPower(gamepad1.right_trigger);
        }
        else if (gamepad1.left_trigger > 0.1 && !touchSensor.getState()) {
            liftMotor1.setPower(-gamepad1.left_trigger);
            liftMotor2.setPower(-gamepad1.left_trigger);
        }
        else {
            liftMotor1.setPower(0);
            liftMotor2.setPower(0);
        }
    }

    private void controlGlyphPan() {
        /**
         * Buttons:
         *      A = toggle between pan up and down or flat depending on state of limit switch
         *      B = set pan down
         *      X = set pan mid
         *      Y = set pan flat
         * Automatic movements:
         *      Limit switch pressed (lift is down) => if pan flat put down
         *      Limit switch not pressed (lift is raised) => if pan down put flat
         */

        boolean limitSwitchPressed = touchSensor.getState();

        if ( gamepad1.b ) pos = GLYPH_PAN_DOWN;
        else if ( gamepad1.x ) pos = GLYPH_PAN_MID;
        else if ( gamepad1.y ) pos = GLYPH_PAN_FLAT;
        else if ( gamepad1.a && !aFlag ) {
            aFlag = true;
            if ( pos == GLYPH_PAN_UP ) {
                if ( limitSwitchPressed ) pos = GLYPH_PAN_DOWN;
                else pos = GLYPH_PAN_FLAT;
            }
            else pos = GLYPH_PAN_UP;
        }
        else if ( !gamepad1.a ) aFlag = false;

        if ( limitSwitchPressed && pos == GLYPH_PAN_FLAT ) pos = GLYPH_PAN_DOWN;
        else if ( !limitSwitchPressed && pos == GLYPH_PAN_DOWN ) pos = GLYPH_PAN_FLAT;

        servoGlyphPan.setPosition( pos );
    }

    private void controlCollector() {
        if(gamepad1.right_bumper) {
            glyphCollectorL.setPosition(COLLECTOR_CLOCKWISE); // Use 0.8 and 0.2 instead of 1.0 and 0 for new D941TW high speed
            glyphCollectorR.setPosition(COLLECTOR_COUNTERCLOCKWISE); //   servos. They don't turn when set to 1.0 and 0!  2/16/18 JAH
        } else if(gamepad1.left_bumper) {
            glyphCollectorL.setPosition(COLLECTOR_COUNTERCLOCKWISE);
            glyphCollectorR.setPosition(COLLECTOR_CLOCKWISE);
        } else {
            glyphCollectorR.setPosition(COLLECTOR_STOP);
            glyphCollectorL.setPosition(COLLECTOR_STOP);
        }

        if(gamepad2.y && touchSensor.getState()) {          // lift must be all the way down in order to put pusher in
            glyphCollectorTop.setPosition(GLYPH_TOP_IN);
        } else {
            glyphCollectorTop.setPosition(GLYPH_TOP_OUT);
        }
    }

    public double scaleInput(double val, double max) {
        double[] scale = {
                0.0 * max,
                0.0 * max,
                0.05 * max,
                0.05 * max,
                0.1 * max,
                0.1 * max,
                0.15 * max,
                0.15 * max,
                0.2 * max,
                0.2 * max,
                0.3 * max,
                0.35 * max,
                0.4 * max,
                0.45 * max,
                0.5 * max,
                0.6 * max,
                0.7 * max,
                0.85 * max,
                1.0 * max,
                1.0 * max };

        int index = (int) (val * 19.0);

        if (index < 0) index = -index;
        if (index > 19) index = 19;

        double rate;
        if (val < 0) rate = -scale[index];
        else rate = scale[index];

        return rate;
    }
}
