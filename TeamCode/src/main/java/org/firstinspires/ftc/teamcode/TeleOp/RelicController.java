package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Autonomous.C4PropFile;

/**
 * Created by cgrebosky on 2/3/18.
 */

public class RelicController {
    double YSPEED, XSPEED, CLOPEN, CLCLOSE;
    double YINIT, XINIT;
    double YOUT, XOUT;
    double POWER;

    Servo servoClaw, servoClawY, servoClawX;
    DcMotor extender;
    Gamepad gamepad;
    Telemetry telemetry;

    double yPower, xPower;

    boolean openCloseToggle = false;
    boolean aPressed = false;
    boolean toggleMode = false;
    boolean buttonPressed = false;
    boolean closed = true;

    public RelicController(Gamepad gamepad, Telemetry telemetry) {
        this.gamepad = gamepad;
        this.telemetry = telemetry;
    }

    public void init(HardwareMap hardwareMap) {
        initPropVars();
        yPower = YINIT;
        xPower = XINIT;
        servoClaw  = hardwareMap.servo.get("servo_claw");
        servoClawY = hardwareMap.servo.get("servo_clawy");
        servoClawX = hardwareMap.servo.get("servo_clawx");
        extender   = hardwareMap.dcMotor.get("motor_relic");
        servoClaw.setPosition(CLCLOSE);
    }

    private void initPropVars() {
        C4PropFile.loadPropFile();
        YSPEED = C4PropFile.getDouble("RELIC_ARM_YSPEED");
        XSPEED = C4PropFile.getDouble("RELIC_ARM_XSPEED");
        CLOPEN = C4PropFile.getDouble("RELIC_ARM_CLOPEN");
        CLCLOSE = C4PropFile.getDouble("RELIC_ARM_CLCLOSE");
        YINIT = C4PropFile.getDouble("RELIC_ARM_YINIT");
        XINIT = C4PropFile.getDouble("RELIC_ARM_XINIT");
        YOUT = C4PropFile.getDouble("RELIC_ARM_YOUT");
        XOUT = C4PropFile.getDouble("RELIC_ARM_XOUT");
        POWER = C4PropFile.getDouble("RELIC_ARM_POWER");
    }

    public void control() {
        updateVars();
        updateClaw();
        updatePosition();
        updateMotor();
    }

    private void updateClaw() {
        double val = closed?CLCLOSE:CLOPEN;
        servoClaw.setPosition(val);
    }

    private void updateVars() {
        if(gamepad.a && !aPressed) {
            openCloseToggle = !openCloseToggle;
            aPressed = true;
        } else if (!gamepad.a) {
            aPressed = false;
        }

        if(gamepad.left_stick_x != 0 || gamepad.right_stick_y != 0) {
            toggleMode = false;
        } else if(aPressed) {
            toggleMode = true;
        }

        if(gamepad.right_bumper && !buttonPressed) {
            buttonPressed = true;
            closed = !closed;
        } else if(!gamepad.right_bumper) {
            buttonPressed = false;
        }
    }

    private void updatePosition() {
        if(!toggleMode) {
            yPower = yPower - gamepad.right_stick_y * YSPEED;
            xPower = xPower - gamepad.left_stick_x * XSPEED;

            yPower = clamp(yPower, 0, 1);
            xPower = clamp(xPower, 0, 1);
        } else {
            if(openCloseToggle) {
                xPower = XOUT;
            } else {
                xPower = XINIT;
            }
        }

        if(gamepad.b) {
            yPower = YINIT;
        }

        servoClawY.setPosition(yPower);
        servoClawX.setPosition(xPower);
    }

    private void updateMotor() {
        extender.setPower(gamepad.left_trigger - gamepad.right_trigger);
    }

    private double clamp(double arg, double low, double high){
        return(Math.max(low, Math.min(high, arg)));
    }
}
