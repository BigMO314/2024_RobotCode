// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.molib.buttons.ButtonManager;
import frc.robot.periods.Autonomous;
import frc.robot.periods.Disabled;
import frc.robot.periods.Teleoperated;
import frc.robot.periods.Test;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Hanger;
import frc.robot.subsystem.Runway;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    public static NetworkTable tblMain = NetworkTableInstance.getDefault().getTable("MO Data");
    public static NetworkTable tblPeriod = tblMain.getSubTable("Period");
    public static NetworkTable tblSubsystem = tblMain.getSubTable("Subsytem");
    public static UsbCamera camMain;

    /**
     * disables subsystems
     */
    public static void disableSubsystems(){
        Chassis.disable();
        Runway.disable();
        Hanger.disable();
    }
    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    @Override
    public void robotInit() {
    enableLiveWindowInTest(true);

        //Setup Driver camera
        try{
            camMain = CameraServer.startAutomaticCapture("Main Camera", 0);
            camMain.setFPS(15);
            camMain.setResolution(128, 80);
            camMain.setBrightness(50);
        } finally {
            //Just ignore camera if it fails
        }

        Chassis.init();
        Runway.init();
        Hanger.init();

        //Wait for NetworkTable connection
        while(!NetworkTableInstance.getDefault().isConnected());

        Autonomous.initDashboard();
        Teleoperated.initDashboard();
        Test.initDashboard();
        Chassis.initDashboard();
        Runway.initDashboard();
        Hanger.initDashboard();
    }

    @Override
    public void robotPeriodic() {
        ButtonManager.updateValues();

        Chassis.updateDashboard();
        Runway.updateDashboard();
    }

    @Override
    public void autonomousInit() {
        Autonomous.init();
    }

    @Override
    public void autonomousPeriodic() {
        Autonomous.periodic();
    }

    @Override
    public void teleopInit() {
        Teleoperated.init();
    }

    @Override
    public void teleopPeriodic() {
        Teleoperated.periodic();
    }

    @Override
    public void disabledInit() {
        Disabled.init();
    }

    @Override
    public void disabledPeriodic() {
        Disabled.periodic();
    }

    @Override
    public void testInit() {
        Test.init();
    }

    @Override
    public void testPeriodic() {
        Test.periodic();
    }

    @Override
    public void simulationInit() {}

    @Override
    public void simulationPeriodic() {}
}
