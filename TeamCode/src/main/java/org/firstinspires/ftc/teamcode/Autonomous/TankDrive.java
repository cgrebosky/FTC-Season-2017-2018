package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * main class for our tank drive system
 * @author Carson 2
 */

/*
Dead reckoning values done with test bot.
Need to retest to be accurate.

1st column: 2750 L, 2950 R
2nd column: 3600 L, 3750 R
3rd column: 4650 L, 5000 R

Left Turn: -2100 L, 2200 R

Forward after turn: 900 each
 */
public class TankDrive {

    DcMotor FLMotor, BLMotor, FRMotor, BRMotor;
    Servo servoArm, servoFlick;
    final double FORWARD_SPEED = 0.25;
    final double TURN_SPEED = 0.25;
    LinearOpMode lop;

    public TankDrive(DcMotor FLMotor, DcMotor BLMotor, DcMotor FRMotor, DcMotor BRMotor, Servo servoArm, Servo servoFlick, LinearOpMode lop) {
        this.FLMotor = FLMotor;
        this.BLMotor = BLMotor;
        this.FRMotor = FRMotor;
        this.BRMotor = BRMotor;
        this.servoArm = servoArm;
        this.servoFlick = servoFlick;
        this.lop = lop;
    }

    public void up(int tick) {
        int left = -BLMotor.getCurrentPosition()+tick;
        int right = -BRMotor.getCurrentPosition()+tick;
        while ((-BLMotor.getCurrentPosition() < left || -BRMotor.getCurrentPosition() < right) && lop.opModeIsActive()) {
            FLMotor.setPower(FORWARD_SPEED);
            BLMotor.setPower(FORWARD_SPEED);
            FRMotor.setPower(FORWARD_SPEED);
            BRMotor.setPower(FORWARD_SPEED);
        }
        FLMotor.setPower(0);
        BLMotor.setPower(0);
        FRMotor.setPower(0);
        BRMotor.setPower(0);
    }
    public void down(int tick) {
        int left = -BLMotor.getCurrentPosition()-tick;
        int right = -BRMotor.getCurrentPosition()-tick;
        while ((-BLMotor.getCurrentPosition() > left || -BRMotor.getCurrentPosition() > right) && lop.opModeIsActive()) {
            FLMotor.setPower(-FORWARD_SPEED);
            BLMotor.setPower(-FORWARD_SPEED);
            FRMotor.setPower(-FORWARD_SPEED);
            BRMotor.setPower(-FORWARD_SPEED);
        }
        FLMotor.setPower(0);
        BLMotor.setPower(0);
        FRMotor.setPower(0);
        BRMotor.setPower(0);
    }
    public void left(int tick) {
        int left = -BLMotor.getCurrentPosition()-tick;
        int right = -BRMotor.getCurrentPosition()+tick;
        while ((-BLMotor.getCurrentPosition() > left || -BRMotor.getCurrentPosition() < right) && lop.opModeIsActive()) {
            FLMotor.setPower(-TURN_SPEED);
            BLMotor.setPower(-TURN_SPEED);
            FRMotor.setPower(TURN_SPEED);
            BRMotor.setPower(TURN_SPEED);
        }
        FLMotor.setPower(0);
        BLMotor.setPower(0);
        FRMotor.setPower(0);
        BRMotor.setPower(0);
    }
    public void right(int tick) {
        int left = -BLMotor.getCurrentPosition()+tick;
        int right = -BRMotor.getCurrentPosition()-tick;
        while ((-BLMotor.getCurrentPosition() < left || -BRMotor.getCurrentPosition() > right) && lop.opModeIsActive()) {
            FLMotor.setPower(TURN_SPEED);
            BLMotor.setPower(TURN_SPEED);
            FRMotor.setPower(-TURN_SPEED);
            BRMotor.setPower(-TURN_SPEED);
        }
        FLMotor.setPower(0);
        BLMotor.setPower(0);
        FRMotor.setPower(0);
        BRMotor.setPower(0);
    }
}