package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.TranslationalVelConstraint
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import org.firstinspires.ftc.teamcode.TankDrive.TurnAction
import org.opencv.core.Mat

@Autonomous(name = "Test", group = "Autonomous")
@Disabled
class Test() : InheritableAutonomous() {
    override var initialPose: Pose2d = Pose2d(8.5, -64.5, Math.toRadians(0.0))

    override fun runOpMode() {
        robot = MecanumDrive(hardwareMap, initialPose)
        val claw = Claw(hardwareMap)
        val lifterBoom = LifterBoom(hardwareMap)

        dashboard = FtcDashboard.getInstance()
        dashboardTelemetry = dashboard.telemetry

        waitForStart()
        runBlocking(
            robot.actionBuilder(initialPose).turn(Math.toRadians(90.0)).build()
        )

        dashboardTelemetry.addData("heading: ", robot.lazyImu.get().robotYawPitchRollAngles.yaw)
        dashboardTelemetry.update()
    }
}