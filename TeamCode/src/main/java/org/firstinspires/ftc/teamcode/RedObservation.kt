package org.firstinspires.ftc.teamcode

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

        val components = object {
            val preClipSpecimenOne = robot.actionBuilder(initialPose)
                .lineToY(-33.0, TranslationalVelConstraint(40.0))
                .build()

            val pushSpikeSpecimen = robot.actionBuilder(Pose2d(8.5, -36.0, Math.toRadians(90.0)))
                .lineToY(-38.0, TranslationalVelConstraint(60.0))
                .splineToConstantHeading(Vector2d(39.0, -48.0), Math.toRadians(90.0))
                .build()
        }

        waitForStart()
        runBlocking(
            SequentialAction(
                claw.close(),
                ParallelAction(
                    components.preClipSpecimenOne,
                    lifterBoom.setLifterBoom(
                        Constants.Lifter.HIGH_CHAMBER - 20,
                        Constants.Boom.HIGH_CHAMBER,
                        2500
                    )
                ),
                claw.open(),
                components.pushSpikeSpecimen,
                lifterBoom.setLifterBoom(0, 0, 1000)
            )
        )
    }
}