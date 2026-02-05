package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="BadAuto", group="Robot")
public class BadAuto extends LinearOpMode {
    // Add motor variables
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor intakeMotor;
    DcMotor flywheelMotor;

    // Add other variables
    ElapsedTime runtime = new ElapsedTime();

    // All measurements in MM
    int driveTicks = 1440;
    int wheelRadius = 51;
    double trim = 0.95;
    int wheelDistance = 330;

    public void runOpMode() {
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");
        flywheelMotor = hardwareMap.dcMotor.get("flywheelMotor");
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        flywheelMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.setMsTransmissionInterval(100);
        waitForStart();
        int robotLength = 300; // FIX THIS LATER
        int shootDistance = 300; // FIX THIS LATER
        int placeDistance = 180;
        boolean blue = true;
        float shootingTime = 5;
        float placingTime = 6;
        boolean shooting = false;
        double intakePlacingSpeed = -0.7;
        drive(4000, 0.8, "Driving to opposite wall");
        drive(-914 + robotLength, 0.6, "Reversing to align with the goal");
        turn(55, 0.6, blue, "Aiming at the goal");
        drive(1585 - robotLength, 0.8, "Driving to goal");


        if(shooting) {
            drive(-shootDistance, 0.6, "Reversing from the goal to shoot");
            runtime.reset();
            while(runtime.seconds() < shootingTime) {
                intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                flywheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                intakeMotor.setPower(0.75);
                flywheelMotor.setPower(1);
            }
        } else {
            drive(-placeDistance, 0.6, "Reversing from the goal to place");
            runtime.reset();
            while(runtime.seconds() < placingTime) {
                intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                intakeMotor.setPower(intakePlacingSpeed);
            }
        }
        drive(-1585, 0.8, "Driving away");
        turn(-55, 0.6, blue, "Aiming at the wall");
        drive(1500, 0.8, "Driving to wall");
        drive(-1830, 0.8, "Driving reverse");
        turn(90, 0.6, blue, "Aiming at the gate");
        drive(2000, 0.8, "Driving to gate");




        intakeMotor.setPower(0);
        flywheelMotor.setPower(0);


    }
    public int driveCalc(int distance) {
        double circumference = Math.PI * 2 * wheelRadius;
        double decimalTicks = (driveTicks / circumference) * distance;
        int target = (int) (Math.round(decimalTicks) * trim);
        return target;
    }
    public void drive(int distance, double speed, String action) {
        int target = driveCalc(distance);
        leftMotor.setTargetPosition(leftMotor.getCurrentPosition() + target);
        rightMotor.setTargetPosition(rightMotor.getCurrentPosition() + target);
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setPower(speed);
        rightMotor.setPower(speed);
        while (opModeIsActive() && (leftMotor.isBusy() || rightMotor.isBusy())) {
            telemetry.addData("Action", action);
            telemetry.addData("leftPos", leftMotor.getCurrentPosition());
            telemetry.addData("rightPos", rightMotor.getCurrentPosition());
            telemetry.update();
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }
    public void turn(int degrees, double speed, boolean right, String action) {
        double circle = 2 * Math.PI * wheelDistance;
        double distance = circle / (360 / (double) degrees);
        int target = driveCalc((int) distance);
        if(right) {
            rightMotor.setTargetPosition(rightMotor.getCurrentPosition() + target);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setPower(speed);
        } else {
            leftMotor.setTargetPosition(leftMotor.getCurrentPosition() + target);
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftMotor.setPower(speed);
        }
        while (opModeIsActive() && (leftMotor.isBusy() || rightMotor.isBusy())) {
            telemetry.addData("Action", action);
            telemetry.addData("leftPos", leftMotor.getCurrentPosition());
            telemetry.addData("rightPos", rightMotor.getCurrentPosition());
            telemetry.update();
        }
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }
}

