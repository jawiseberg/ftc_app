package org.fawkesbots.rc.heathens.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.fawkesbots.rc.heathens.Hardware.HardwareCollector;
import org.fawkesbots.rc.heathens.Hardware.HardwareLauncher;
import org.fawkesbots.rc.heathens.Hardware.HardwareMecanumWithEncoders;
import org.fawkesbots.rc.heathens.Hardware.HardwareServoBasic;
import org.fawkesbots.rc.vendetta.Auton;
import org.fawkesbots.rc.vendetta.Camera.FawkesCam;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

@Autonomous(
        name="V_V",
        group="Finished"
)

/**
 * Created by Priansh on 11/30/16.
 */
public class VV_Auton extends Auton {
    public HardwareMecanumWithEncoders EncodedDrive;
    public HardwareLauncher Launcher; public HardwareServoBasic Flicker;
    public HardwareCollector Collector;
    public FawkesCam AutonCam; public BeaconUtil BeaconFinder;

    public float TILE=24; //length of a tile

    public int side = -1;

    @Override
    public void runOpMode() throws InterruptedException {
        side = ((FtcRobotControllerActivity)hardwareMap.appContext).color_side;

        EncodedDrive = new HardwareMecanumWithEncoders(hardwareMap,telemetry);
        EncodedDrive.hardwareSetup(); EncodedDrive.setSides(1, 1, 1, 1);
        Flicker = new HardwareServoBasic(hardwareMap,0.82f,0.0f,"flicker"); Launcher = new HardwareLauncher(hardwareMap);
        Flicker.hardwareSetup(); Launcher.hardwareSetup();
        Collector = new HardwareCollector(hardwareMap); Collector.hardwareSetup();
        AutonCam = new FawkesCam(hardwareMap);
        BeaconFinder = new BeaconUtil(telemetry,EncodedDrive);
        log("initialized");

        waitForStart();

        EncodedDrive.forwardEncoded(TILE, 0.78f);
        log("moved forward");
/*
        Launcher.fire(.78f); sleep(400); Launcher.fire(0.0f); //launch once
        Collector.collect(-.78f); sleep(1000); Flicker.flick(true); sleep(400);
        Collector.collect(0.0f); Flicker.flick(false); //collect another ball
        Launcher.fire(.78f); sleep(400); Launcher.fire(0.0f); //launch again
*/
        BeaconFinder.moveToBeacon(side);

        int[] colors;
        try {
            colors = AutonCam.getBeaconColors();
            log("red on " + ((colors[0] > colors[1]) ? "Left" : "Right"));
            BeaconFinder.hitBeacon(side, colors);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        EncodedDrive.strafeEncoded(2*TILE,0.6f);

        try {
            colors = AutonCam.getBeaconColors();
            log("red on " + ((colors[0] > colors[1]) ? "Left" : "Right"));
            BeaconFinder.hitBeacon(side, colors);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        EncodedDrive.strafeEncoded(-2 * TILE, .6f);
        EncodedDrive.forwardEncoded(-2.2f*TILE,.78f);
        log("finished");
    }
}