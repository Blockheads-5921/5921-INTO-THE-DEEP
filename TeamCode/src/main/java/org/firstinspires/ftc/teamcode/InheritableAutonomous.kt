package org.firstinspires.ftc.teamcode

import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

abstract class InheritableAutonomous : LinearOpMode() {
    abstract val initialPose: Pose2d;
    var robot: MecanumDrive = MecanumDrive(hardwareMap, initialPose)

    override fun runOpMode() {

    }
}
class Lifter(hardwareMap: HardwareMap) {
    private val lifter: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "lifter")

    init {
        lifter.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        lifter.mode = DcMotor.RunMode.RUN_TO_POSITION
        lifter.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lifter.direction = DcMotorSimple.Direction.FORWARD
    }


}