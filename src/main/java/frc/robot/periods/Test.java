package frc.robot.periods;

import edu.wpi.first.networktables.NetworkTable;
import frc.molib.buttons.Button;
import frc.molib.buttons.ButtonManager;
import frc.molib.dashboard.Entry;
import frc.molib.hid.XboxController;
import frc.robot.Robot;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Runway;

/**
 * Implement Test Code and make test adjustments without affecting the main code
 */
@SuppressWarnings("unused")
public class Test {

    private static NetworkTable tblTest = Robot.tblPeriod.getSubTable("Test");
    private static Entry<Double> entTopReelPower = new Entry<Double>(tblTest, "Top Reel Power");
    private static Entry<Double> entBottomReelPower = new Entry<Double>(tblTest, "Bottom Reel Power");
    private static Entry<Double> entDirectorPower = new Entry<Double>(tblTest, "Director Power");

    private static XboxController ctlTest = new XboxController(1);

    private static Button btnResetDistance = new Button(){
        @Override public boolean get(){ return ctlTest.getStartButton(); }
    };

    private static Button btnDriveEightFeet = new Button(){
        @Override public boolean get(){return ctlTest.getAButton();}
    };

    private static Button btnDriveFourFeet = new Button(){
        @Override public boolean get(){return ctlTest.getXButton();}
    };

    private static Button btnDriveTwoFeet = new Button(){
        @Override public boolean get(){return ctlTest.getYButton();}
    };

    private static Button btnDriveOneFeet = new Button(){
        @Override public boolean get(){return ctlTest.getBButton();}
    };

    private static Button btnTurn90 = new Button(){
        @Override public boolean get(){return ctlTest.getRightBumper();}
    };

    private static Button btnDisablePIDs = new Button(){
        @Override public boolean get(){return ctlTest.getBackButton();}
    };

    private static Button btnReels = new Button(){
        @Override public boolean get(){return ctlTest.getYButton();}
    };

    private static Button btnBottomReel = new Button(){
        @Override public boolean get(){return ctlTest.getAButton();        }
    };

    private static Button btnEnableDirector = new Button(){
        @Override public boolean get(){return ctlTest.getRightTrigger();}
    };

    private static Button btnEnableLights = new Button(){
        @Override public boolean get(){return ctlTest.getBButton();}
    };

    private static Button btnIntake = new Button() {
        @Override public boolean get() { return ctlTest.getLeftTrigger(); }
    };

    private static Button btnFullSpeedShot = new Button(){
        @Override public boolean get(){return ctlTest.getLeftBumper();}
    };
    /**
     * Constructor
     */
    private Test(){}

    /**
     * runs when you start the test period
     */
    public static void init(){
        ButtonManager.clearFlags();
        
        Chassis.disablePIDs();
        Robot.disableSubsystems();
    }

    /**
     * to intialize any dashboard variables
     */
    public static void initDashboard() {
        entTopReelPower.set(0.0);
        entBottomReelPower.set(0.0);
        entDirectorPower.set(0.0);
    }

    /**
     *Updates motors pulls inputs from drivers statoin
     */
    public static void periodic() {

        if(btnReels.get()){
            Runway.setReelPower(entTopReelPower.get(), entBottomReelPower.get());
            if(btnEnableDirector.get()) Runway.enableDirector();
            else Runway.disableDirector();
        }else if( btnIntake.get()){
            Runway.reverseReels();
            Runway.reverseDirector();
        }else if(btnFullSpeedShot.get()){
            Runway.setReelPower(1.0, 1.0);
            if(btnEnableDirector.get()) Runway.enableDirector();
            else Runway.disableDirector();
        }else{
            Runway.disable();
        }
/*
        if(btnEnableLights.get()){
            Runway.enableLED();
        }else{
            Runway.disableLED();
        }
*/

        if(btnResetDistance.get()){
            Chassis.resetDistance();
            Chassis.resetAngle();
        }

        if(btnDriveOneFeet.getPressed()){
            Chassis.resetDistance();
            Chassis.goToDistance(-12.00);
        }

        if(btnDriveTwoFeet.getPressed()){
            Chassis.resetDistance();
            Chassis.goToDistance(-24.00);
        }

        if(btnDriveFourFeet.getPressed()){
            Chassis.resetDistance();
            Chassis.goToDistance(-48.00);
        }

        if(btnDriveEightFeet.getPressed()){
            Chassis.resetDistance();
            Chassis.goToDistance(-96.00);
        }

        if(btnDisablePIDs.get()){
            Chassis.disablePIDs();
        }

        if (btnTurn90.getPressed()){
            Chassis.resetAngle();
            Chassis.goToAngle(90.0);
        }
        

       // Runway.setDirectorPower(ctlTest.getTriggerAxis());

        //Update Subsystems
        Chassis.periodic();
        Runway.periodic();
        //Hanger.periodic();
    }
    
}
