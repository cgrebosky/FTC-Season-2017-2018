package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by cgrebosky on 10/14/17.
 */

public class FlickerController {
    ColorSensor colorSensorL, colorSensorR;
    Servo servoArm, servoFlicker;
    final double armSpeed = 0.02;
    double FLICKER_LEFT, FLICKER_RIGHT, FLICKER_STRAIGHT, ARM_UP;

    final int colorThreshhold = 2;
    public boolean redTeam; //1 for red, 0 for blue
    LinearOpMode lop;

    /**
     * FlickerController conscructor
     *
     * @param colorSensorL leftt color sensor
     * @param colorSensorR right color sensor
     * @param servoArm servo controlling the arm
     * @param servoFlicker servo controlling the flicker
     */
    public FlickerController(ColorSensor colorSensorL, ColorSensor colorSensorR, Servo servoArm, Servo servoFlicker, LinearOpMode lop ) {
        this.colorSensorL = colorSensorL;
        this.colorSensorR = colorSensorR;
        this.servoArm = servoArm;
        this.servoFlicker = servoFlicker;
        this.redTeam = redTeam;
        this.lop = lop;
    }

    public void setRedTeam( boolean redTeam ) {
        this.redTeam = redTeam;
    }

    /**
     * Moves the arm to the ground position where it can easily flick the balls
     *
     * @throws InterruptedException only so I can use Thread.sleep()
     */
    public void moveArmDown() {
        servoFlicker.setPosition(FLICKER_STRAIGHT);
        double position = 0.47; // Arm up

        Trace.log("************************ ARM POSITION ***********************");

        while( /*!touchSensor.isPressed() && */ position >= 0 && lop.opModeIsActive()) {
            servoArm.setPosition( position );
            Trace.log("****************** servoArm position = " +  position );
            position -= armSpeed;
            lop.sleep(50);
        }
        lop.sleep(300);
    }

    /**
     * Flicks the lever.  This is responsible for measuring the colors and flicking the lever.  This
     * now uses a numeric system to compare colors instead of a boolean one like before.
     */
    public void flickLever() {

        int RLreading = colorSensorL.red();
        if(RLreading == 255) {
            RLreading = 0;
            Trace.log("****************** RL=255; error in reading color sensor");
        }
        int BLreading = colorSensorL.blue();
        if(BLreading == 255) {
            BLreading = 0;
            Trace.log("****************** BL=255; error in reading color sensor");
        }
        int RRreading = colorSensorR.red();
        if(RRreading == 255) {
            RRreading = 0;
            Trace.log("****************** RR=255; error in reading color sensor");
        }
        int BRreading = colorSensorR.blue();
        if(BRreading == 255) {
            BRreading = 0;
            Trace.log("****************** BR=255; error in reading color sensor");
        }

        int redLeft  = RLreading + BRreading;
        int blueLeft = BLreading + RRreading;

        Trace.log("************************ COLOR READINGS *******************");
        Trace.log("****************** Flicker Arm Color Sensor Readings:");
        Trace.log("****************** RL = " + RLreading);
        Trace.log("****************** BL = " + BLreading);
        Trace.log("****************** RR = " + RRreading);
        Trace.log("****************** BR = " + BRreading);
        Trace.log("****************** RedLeft   = " + redLeft);
        Trace.log("****************** BlueLeft = " + blueLeft);
        Trace.log("************************************************************");

        //TODO: test this.  I'm not sure if the servoFlicker positions are correct
        if(redLeft >= blueLeft + colorThreshhold) {
            //red on left
            if(redTeam)
                servoFlicker.setPosition(FLICKER_LEFT);
            else
                servoFlicker.setPosition(FLICKER_RIGHT);
            return;
        } else if(blueLeft >= redLeft + colorThreshhold) {
            //red on right
            if(redTeam)
                servoFlicker.setPosition(FLICKER_RIGHT);
            else
                servoFlicker.setPosition(FLICKER_LEFT);
            return;
        } else {
            Trace.log("********** Unable to detect color **********");
        }
    }

    /**
     * This has everything in it.  just call this after initializing when in position to immediately
     * have the arm come down and flick the correct ball.
     * @throws InterruptedException Just to enable Thread.sleep() function
     */
    public void completeArmMovement() {
        moveArmDown();
        if ( !lop.opModeIsActive() ) return;
        lop.sleep(100);

        flickLever();
        if ( !lop.opModeIsActive() ) return;
        lop.sleep(400);

        retractArm();
        if ( !lop.opModeIsActive() ) return;
        lop.sleep(500);
    }

    /**
     * Gets the arm and flicker in the correct position: the arm down at the bottom and the flicker
     * completely vertical.
     */
    public void init() {
        initPropVars();
        servoArm.setPosition(ARM_UP);
        servoFlicker.setPosition(FLICKER_LEFT);
    }

    private void initPropVars() {
        C4PropFile.loadPropFile();
        FLICKER_LEFT = C4PropFile.getDouble("FLICKER_LEFT");
        FLICKER_RIGHT = C4PropFile.getDouble("FLICKER_RIGHT");
        FLICKER_STRAIGHT = C4PropFile.getDouble("FLICKER_STRAIGHT");
        ARM_UP = C4PropFile.getDouble("ARM_UP");
    }

    /**
     * closes the FlickerController part.  This moves the flicker back into its closed position and
     * moves the arm back up into its init position.
     * @throws InterruptedException for the purpose of Thread.sleep()
     */
    public void retractArm() {
        servoArm.setPosition(ARM_UP);
        lop.sleep(400);
        servoFlicker.setPosition(FLICKER_LEFT);
    }

}
