package org.firstinspires.ftc.teamcode

import com.acmerobotics.roadrunner.ParallelAction
import com.acmerobotics.roadrunner.Pose2d
import com.acmerobotics.roadrunner.SequentialAction
import com.acmerobotics.roadrunner.TranslationalVelConstraint
import com.acmerobotics.roadrunner.Vector2d
import com.acmerobotics.roadrunner.ftc.runBlocking
import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(name = "Red Net", group = "Autonomous")
class RedNet() : InheritableAutonomous() {
    override var initialPose: Pose2d = Pose2d(0.0, 0.0, Math.toRadians(90.0))

    val pi = Math.PI;

    override fun runOpMode() {
        val claw = Claw(hardwareMap)
        val lifterBoom = LifterBoom(hardwareMap)

        val specimenOnePreClip = robot.actionBuilder(initialPose)
            .strafeTo(Vector2d(67.5, 8.5))
            .build()

        runBlocking(claw.close())
        runBlocking(SequentialAction(
            specimenOnePreClip
            )
        )
    }
}