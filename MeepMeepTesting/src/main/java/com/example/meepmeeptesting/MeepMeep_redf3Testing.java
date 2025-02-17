package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeep_redf3Testing {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot1 = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 17)
                .build();


        // Go to the basket.
        myBot1.runAction(myBot1.getDrive().actionBuilder(new Pose2d(-8.5, -67.5, Math.toRadians(90)))
                //Hang preloaded specimen
                .lineToY(-37)
                .lineToY(-50)
                .waitSeconds(1)

                //Get right strike
                        .strafeTo(new Vector2d(35, -50))
                        .turn(Math.toRadians(-180))
                        .waitSeconds(2)
                        .lineToY(-40)
                .build());



        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_OFFICIAL)
                .setDarkMode(false)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot1)
                //.addEntity(myBot2)
                .start();
    }

}