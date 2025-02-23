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

@Autonomous(name = "Red Observation", group = "Autonomous")
class RedObservation() : InheritableAutonomous() {
    override var initialPose: Pose2d = Pose2d(8.5, -64.5, Math.toRadians(90.0))

    val pi = Math.PI;

    override fun runOpMode() {
        robot = MecanumDrive(hardwareMap, initialPose)
        val claw = Claw(hardwareMap)
        val lifterBoom = LifterBoom(hardwareMap)

        dashboard = FtcDashboard.getInstance()
        dashboardTelemetry = dashboard.telemetry

        val components = object {
            val preClipSpecimenOne = robot.actionBuilder(initialPose)
                .lineToY(-34.0, TranslationalVelConstraint(50.0))
                .build()

            val pushSpikes = robot.actionBuilder(Pose2d(8.5, -34.0, Math.toRadians(90.0)))
                .lineToY(-44.0, TranslationalVelConstraint(60.0))
                .splineToLinearHeading(
                    Pose2d(39.0, -39.0, Math.toRadians(-90.0)),
                    Math.toRadians(-90.0)
                )
                .lineToY(-15.0)
                .splineToConstantHeading(Vector2d(45.0, -15.0), Math.toRadians(-90.0))
                .lineToY(-62.0, TranslationalVelConstraint(60.0))
                .build()

            val backAndWaitForClip = robot.actionBuilder(Pose2d(45.0, -62.0, Math.toRadians(-90.0)))
                .lineToY(-42.0)
                .waitSeconds(1.5)
                .build()

            val wait = robot.actionBuilder(Pose2d(0.0, 0.0, 0.0)).waitSeconds(1.0).build()

            val driveToClip = robot.actionBuilder(Pose2d(45.0, -46.0, Math.toRadians(-90.0)))
                .lineToY(-60.0)
                .build()

            val goBack = robot.actionBuilder(Pose2d(45.0, -60.0, Math.toRadians(-90.0)))
                .splineToLinearHeading(Pose2d(0.0, -44.0, Math.toRadians(90.0)), 90.0).build()

            val forward = robot.actionBuilder(Pose2d(0.0, -44.0, Math.toRadians(90.0))).lineToY(-34.0).build()
            val backThere = robot.actionBuilder(Pose2d(0.0, -34.0, Math.toRadians(90.0))).lineToY(-40.0).build()
            val back = robot.actionBuilder(Pose2d(0.0, -38.0, Math.toRadians(90.0))).strafeToConstantHeading(Vector2d(56.0, -60.0)).build()
        }

        val first: Action = SequentialAction(
            claw.close(),
            ParallelAction(
                components.preClipSpecimenOne,
                lifterBoom.setLifterBoom(
                    749,
                    98
                )
            ),
            claw.open(),
            ParallelAction(
                lifterBoom.setLifterBoom(420, 0),
                components.pushSpikes,
            ),
            components.backAndWaitForClip,
            lifterBoom.setLifterBoom(
                390,
                38
            ),
            components.driveToClip,
            ParallelAction(
                claw.close(),
                components.wait
            ),
            ParallelAction(
                lifterBoom.setLifterBoom(
                    790, 98
                ),
                components.goBack
            ),
            components.forward,
            ParallelAction(
                claw.open(),
                components.wait
            ),

            components.backThere,
            components.back,
            ParallelAction(
                lifterBoom.setLifterBoom(0, 0),
                components.wait,
                claw.close()
            )
        )

        val second: Action = SequentialAction(
            claw.open(),
            lifterBoom.setLifterBoom(Constants.Lifter.SAFE_MODE, 250),
            claw.close()
        )

        waitForStart()
        runBlocking(
            first
        )
    }
}