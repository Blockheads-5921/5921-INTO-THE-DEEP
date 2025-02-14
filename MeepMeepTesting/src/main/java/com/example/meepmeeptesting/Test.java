package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Test {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity robot = new DefaultBotBuilder(meepMeep).setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15).build();

        Pose2d initialPose = new Pose2d(8.5, -64.5, Math.toRadians(90.0));

        Action preClipSpecimenOne = robot.getDrive().actionBuilder(initialPose).lineToY(-36.0, new TranslationalVelConstraint(11.0))
                .build();

        Action pushSpikeSpecimen = robot.getDrive().actionBuilder(new Pose2d(8.5, -36.0, Math.toRadians(90.0)))
                .lineToY(-38.0, new TranslationalVelConstraint(60.0))
                .splineToConstantHeading(new Vector2d(39.0, -30.0), Math.toRadians(90.0))
                .lineToY(-15.0)
                .splineToConstantHeading(new Vector2d(47.0, -15.0), Math.toRadians(-90.0))
                .lineToY(-59.0, new TranslationalVelConstraint(60.0))
                .splineToConstantHeading(new Vector2d(47.0, -59.0), Math.toRadians(90.0))
                .build();

        robot.runAction(
                new SequentialAction(
                        preClipSpecimenOne,
                        pushSpikeSpecimen
                )
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(robot)
                .start();

    }
}
