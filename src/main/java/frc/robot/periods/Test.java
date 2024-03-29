package frc.robot.periods;

import edu.wpi.first.networktables.NetworkTable;
import frc.molib.buttons.Button;
import frc.molib.buttons.ButtonManager;
import frc.molib.dashboard.Entry;
import frc.molib.hid.XboxController;
import frc.robot.Robot;
import frc.robot.subsystem.Backstage;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Hanger;
import frc.robot.subsystem.Runway;

//TODO: Hanger reset options

/**
 * Implement Test Code and make test adjustments without affecting the main code
 */
@SuppressWarnings("unused")
public class Test {

    private static NetworkTable tblTest = Robot.tblPeriod.getSubTable("Test");
    private static Entry<Double> entTopReelPower = new Entry<Double>(tblTest, "Top Reel Power");
    private static Entry<Double> entBottomReelPower = new Entry<Double>(tblTest, "Bottom Reel Power");
    private static Entry<Double> entDirectorPower = new Entry<Double>(tblTest, "Director Power");

    private static XboxController ctlDriver = new XboxController(0);
    private static XboxController ctlOperator = new XboxController(1);

    private static Button btnResetDistance = new Button(){
        @Override public boolean get(){ return ctlOperator.getStartButton(); }
    };

    private static Button btnDriveEightFeet = new Button(){
        @Override public boolean get(){return ctlOperator.getAButton();}
    };

    private static Button btnDriveFourFeet = new Button(){
        @Override public boolean get(){return ctlOperator.getXButton();}
    };

    private static Button btnDriveTwoFeet = new Button(){
        @Override public boolean get(){return ctlOperator.getYButton();}
    };

    private static Button btnDriveOneFeet = new Button(){
        @Override public boolean get(){return ctlOperator.getBButton();}
    };

    private static Button btnTurn90 = new Button(){
        @Override public boolean get(){return ctlOperator.getRightBumper();}
    };

    private static Button btnDisablePIDs = new Button(){
        @Override public boolean get(){return ctlOperator.getBackButton();}
    };

    private static Button btnReels = new Button(){
        @Override public boolean get(){return ctlOperator.getYButton();}
    };

    private static Button btnBottomReel = new Button(){
        @Override public boolean get(){return ctlOperator.getAButton();        }
    };

    private static Button btnEnableDirector = new Button(){
        @Override public boolean get(){return ctlOperator.getRightTrigger();}
    };

    private static Button btnEnableLights = new Button(){
        @Override public boolean get(){return ctlOperator.getBButton();}
    };

    private static Button btnIntake = new Button() {
        @Override public boolean get() { return ctlOperator.getLeftTrigger(); }
    };

    private static Button btnFullSpeedShot = new Button(){
        @Override public boolean get(){return ctlOperator.getLeftBumper();}
    };

    private static Button btnHanger_ResetFlag = new Button() {
        @Override public boolean get() { return ctlDriver.getStartButton() && ctlDriver.getBackButton(); }
    };

    private static Button btnHanger_Enable = new Button() {
        @Override public boolean get() { return ctlDriver.getPOV() == 0; }
    };

    private static Button btnHanger_Reverse = new Button() {
        @Override public boolean get() { return ctlDriver.getPOV() == 180; }
    };

    private static Button btnRaiseBackstage = new Button() {
        @Override public boolean get() { return ctlOperator.getPOV() == 0; }
    };

    private static Button btnLowerBackstage = new Button() {
        @Override public boolean get() { return ctlOperator.getPOV() == 180; }
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

    //Runway Controls
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

    //Hanger Controls
        if(btnHanger_Enable.get())
            Hanger.setWinchPower(0.20);
        else if(btnHanger_Reverse.get())
            Hanger.setWinchPower(-0.20);
        else
            Hanger.disable();

    //Backstage Controls
        if(btnRaiseBackstage.get()) 
            Backstage.setPivotPower(-0.10);
        else if(btnLowerBackstage.get())
            Backstage.setPivotPower(0.10);
        else Backstage.disablePivot();

        //Update Subsystems
        Chassis.periodic();
        Runway.periodic();
        Hanger.periodic();
        Backstage.periodic();
    }
    
}
