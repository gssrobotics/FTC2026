# FTC2026
Bear Necessities' Code for FIRST Tech Challenge 2025-2026 "Decode".

## Overview
Our robot, named Peashooter, was a simple robot with 2 wheel drive, an intake wheel, and 2 flywheels powered by the same motor to shoot artifacts. This code features an enhanced two-wheel-drive system which offers better control ver the robot when driving and turning at the same time.

## Autonomous
There are 2 autonomous routines, both run without the need for sensors. A few variables must be changed in the source code to change the robot's physical characteristics and team. The 2 routines are:
### BadAuto
BadAuto is designed to score points by either shooting artifacts or placing them in the Depot, which are preloaded into the robot. The robot will be lined up so that the centre is in line with the line between the 4th and 5th tiles, on the opposite side of the field to the goals. Variables can be changed to alter robot wheel size, team colour, and robot length, as well as choosing to shoot or not. This was done in case the flywheel was not ready to shoot on the competition day.
#### Once activated, the robot will:
- Drive forward into the opposite wall
- Drive backwards
- Turn towards the goal (set by boolean blue)
- Drive into the goal
- Back off from the goal
- Decide whether to shoot or place artifacts from predefined variables
- If shooting, spin up the flywheel and start pushing the artifacts into it
- If placing, spin the intake wheel backwards
- After some time of spinning, turn off both wheels
### WorseAuto
WorseAuto is designed to minimize possible penalties while still scoring a ranking point. This was coded on the day of the competition, so it is quite buggy and messy. Most code is copypasted from BadAuto.
#### Once activated, the robot will:
- Drive forward engouh to leave the starting line
- Spin the intake wheel backwards
- Turn of all motors before autonomous finishes

## TeleOp
The TeleOp controls are quite simple, offering precise analog control over the motors. The robot can drive and turn at the same time, offering intuitive controls that allow for precise driving.

**Drive:** Left stick up/down
**Turn:** Right stick left/right
**Intake:** Left trigger to intake (analog), Left bumper to release (fixed speed)

**Flywheel:** Right trigger to intake (analog), Right bumper to release (fixed speed)