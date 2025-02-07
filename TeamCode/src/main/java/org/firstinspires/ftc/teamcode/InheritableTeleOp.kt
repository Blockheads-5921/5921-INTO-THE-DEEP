package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.roadrunner.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.abs
import kotlin.math.pow

abstract class InheritableTeleOp : OpMode() {
    protected var robot: MecanumDrive? = null
    private lateinit var dashboard: FtcDashboard
    private var dashboardTelemetry: Telemetry? = null
    val time: ElapsedTime = ElapsedTime()

    override fun init() {
        robot = MecanumDrive(hardwareMap, Pose2d(0.0, 0.0, 0.0))
        dashboard = FtcDashboard.getInstance()
        dashboardTelemetry = dashboard.telemetry

//        callableIteration<DcMotorEx>(
//            robot!!,
//            ::resetMotor
//        )
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
    fun safeMode() {
        robot!!.lifter.targetPosition = 560
        robot!!.lifter.power = 0.5
        robot!!.boom.targetPosition = 100
        robot!!.boom.power = 0.99
    }
    fun power(): Double {
        if (gamepad1.right_bumper) return 0.75
        if (gamepad1.right_trigger > 0.0) return 0.5
        else return 1.0
    }
    fun resetMotor(motor: DcMotorEx) {
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        motor.targetPosition = 0
        motor.mode = DcMotor.RunMode.RUN_TO_POSITION
        motor.power = 0.0;
    }
}