package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Base Drive", group="production")
class BaseDrive : InheritableTeleOp() {

    override fun start() {
        robot!!.transversal.position = 0.55
        robot!!.leftLock.position = 0.5
        robot!!.rightLock.position = 0.5
    }

    override fun loop() {
        updateButtons()
        drive(power())
        claw()
        submersible()

        if (x.tapped()) highChamber()
        if (dpadUp.tapped()) safeMode()
        if (dpadRight.tapped()) highBasket()
        if (dpadDown.tapped()) submersible()
    }

}