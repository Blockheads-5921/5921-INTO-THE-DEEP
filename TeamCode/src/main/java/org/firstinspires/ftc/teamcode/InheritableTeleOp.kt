package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.abs
import kotlin.math.pow

abstract class InheritableTeleOp : OpMode() {
    protected var robot: MecanumDrive? = null
    private lateinit var dashboard: FtcDashboard
    protected var dashboardTelemetry: Telemetry? = null
    val time: ElapsedTime = ElapsedTime()
    private var clawState = ClawStates.CLOSED


    override fun init() {
        robot = MecanumDrive(hardwareMap, Pose2d(0.0, 0.0, 0.0))
        dashboard = FtcDashboard.getInstance()
        dashboardTelemetry = dashboard.telemetry

        callableIteration<DcMotorEx>(
            robot!!,
            ::resetMotor,
            listOf(
                "leftFront",
                "leftBack",
                "rightFront",
                "rightBack"
            )
        )

        robot!!.lifter.direction = DcMotorSimple.Direction.REVERSE
        robot!!.boom.direction = DcMotorSimple.Direction.REVERSE
    }

    override fun loop() {}

    fun drive(power: Double) {
        var directionX = 0.0
        var directionY = 0.0
        var directionR = 0.0

        if (abs(gamepad1.left_stick_x.toDouble()) > 0.25) directionX =
            gamepad1.left_stick_x.pow(1).toDouble()
        if (abs(gamepad1.left_stick_y.toDouble()) > 0.25) directionY =
            -gamepad1.left_stick_y.pow(1).toDouble()
        if (abs(gamepad1.right_stick_x.toDouble()) > 0.25) directionR =
            gamepad1.right_stick_x.pow(1).toDouble()

        val leftFrontPower: Double = (directionX + directionY + directionR) * power
        val leftBackPower: Double = (-directionX + directionY + directionR) * power
        val rightFrontPower: Double = (-directionX + directionY - directionR) * power
        val rightBackPower: Double = (directionX + directionY - directionR) * power

        robot!!.leftFront.power = leftFrontPower
        robot!!.leftBack.power = leftBackPower
        robot!!.rightFront.power = rightFrontPower
        robot!!.rightBack.power = rightBackPower
    }

    fun claw() {
        if (gamepad2.left_bumper) {
            robot!!.transversal.position = 0.3
            clawState = ClawStates.OPEN
        }
        if (gamepad2.right_bumper) {
            robot!!.transversal.position = 0.55
            clawState = ClawStates.CLOSED
        }
    }

    fun safeMode() {
        robot!!.lifter.targetPosition = 600
        robot!!.lifter.power = 0.5
        robot!!.boom.targetPosition = 100
        robot!!.boom.power = 0.99
    }

    fun highChamber() {
        robot!!.boom.targetPosition = 281
        robot!!.boom.power = 0.99
        robot!!.lifter.targetPosition = 1100
        robot!!.lifter.power = 0.45
    }

    fun highBasket() {
        robot!!.lifter.targetPosition = 1520
        robot!!.lifter.power = 0.99
        robot!!.boom.targetPosition = 2200
        robot!!.boom.power = .99
    }

    fun submersibleOut() {
        robot!!.lifter.targetPosition = 490
        robot!!.lifter.power = 0.2
        robot!!.boom.targetPosition = 2300
        robot!!.boom.power = .99
    }

    fun submersibleDown() {
        robot!!.lifter.targetPosition = 297
        robot!!.lifter.power = .75
    }

    fun power(): Double {
        return if (gamepad1.a) 0.5
        else 1.0
    }

    fun resetMotor(motor: DcMotorEx) {
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        motor.targetPosition = 0
        motor.mode = DcMotor.RunMode.RUN_TO_POSITION
        motor.power = 0.0;
    }

    enum class ClawStates {
        CLOSED,
        OPEN
    }
}