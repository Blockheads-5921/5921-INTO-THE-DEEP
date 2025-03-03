package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry

abstract class InheritableAutonomous : LinearOpMode() {
    abstract val initialPose: Pose2d
    lateinit var robot: MecanumDrive
    lateinit var dashboard: FtcDashboard
    lateinit var dashboardTelemetry: Telemetry

    override fun runOpMode() {

    }
}

class LifterBoom(hardwareMap: HardwareMap) {
    private val lifter: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "lifter")
    private val boom: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "boom")

    init {
        lifter.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        lifter.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        lifter.direction = DcMotorSimple.Direction.REVERSE
        boom.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        boom.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        boom.direction = DcMotorSimple.Direction.REVERSE
    }

    fun safeMode(): Action {
        return Action {
            lifter.targetPosition = Constants.Lifter.SAFE_MODE
            lifter.mode = DcMotor.RunMode.RUN_TO_POSITION
            lifter.power = 1.0
            boom.targetPosition = Constants.Boom.HIGH_BASKET
            boom.mode = DcMotor.RunMode.RUN_TO_POSITION
            boom.power = 0.99

            (lifter.isBusy || boom.isBusy)
        }
    }

    fun setLifterBoom(lifterPos: Int, boomPos: Int): Action {


        return Action() {
            lifter.targetPosition = lifterPos
            lifter.mode = DcMotor.RunMode.RUN_TO_POSITION
            lifter.power = 1.0
            boom.targetPosition = boomPos
            boom.mode = DcMotor.RunMode.RUN_TO_POSITION
            boom.power = 1.0

            (lifter.isBusy || boom.isBusy)
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
            claw.position = Constants.Claw.OPEN
            false
        }
    }

    fun close(): Action {
        return Action {
            claw.position = Constants.Claw.CLOSED
            false
        }
    }
}