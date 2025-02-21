package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Base Drive", group="aaaaaaaa")
class BaseDrive : InheritableTeleOp() {

    override fun start() {
        robot.transversal.position = 0.55
        robot.leftLock.position = 0.5
        robot.rightLock.position = 0.5
    }

    override fun loop() {
        updateButtons()
        drive(power())
        claw()
        submersible()
        if (preClimb.up()) lifterToClimbPosition()
        if (lockClimb.up()) lockLifter()
        if (climb.up()) robotClimb()

        if (x.up()) highChamber()
        if (dpadUp.up()) safeMode()
        if (dpadRight.up()) highBasket()

        telemetry.addData("lifter boom state: ", lifterBoomState)
        telemetry.addData("lifter position: ", robot.lifter.currentPosition)
        telemetry.addData("boom position: ", robot.boom.currentPosition)
        telemetry.update()
    }

}