package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Base Drive", group="awesome")
class BaseDrive : InheritableTeleOp() {
    override fun loop() {
        drive(0.75);
    }
}