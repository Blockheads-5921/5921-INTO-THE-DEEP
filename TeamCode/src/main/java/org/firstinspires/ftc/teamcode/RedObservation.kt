package org.firstinspires.ftc.teamcode

import com.acmerobotics.roadrunner.Action
import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.TranslationalVelConstraint
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.TankDrive.TurnAction
import org.opencv.core.Mat

@Autonomous(name = "Red Observation", group = "Autonomous")
class RedObservation() : InheritableAutonomous() {
    override var initialPose: Pose2d = Pose2d(8.5, -64.5, Math.toRadians(90.0))

    val pi = Math.PI;

    override fun runOpMode() {
        robot = MecanumDrive(hardwareMap, initialPose)
        val claw = Claw(hardwareMap)
        val lifterBoom = LifterBoom(hardwareMap)

        val components = object {
            val preClipSpecimenOne = robot.actionBuilder(initialPose)
                .lineToY(-33.0, TranslationalVelConstraint(50.0))
                .build()

            val strafeToSpikes = robot.actionBuilder(Pose2d(8.5, -33.0, Math.toRadians(90.0)))
                .lineToY(-42.0, TranslationalVelConstraint(60.0))
                .strafeTo(Vector2d(48.0, -44.0))
                .build()

            val back = robot.actionBuilder(Pose2d(48.0, -44.0, Math.toRadians(90.0)))
               // .turnTo(-90.0)
               // .turnTo(Math.toRadians(0.0))
                .turn(Math.toRadians(90.0))
                .build()

            val wait = robot.actionBuilder(Pose2d(0.0, 0.0, 0.0)).waitSeconds(0.5).build()
        }

        val first: Action = SequentialAction(
            claw.close(),
            ParallelAction(
                components.preClipSpecimenOne,
                lifterBoom.setLifterBoom(
                    Constants.Lifter.HIGH_CHAMBER - 20,
                    Constants.Boom.HIGH_CHAMBER
                )
            ),
            claw.open(),
            components.strafeToSpikes,
            lifterBoom.setLifterBoom(
                Constants.Lifter.PICKUP_SPIKE,
                Constants.Boom.PICKUP_SPIKE
            ),
            ParallelAction(
                claw.close(),
                components.wait
            ),
            lifterBoom.setLifterBoom(Constants.Lifter.PICKUP_SPIKE + 30, 30),
            components.back
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