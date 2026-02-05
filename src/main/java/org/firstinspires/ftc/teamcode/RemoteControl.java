package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="RemoteControl", group="Robot")
public class RemoteControl extends OpMode {

    // Add motor variables
    DcMotor leftMotor;
    DcMotor rightMotor;
    DcMotor intakeMotor;
    DcMotor flywheelMotor;

    // Add controller variables
    double driveInput;
    double turnInput;
    double leftInput;
    double rightInput;
    double intakeInput;
    double flywheelInput;
    double leftPower;
    double rightPower;
    double intakePower;
    double flywheelPower;
    // Add other variables
    double reverseSpeed = -0.5;
    int counter = 0;
    boolean counterFlag = false;
    // ignore this
    boolean sing = false;

    @Override
    public void init() {

        // Connect motors to the control hub
        leftMotor = hardwareMap.dcMotor.get("leftMotor");
        rightMotor = hardwareMap.dcMotor.get("rightMotor");
        intakeMotor = hardwareMap.dcMotor.get("intakeMotor");
        flywheelMotor = hardwareMap.dcMotor.get("flywheelMotor");
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flywheelMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flywheelMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.setMsTransmissionInterval(100);
    }

    @Override
    public void loop() {
        // Controller mapping
        driveInput = -gamepad1.left_stick_y; // Drive forward/backward
        turnInput = gamepad1.right_stick_x; // Turn left/right
        leftInput = gamepad1.left_stick_y; // Tank drive - left motor
        rightInput = gamepad1.right_stick_y; // Tank drive - right motor
        intakeInput = bumperTriggerCalc(false); // Intake wheel - trigger for forward, bumper for reverse
        flywheelInput = bumperTriggerCalc(true); // Flywheel - trigger for forward, bumper for reverse
        loadControls();

        // Run motors
        driveRobot();
        intakePower = intakeInput;
        intakeMotor.setPower(-intakePower);
        flywheelPower = flywheelInput;
        flywheelMotor.setPower(flywheelPower);

        updateTelemetry();
    }

    public void driveRobot() {
        // Driving logic completed by Edwin Hoeppner and Arjun Subash
        if (driveInput > 0 && turnInput !=0) {
            // Driving forward and turning logic
            if (turnInput < 0) {
                // Right Joystick tilted Left

                // Right Wheel takes the maximum value of
                // either the right stick or left stick
                rightPower = Math.max(driveInput, -turnInput);
                // Left Wheel subtracts the turn input from the
                // drive input to "slow' the left wheel down"
                leftPower = -(driveInput + turnInput);
            } else if (turnInput > 0) {
                // Right Joystick tilted Right

                // Right Wheel subtracts the turn input from the
                // drive input to "slow' the right wheel down"
                rightPower = (driveInput - turnInput);
                // Right Wheel takes the maximum value of
                // either the right stick or left stick
                leftPower = -(Math.max(driveInput, turnInput));
            }
        } else if (driveInput < 0 && turnInput != 0){
            // Driving backwards and turning logic
            // Everything is reversed to make it go backwards
            // Right Forward Logic is similar to Right Backward Logic and vice verca
            if (turnInput < 0) {
                // Right Joystick tilted Left

                // Right Wheel subtracts the turn input from the
                // drive input to "slow' the right wheel down"
                // Its negative so it can go backwards
                rightPower = (driveInput - turnInput);
                // Left Wheel takes the maximum value of either the right
                // stick or left stick but negative to make it go backwards
                leftPower = -Math.min(driveInput, -turnInput);
            } else if (turnInput > 0) {
                // Right Joystick tilted Right

                // Right Wheel takes the maximum value of either the right
                // stick or left stick but negative to make it go backwards
                rightPower = Math.min(driveInput, turnInput);
                // Left Wheel subtracts the turn input from the
                // drive input to "slow' the left wheel down"
                // Its negative so it can go backwards
                leftPower = -(driveInput + turnInput);
            }
        } else if (driveInput != 0) {
            // Move Forward and Backward
            rightPower = driveInput - turnInput;
            leftPower = -driveInput - turnInput;
        } else if (driveInput == 0) {
            // Still using an else if because its always a
            // good habit to use it just in case there is
            // an error in the code and something unexpected happens

            // Spin 360
            rightPower = -turnInput;
            leftPower = -turnInput;
        }

        // Run motors
        leftMotor.setPower(leftPower);
        rightMotor.setPower(rightPower);
    }

    // Update telemetry
    public void updateTelemetry() {
        // Telemetry data for the controller device for testing purposes.
        telemetry.addLine("TeleOp: RemoteControl");
        telemetry.addData("Drive", driveInput * 100 + "%");
        telemetry.addData("Turn", turnInput * 100 + "%");
        telemetry.addData("Left wheel", leftPower * 100 + "%");
        telemetry.addData("Right wheel", rightPower * 100 + "%");
        telemetry.addData("Intake wheel", intakePower * 100 + "%");
        telemetry.addData("Flywheel", flywheelPower * 100 + "%");
        telemetry.addLine("Controls:");
        telemetry.addData("Drive", "Left Stick Up/Down");
        telemetry.addData("Turn", "Right Stick Left/Right");
        telemetry.addData("Intake", "Left Trigger/Bumper");
        telemetry.addData("Flywheel", "Right Trigger/Bumper");
        telemetry.update();
    }

    public double bumperTriggerCalc(boolean right) {
        // If left -> false, If right -> true
        // When trigger pressed, returns trigger value
        // When bumper pressed, returns -1
        // When both bumpers pressed, returns 0

        // Declare variables
        boolean bumper;
        double trigger;

        // Select which pair of bumper/triggers to return
        if(!right) {
            bumper = gamepad1.left_bumper;
            trigger = gamepad1.left_trigger;
        } else {
            bumper = gamepad1.right_bumper;
            trigger = gamepad1.right_trigger;
        }

        // Calculations
        if(bumper) { // If bumper is pressed
            if(trigger > 0) {
                // If both are pressed, return 0
                return 0;
            } else {
                // If only the bumper is pressed, reverse at reverse speed
                return reverseSpeed;
            }
        } else {
            // If only the trigger is pressed, use trigger for speed control
            return trigger;
        }
    }

    public void loadControls() {
        boolean resetFlag = false;
        //
        // dont think too much about it
        //
        boolean[] code = {
                gamepad1.dpad_up,
                gamepad1.dpad_up,
                gamepad1.dpad_down,
                gamepad1.dpad_down,
                gamepad1.dpad_left,
                gamepad1.dpad_right,
                gamepad1.dpad_left,
                gamepad1.dpad_right,
                gamepad1.b,
                gamepad1.a,
                gamepad1.start
        };
        //
        // you can stop scrolling now
        //
        for(int i = 0; i < 11; i++) {
            if(code[i]) {
                if(i == counter && !counterFlag) {
                    counter++;
                } else {
                    counter = 0;
                }
                resetFlag = true;
                counterFlag = true;
            }
        }
        //
        // I SAID YOU CAN STOP SCROLLING NOW
        //
        if(counter > 10) {
            counter = 0;
            sing = true;
        }
        if (sing) {
            konamiCode();
        }
        if (!resetFlag) {
            counterFlag = false;
        }
    }
    public void konamiCode() {
        // Wow.
        // You really scrolled to the bottom of this mess?
        // No self-restraint whatsoever.
        // No wonder they say this generation is cooked.
        // Really, what do you get from this, pretending you understand this code?
        // You thought reusing last year's code would be easy, didn't you?
        // I wrote this code half-asleep and no mere mortal will ever understand it.
        // We both know the robot has other problems that need fixing.
        // Go back to help your team instead of doomscrolling
        // on Android Studio like the nerd you are.
        // Go on. I can hear them shouting at you.
        // ...
        // I'm waiting.
        // ...
        // Get back to work, you imbecile.
        // ...
        // Well if you haven't reached your attention span yet, let me tell you a story.
        // The year: 2026
        // The problem: Our team of 5 grade 9's and barely any parts
        // was nowhere near ready to compete.
        // The day before the competition, the robot couldn't even shoot balls,
        // which was the main focus of the game.
        // The solution: You really think we had one? No, I gave up and started adding
        // the dumbest easter egg ever into the teleop code.
        // Yeah, that's the whole story. You thought this was going to be
        // some elaborate and meaningful story? Of course not, I'm writing this bullshit
        // the day before the competition after telling my parents I'm doing homework.
        // ...
        // You're still reading?
        // Quite impressive for your generation.
        // Alright, storytime's over. Go back to working on the robot.
        // ...
        // ...
        // ...
        // Why the hell are you still here?
        // What do you want from me?
        // Any important code ended around 85 lines ago.
        // Well, if you're still here, why not add your name to the list of engineers
        // who have wasted their time here.
        String survivors[] = {
            "Jarnu47 - 2026 :)"
        };

    }
}
