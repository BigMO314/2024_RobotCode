package frc.robot.periods;

import frc.molib.hid.XboxController;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Hanger;
import frc.robot.subsystem.Runway;

/**
 * Code that runs when the robot is disabled
 */ 
public class Disabled {
    /**
     * prevents other instances of the class being created
     */
    private Disabled(){}

    /**
     * runs once when the robot is disabled
     */
    public static void init(){
        Chassis.disableBrake();
        new XboxController(0).setRumble(0.0);
        Hanger.disableOverride();
    }

    /**
     * Configures the dashboard for the disabled period
     */
    public static void initDashboard() {

    }

    /**
     *updates subsystems
     */
    public static void periodic(){
        Chassis.disable();
        Runway.disable();
        Hanger.disable();
    }
}
