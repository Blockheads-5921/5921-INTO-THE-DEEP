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

        val specimenOnePreClip = robot.actionBuilder(initialPose)
            .lineToY(-36.0, TranslationalVelConstraint(11.0))
            .build()

        runBlocking(claw.close())
        runBlocking(
            SequentialAction(
                ParallelAction(
                    specimenOnePreClip,
                    lifterBoom.setLifterBoom(811, 106)
                ),
                claw.open(),
                lifterBoom.safeMode()
            )
        )
    }
}