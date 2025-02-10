package org.firstinspires.ftc.teamcode

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

abstract class InheritableAutonomous : LinearOpMode() {
    abstract val initialPose: Pose2d;
    var robot: MecanumDrive = MecanumDrive(hardwareMap, initialPose)

    override fun runOpMode() {

    }
}

class LifterBoom(hardwareMap: HardwareMap) {
    private val lifter: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "lifter")
    private val boom: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "boom")

    init {
        lifter.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        lifter.mode = DcMotor.RunMode.RUN_TO_POSITION
        lifter.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lifter.direction = DcMotorSimple.Direction.FORWARD
        boom.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        boom.mode = DcMotor.RunMode.RUN_TO_POSITION
        boom.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        boom.direction = DcMotorSimple.Direction.FORWARD
    }

    fun safeMode(): Action {
        return Action {
            lifter.targetPosition = 600
            lifter.power = 0.5
            boom.targetPosition = 100
            boom.power = 0.99

            !(lifter.currentPosition == 600 && boom.currentPosition == 100)
        }
    }

    fun setLifterBoom(lifterPos: Int, boomPos: Int): Action {
        return Action {
            lifter.targetPosition = lifterPos
            lifter.power = .45
            boom.targetPosition = boomPos
            boom.power = .45

            !(lifter.currentPosition == lifterPos && boom.currentPosition == boomPos)
        }
    }
}

class Claw(hardwareMap: HardwareMap) {
    private val claw: Servo = hardwareMap.get(Servo::class.java, "transversal")

    init {
        claw.position = 0.55
    }

    fun open(): Action {
        return Action {
            claw.position = 0.3
            false
        }
    }

    fun close(): Action {
        return Action {
            claw.position = 0.55
            false
        }
    }
}