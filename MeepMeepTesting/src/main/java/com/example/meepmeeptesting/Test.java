package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Test {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        RoadRunnerBotEntity robot = new DefaultBotBuilder(meepMeep).setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15).build();;

        robot.runAction(robot.getDrive().actionBuilder(new Pose2d(0, 0, Math.toRadians(0))).strafeTo(new Vector2d(10, 10)).build());
    }
}
