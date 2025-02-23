/* Copyright (c) 2021 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Trajectory;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;


@Autonomous(name = "Red_f4", group = "Autonomous")
@Disabled
public class Red_F4 extends LinearOpMode {

    public void setLifterBoom(DcMotor boom, DcMotor lifter, int boomVal, int lifterVal) {
        lifter.setTargetPosition(lifterVal);
        lifter.setPower(.45);
        boom.setTargetPosition(boomVal);
        boom.setPower(.45);
    }

    public void setLifterBoomAndWait(DcMotor boom, DcMotor lifter, int boomVal, int lifterVal) {
        lifter.setTargetPosition(lifterVal);
        lifter.setPower(1);
        boom.setTargetPosition(boomVal);
        boom.setPower(1);
        while (boom.isBusy() || lifter.isBusy()){}
    }
    @Override
    public void runOpMode() throws InterruptedException {

        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(8.5, -67.5, Math.toRadians(90)));
        Servo clamp = hardwareMap.get(Servo.class, "transversal");
        DcMotor boom = hardwareMap.get(DcMotor.class, "boom");
        DcMotor lifter = hardwareMap.get(DcMotor.class, "lifter");

        boom.setDirection(DcMotorSimple.Direction.REVERSE);
        lifter.setDirection(DcMotorSimple.Direction.REVERSE);

        boom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        boom.setTargetPosition(0);
        boom.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        boom.setPower(0.0);

        lifter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lifter.setTargetPosition(0);
        lifter.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lifter.setPower(0.0);

        final double GRAB = 0.55;
        final double RELEASE =0.3;

        final double waitTime = 1.0;

        //Move to high bar that will clip the specimen onto it and move back
        Action highBar = drive.actionBuilder(new Pose2d(8.5, -64.5, Math.toRadians(90)))
                .lineToY(-36, new TranslationalVelConstraint(11))
                .build();

        //Push strikes 1 and 2
        Action pushStrike1and2 = drive.actionBuilder(new Pose2d(8.5, -36, Math.toRadians(90)))
                .lineToY(-44, new TranslationalVelConstraint(60))
                .splineToLinearHeading(new Pose2d(39, -39, Math.toRadians(-90)), Math.toRadians(-90))
                .lineToY(-15)
                .splineToConstantHeading(new Vector2d(47,-15), Math.toRadians(-90))
                .lineToY(-62, new TranslationalVelConstraint(60))
//                .splineToConstantHeading(new Vector2d(47,-59), Math.toRadians(90))
                //.lineToY(-46)
                // Go back and push the middle strike
                //.lineToY(-15)  //************************************************
                //.splineToConstantHeading(new Vector2d(57,-15), Math.toRadians(-90))  //*************
                //.lineToY(-59, new TranslationalVelConstraint(60))  //***************************

                .build();

        Action Grab_clip = drive.actionBuilder(new Pose2d(47, -62, Math.toRadians(-90)))
                .lineToY(-46)
                .build();

        Action move_away_from_wall = drive.actionBuilder(new Pose2d(47,-46, Math.toRadians(-90)))  //**
                //.waitSeconds(1)
                .splineToConstantHeading(new Vector2d(38,-46),Math.toRadians(10))
                .build(); //May not have to move away from the wall

        //Clip strike 1
        Action clipStrike1 = drive.actionBuilder( new Pose2d(38, -46, Math.toRadians(10)))
                .setReversed(true)
                .splineTo(new Vector2d(-3,-49), Math.toRadians(-81))  //clip onto bar & open clamp
                .setReversed(false)
                //.splineTo(new Vector2d(5,-40), Math.toRadians(-180))
                .lineToY(-41)
                //.lineToY(-57)
                .build();

        //Push strike 3
        Action pushStrike3 = drive.actionBuilder( new Pose2d(58, -43.5, Math.toRadians(90)))
                .lineToY(-45)
                .splineToConstantHeading(new Vector2d(59,-10), Math.toRadians(70))
                .splineToConstantHeading(new Vector2d(62,-50), Math.toRadians(90),
                        new TranslationalVelConstraint(60))
                .lineToY(-43.5)
                .build();

        //Clip 2nd specimen
        Action clipStrike2 = drive.actionBuilder( new Pose2d(62, -43.5, Math.toRadians(90)))
                .splineTo(new Vector2d(0,-39), Math.toRadians(90))  //clip onto bar & open clamp
                .lineToY(-45)
                .build();

        //Get strike 3
        Action getStrike3 = drive.actionBuilder( new Pose2d(0, -43.5, Math.toRadians(90)))
                .waitSeconds(.25)
                .splineTo(new Vector2d(50, -43.5), Math.toRadians(90))//get it
                .build();

        //Clip specimen 3
        Action clipStrike3 = drive.actionBuilder( new Pose2d(50, -43.5, Math.toRadians(90)))
                .waitSeconds(0.5)
                .splineTo(new Vector2d(5,-37), Math.toRadians(-90))//hang it
                .lineToY(-43)
                .build();

        //Park at the of autonomous
        Action park = drive.actionBuilder( new Pose2d(-3, -41, Math.toRadians(90)))  //***
                .waitSeconds(0.5)
                .splineTo(new Vector2d(40,-61), Math.toRadians(0))//park it
                .build();

        Action delay1 = drive.actionBuilder(new Pose2d(0, 0, 90))
                .waitSeconds(1)
                .build();

        Action delay2 = drive.actionBuilder(new Pose2d(0, 0, 90))
                .waitSeconds(2)
                .build();

        Action goBack = drive.actionBuilder(new Pose2d(5, -38,   90))
                .lineToY(-53, new TranslationalVelConstraint(20))
                .build();

        Action waitForIT = drive.actionBuilder(new Pose2d(0,0,0))
                .waitSeconds(waitTime)
                //.lineToY(-46)
                .build();
        Action waitForIT_two = drive.actionBuilder(new Pose2d(0,0,0))
                .lineToX(-30)
                .waitSeconds(waitTime)
                .build();

        Action back_up = drive.actionBuilder(new Pose2d(-3,-45, Math.toRadians(-90)))
                .lineToY(-45)
                .build();



        waitForStart(); // wait to start


        //==========================  Beginning of autonomous  =====================================

        //Clip preloaded specimen
        clamp.setPosition(GRAB);
        //Actions.runBlocking(delay1);
        setLifterBoom(boom, lifter, 98 , 749);
        //while (boom.isBusy() || lifter.isBusy()){ }
        Actions.runBlocking(highBar);
        clamp.setPosition(RELEASE);

        //Go push strike 1 and 2 and grab strike 1
        Actions.runBlocking(pushStrike1and2);
        setLifterBoom(boom, lifter, 38, 390);

        Actions.runBlocking(Grab_clip);
        setLifterBoom(boom, lifter, 100, 560);//get ready clamp strike 1
        while (boom.isBusy() || lifter.isBusy()){}

        clamp.setPosition(GRAB);
        //Actions.runBlocking(waitForIT);
        //Actions.runBlocking(waitForIT);


        //Go clip strike 1
        //setLifterBoom(boom, lifter, 281, 1095);
        //Actions.runBlocking(waitForIT_two);
        //while (boom.isBusy() || lifter.isBusy()){ }
        //Actions.runBlocking(move_away_from_wall);
        //Actions.runBlocking(clipStrike1);
        //setLifterBoom(boom, lifter, 300, 1025);
        //Actions.runBlocking(back_up);aA
        //while (boom.isBusy() || lifter.isBusy()){}
        //Thread.sleep(500);
        //clamp.setPosition(RELEASE);
setLifterBoomAndWait(boom,lifter,0,0);
        while (boom.isBusy() || lifter.isBusy()){

        }
        Actions.runBlocking(waitForIT_two);
        //Go park
//        Actions.runBlocking(park);  //************************************************************

/*        //Go push strike 3
        Actions.runBlocking(pushStrike3);
        setLifterBoom(boom, lifter, 100, 1000);
        while (boom.isBusy() || lifter.isBusy()){ }
        clamp.setPosition(GRAB);

        //Go clip strike 2
        setLifterBoom(boom, lifter, 281, 1000);
        Actions.runBlocking(clipStrike2);
        clamp.setPosition(RELEASE);

        //Go get strike 3
        setLifterBoom(boom, lifter, 100, 1000);
        Actions.runBlocking(getStrike3);
        clamp.setPosition(GRAB);

        //clip specimen 3
        setLifterBoom(boom, lifter, 100, 1000);
        Actions.runBlocking(clipStrike3);
        clamp.setPosition(RELEASE);

        //Go park
        setLifterBoom(boom, lifter, 490, 2300);
        Actions.runBlocking(park);

 */   }
}
