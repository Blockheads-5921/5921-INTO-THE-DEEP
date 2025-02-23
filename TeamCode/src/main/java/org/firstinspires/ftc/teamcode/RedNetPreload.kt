package org.firstinspires.ftc.teamcode

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.TranslationalVelConstraint
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(name = "Red Net Preload", group = "Autonomous")
class RedNetPreload : InheritableAutonomous() {
    override var initialPose: Pose2d = Pose2d(-8.5, -64.5, Math.toRadians(90.0))

    override fun runOpMode() {
        robot = MecanumDrive(hardwareMap, initialPose)
        val claw = Claw(hardwareMap)
        val lifterBoom = LifterBoom(hardwareMap)

        val components = object {
            //Move to high bar that will clip the specimen onto it and move back
            var highBar: Action = robot.actionBuilder(Pose2d(-8.5, -64.5, Math.toRadians(90.0)))
                .lineToY(-34.0, TranslationalVelConstraint(18.0))
                .build()


            //position to grab strike
            var getRightStrike: Action =
                robot.actionBuilder(Pose2d(-8.5, -34.0, Math.toRadians(90.0)))
                    .lineToY(-57.0)
                    .splineToSplineHeading(
                        Pose2d(-21.0, -41.0, Math.toRadians(160.0)),
                        Math.toRadians(141.0)
                    )
                    .build()


            //Go to the basket
            var goToBasket: Action =
                robot.actionBuilder(Pose2d(-21.0, -41.0, Math.toRadians(173.0)))
                    .splineTo(Vector2d(-58.0, -58.0), Math.toRadians(-120.0))
                    .build()

            //Backup and drop boom
            var backup: Action = robot.actionBuilder(Pose2d(-57.0, -57.0, Math.toRadians(173.0)))
                .waitSeconds(1.0)
                .setReversed(false)
                .splineTo(Vector2d(-43.0, -43.0), Math.toRadians(-133.0))
                .build()

            val wait = robot.actionBuilder(Pose2d(0.0, 0.0, 0.0)).waitSeconds(1.0).build()
            val waitLess = robot.actionBuilder(Pose2d(0.0, 0.0, 0.0)).waitSeconds(0.5).build()

        }

        val first: Action = SequentialAction(
            claw.close(),
            lifterBoom.setLifterBoom((1015 * 0.738).toInt(), (262 * 0.377).toInt()),
            components.highBar,
            claw.open(),
            components.getRightStrike,
            lifterBoom.setLifterBoom((297 * 0.738).toInt(), (2350 * 0.377).toInt()),
            claw.close(),
            components.wait,
            lifterBoom.setLifterBoom((1520 * 0.738).toInt(), (2300 * 0.377).toInt()),
            components.waitLess,
            components.goToBasket,
            ParallelAction(
                claw.open(),
                components.wait,
                ),
            components.backup,
            ParallelAction(
                lifterBoom.setLifterBoom(0, 0),
                components.wait,
                claw.close()
            )
        )

        waitForStart()
        runBlocking(
            first
        )
    }
}