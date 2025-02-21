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
    protected lateinit var robot: MecanumDrive
    private lateinit var dashboard: FtcDashboard
    private var dashboardTelemetry: Telemetry? = null
    val time: ElapsedTime = ElapsedTime()

    private var clawState = ClawStates.CLOSED
    protected var lifterBoomState = LifterBoomStates.REST
    private var slow = false

    protected var a = Button()
    protected var x = Button()
    protected var dpadUp = Button()
    protected var dpadRight = Button()
    private var dpadDown = Button()
    private var leftBumper = Button()
    private var rightBumper = Button()
    protected var preClimb = Button()
    protected var lockClimb = Button()
    protected var climb = Button()

    override fun init() {
        robot = MecanumDrive(hardwareMap, Pose2d(0.0, 0.0, 0.0))
        dashboard = FtcDashboard.getInstance()
        dashboardTelemetry = dashboard.telemetry

        callableIteration<DcMotorEx>(
            robot,
            ::resetMotor,
            listOf(
                "leftFront",
                "leftBack",
                "rightFront",
                "rightBack"
            )
        )

        robot.lifter.direction = DcMotorSimple.Direction.REVERSE
        robot.boom.direction = DcMotorSimple.Direction.REVERSE
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

        robot.leftFront.power = leftFrontPower
        robot.leftBack.power = leftBackPower
        robot.rightFront.power = rightFrontPower
        robot.rightBack.power = rightBackPower
    }

    fun claw() {
        if (a.up()) {
            if (clawState == ClawStates.CLOSED) {
                robot.transversal.position = Constants.Claw.OPEN
                clawState = ClawStates.OPEN
            } else if (clawState == ClawStates.OPEN) {
                robot.transversal.position = Constants.Claw.CLOSED
                clawState = ClawStates.CLOSED
            }
        }
    }

    fun safeMode() {
        robot.lifter.targetPosition = Constants.Lifter.SAFE_MODE // 600
        robot.lifter.power = 0.5
        robot.boom.targetPosition = Constants.Boom.SAFE_MODE // 100
        robot.boom.power = 0.99
        lifterBoomState = LifterBoomStates.SAFE_MODE
    }

    fun highChamber() {
        robot.boom.targetPosition = Constants.Boom.HIGH_CHAMBER // 281
        robot.boom.power = 0.99
        robot.lifter.targetPosition = Constants.Lifter.HIGH_CHAMBER + 100// 1100
        robot.lifter.power = 0.45
        lifterBoomState = LifterBoomStates.HIGH_CHAMBER
    }

    fun highBasket() {
        robot.lifter.targetPosition = Constants.Lifter.HIGH_BASKET // 1520
        robot.lifter.power = 0.8
        robot.boom.targetPosition = Constants.Boom.HIGH_BASKET // 2300
        robot.boom.power = .99
        lifterBoomState = LifterBoomStates.HIGH_BASKET
    }

    fun submersible() {
        if (dpadDown.up()) {
            if (lifterBoomState != (LifterBoomStates.SUBMERSIBLE_MID)) {
                robot.lifter.targetPosition = Constants.Lifter.SUBMERSIBLE_MID // 490
                robot.lifter.power = 0.75
                robot.boom.targetPosition = Constants.Boom.SUBMERSIBLE // 2300
                robot.boom.power = .99
                lifterBoomState = LifterBoomStates.SUBMERSIBLE_MID
            } else if (lifterBoomState == LifterBoomStates.SUBMERSIBLE_MID) {
                robot.lifter.targetPosition = Constants.Lifter.SUBMERSIBLE_DOWN // 297
                robot.lifter.power = .75
                LifterBoomStates.SUBMERSIBLE_DOWN
            }
        }
    }

    fun lifterToClimbPosition() {
        robot.lifter.targetPosition = Constants.Lifter.CLIMB_POSITION
        robot.lifter.power = 1.0
        robot.boom.targetPosition = Constants.Boom.SAFE_MODE
        robot.boom.power = 1.0
    }

    fun lockLifter() {
        robot.stageTwo.targetPosition = Constants.StageTwo.LOCK
        robot.stageTwo.power = 1.0
    }

    fun robotClimb() {
        robot.lifter.targetPosition = 5
        robot.lifter.power = 1.0
        robot.boom.targetPosition = Constants.Boom.SAFE_MODE
        robot.boom.power - 1.0
        robot.stageTwo.power = 0.0
    }

    fun power(): Double {
        if (gamepad1.right_bumper) return 0.75
        return if (gamepad1.right_trigger > 0.0) 0.5
        else 1.0
    }

    fun resetMotor(motor: DcMotorEx) {
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        motor.targetPosition = 0
        motor.mode = DcMotor.RunMode.RUN_TO_POSITION
        motor.power = 0.0;
    }

    fun updateButtons() {
        a.update(gamepad2.a)
        x.update(gamepad2.x)
        dpadUp.update(gamepad2.dpad_up)
        dpadRight.update(gamepad2.dpad_right)
        dpadDown.update(gamepad2.dpad_down)
        leftBumper.update(gamepad2.left_bumper)
        rightBumper.update(gamepad2.right_bumper)
        preClimb.update(gamepad1.dpad_up)
        lockClimb.update(gamepad1.dpad_left)
        climb.update(gamepad1.dpad_down)
    }

    enum class ClawStates {
        CLOSED,
        OPEN
    }

    enum class LifterBoomStates {
        REST,
        SAFE_MODE,
        HIGH_CHAMBER,
        HIGH_BASKET,
        SUBMERSIBLE_MID,
        SUBMERSIBLE_DOWN
    }
}