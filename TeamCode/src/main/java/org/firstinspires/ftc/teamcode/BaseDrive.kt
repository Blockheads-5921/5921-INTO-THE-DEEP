package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Base Drive", group="production")
class BaseDrive : InheritableTeleOp() {
    override fun start() {
        robot!!.leftLock.position = 0.5;
        robot!!.rightLock.position = 0.5;
    }

    override fun loop() {
        drive(power());

    }

}