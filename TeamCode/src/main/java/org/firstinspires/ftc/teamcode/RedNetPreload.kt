package org.firstinspires.ftc.teamcode

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
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
            val depositPreload = robot.actionBuilder(initialPose)
                .strafeToLinearHeading(Vector2d(-48.0, -48.0), -45.0).build()

            val innerSpike = robot.actionBuilder(Pose2d(-48.0, -48.0, Math.toRadians(-45.0)))
                .strafeToLinearHeading(
                    Vector2d(-60.0, -48.0), 105.0
                ).build()
        }

        val first: Action = SequentialAction(
            claw.close(),
            ParallelAction(
                components.depositPreload,
                lifterBoom.setLifterBoom(Constants.Lifter.HIGH_BASKET, Constants.Boom.HIGH_BASKET)
            ),
            claw.open(),
            ParallelAction(
                lifterBoom.safeMode(),
                components.innerSpike
            )
        )

        waitForStart()
        runBlocking(
            first
        )
    }
}