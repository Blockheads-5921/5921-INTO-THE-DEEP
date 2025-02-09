package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Base Drive", group="production")
class BaseDrive : InheritableTeleOp() {
    private var lock : Boolean = false

    override fun start() {
        robot!!.transversal.position = 0.55
        robot!!.leftLock.position = 0.5
        robot!!.rightLock.position = 0.5
    }

    override fun loop() {
        drive(power())
        claw()

        if (gamepad2.x) highChamber()
        if (gamepad2.dpad_up) safeMode()
        if (gamepad2.dpad_right) highBasket()
        if (gamepad2.dpad_down) {
            submersibleOut()
            lock = true
        }
        else if (!gamepad2.dpad_down && lock && !robot!!.lifter.isBusy && !robot!!.boom.isBusy) {
            submersibleDown()
            lock = false
        }

        dashboardTelemetry!!.addData("left front:", robot!!.leftFront.power)
        dashboardTelemetry!!.addData("left back:", robot!!.leftBack.power)
        dashboardTelemetry!!.addData("right front:", robot!!.rightFront.power)
        dashboardTelemetry!!.addData("right back:", robot!!.rightBack.power)
        dashboardTelemetry!!.update()

    }

}