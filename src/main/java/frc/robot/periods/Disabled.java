package frc.robot.periods;

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
