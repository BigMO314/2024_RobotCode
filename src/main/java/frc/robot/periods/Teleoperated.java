package frc.robot.periods;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.molib.buttons.Button;
import frc.molib.buttons.ButtonManager;
import frc.molib.hid.XboxController;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Hanger;
import frc.robot.subsystem.Runway;

/** 
 * Runs during the Teleoperated Period
 */
@SuppressWarnings("unused")
public class Teleoperated {

    /** 
     * Options for robot speed during teleoperated
     */
    public static enum SpeedPercentage{
        TORTOISE("Tortoise - 10%", 0.05, 0.10, 0.20),
        SLOW("Slow - 40%", 0.15, 0.40, 0.60),
        NORMAL("Normal - 75%", 0.15, 0.75, 0.90),
        FAST("Fast - 85%", 0.15, 0.85, 1.00);

        public final String label;
        public final double slow;    
        public final double standard;
        public final double boost;

        private SpeedPercentage(String label, double slow, double standard, double boost){
            this.label = label;
            this.slow = slow;
            this.standard = standard;
            this.boost = boost;
        }
    }
   
    /*
     * Options for drive style during teleoperated
     */
    public static enum DriveType{
        TANK("Tank"),
        CHEDDER("Chedder"),
        ARKADE("Arkade");

        public final String label;
        private DriveType(String label){
            this.label = label;
        }
        @Override public String toString(){
            return label;
        }
    }

    //OBJECTS//

    //Sendable choosers
    private static SendableChooser<SpeedPercentage> chsSpeedPercentage = new SendableChooser<SpeedPercentage>();
    private static SendableChooser<DriveType> chsDriveType = new SendableChooser<DriveType>();

    //Buffer variables 
    private static SpeedPercentage mSelectedSpeedPercentage;
    private static DriveType mSelectedDriveType;

    //Make controllers
    private static XboxController ctlDriver = new XboxController(0);
    private static XboxController ctlOperator = new XboxController(1);

    //Make timer
    private static Timer tmrSpinUp = new Timer();


    //BUTTONS//

    //Shots
    private static final Button btnSpeakerShot = new Button(){
        @Override public boolean get() { return ctlOperator.getRightTrigger(); }
    };

    private static final Button btnAmpShot = new Button(){
        @Override public boolean get() {return ctlOperator.getRightBumper(); }
    };

    private static final Button btnFieldShot = new Button(){
        @Override public boolean get() {return ctlOperator.getBButton(); }
    };
    
    //Speed variability
    private static final Button btnBoost = new Button(){
        @Override public boolean get() {return ctlDriver.getRightTrigger() || ctlDriver.getLeftTrigger();}
    };

    private static final Button btnSlow = new Button(){
        @Override public boolean get() {return ctlDriver.getRightBumper();}
    };

    //Brake
    private static final Button btnBrake = new Button(){
        @Override public boolean get() {return ctlDriver.getLeftBumper();}
    };

    //Intake
    private static final Button btnIntake = new Button(){
        @Override public boolean get() { return ctlOperator.getLeftTrigger(); }
    };

    //Floor-intake specific
    private static final Button btnReverseFloorIntake = new Button(){
        @Override public boolean get() { return ctlOperator.getXButton(); }
    };

    private static final Button btnRaiseFloorIntake = new Button(){
        @Override public boolean get() { return ctlOperator.getYButton(); }
    };
    
    private static final Button btnLowerFloorIntake = new Button(){
        @Override public boolean get() { return ctlOperator.getAButton(); }
    };    

    //Hanger
    private static final Button btnHanger_Reset = new Button() {
        @Override public boolean get() { return ctlDriver.getStartButton() && ctlDriver.getBackButton(); };
    };

    private static final Button btnHanger_Override = new Button() {
        @Override public boolean get() { return ctlOperator.getStartButton(); }
    };

    private static final Button btnEnableHanger = new Button(){
        @Override public boolean get() { return ctlDriver.getPOV() == 0; }
    };

    private static final Button btnRetractHanger = new Button(){
        @Override public boolean get() { return ctlDriver.getPOV() == 180; }
    };


    /**
     * Prevents other instances of the Teleoperated class being made
     */
    private Teleoperated(){}


    /**
     * Runs once at the beginning of Tele-Op and configures motors
     */
    public static void init(){

        ButtonManager.clearFlags();

        ctlDriver.configYAxisInverted(true);
        ctlDriver.setRumble(0.0);

        //Get selected enumerations
        mSelectedSpeedPercentage  = chsSpeedPercentage.getSelected();
        mSelectedDriveType = chsDriveType.getSelected();

        //Timer
        tmrSpinUp.restart();
    }


    /**
     * Initializes and pushes the dashboard options
     */
    public static void initDashboard(){
        chsSpeedPercentage.addOption(SpeedPercentage.TORTOISE.label, SpeedPercentage.TORTOISE);
        chsSpeedPercentage.addOption(SpeedPercentage.SLOW.label, SpeedPercentage.SLOW);
        chsSpeedPercentage.addOption(SpeedPercentage.NORMAL.label, SpeedPercentage.NORMAL);
        chsSpeedPercentage.addOption(SpeedPercentage.FAST.label, SpeedPercentage.FAST);

        chsSpeedPercentage.setDefaultOption(SpeedPercentage.NORMAL.label, SpeedPercentage.NORMAL);

        SmartDashboard.putData("Speed Percentage", chsSpeedPercentage);

        chsDriveType.addOption(DriveType.TANK.label, DriveType.TANK);
        chsDriveType.addOption(DriveType.ARKADE.label, DriveType.ARKADE);
        chsDriveType.addOption(DriveType.CHEDDER.label, DriveType.CHEDDER);

        chsDriveType.setDefaultOption(DriveType.CHEDDER.label, DriveType.CHEDDER);

        SmartDashboard.putData("Drive Type", chsDriveType);
    }

    //MATH//
    /**
     *math for tank drive
     * 
     * @param leftPower [-1.0 to 1.0]
     * @param rightPower [-1.0 to 1.0]
     */
    private static void setTankDrive(double leftPower, double rightPower){
        Chassis.setDrivePower(leftPower, rightPower);
    }

    /**
     * math for arcade drive
     * @param throttle [-1.0 to 1.0] forward backward
     * @param steering [-1.0 to 1.0] left right
     */
    //TODO: check if button slow is necessary
    private static void setArcadeDrive(double throttle, double steering){
        if(Math.abs(throttle) > Math.abs(steering) && !btnSlow.get()){
            steering = MathUtil.clamp(( steering / (Math.abs(throttle) * 3.00 )), -1.0, 1.0);
        }
        setTankDrive(throttle + steering, throttle - steering);
    }


    /**
     *loops and updates values      
     */
    public static void periodic(){

        if(DriverStation.getMatchTime() > 15.0 && DriverStation.getMatchTime() < 20.0) ctlDriver.setRumble(1.0);

        //Create speedPercentage
        double speedPercentage;

        //Set speedPercentage based on selected option
        if(btnSlow.get()){
            speedPercentage = mSelectedSpeedPercentage.slow;
        }else if(btnBoost.get()){
            speedPercentage = mSelectedSpeedPercentage.boost;
        }else{
            speedPercentage = mSelectedSpeedPercentage.standard;
        }
        
        //Math of inputs for selected drive styles
        if(mSelectedDriveType == DriveType.ARKADE){
            setArcadeDrive(Math.signum(ctlDriver.getLeftY())*(ctlDriver.getLeftY()*ctlDriver.getLeftY()) * speedPercentage, (Math.signum(ctlDriver.getLeftX()))*(ctlDriver.getLeftX()*ctlDriver.getLeftX()) * speedPercentage);
        }else if(mSelectedDriveType == DriveType.CHEDDER){
            setArcadeDrive(Math.signum(ctlDriver.getLeftY())*(ctlDriver.getLeftY()*ctlDriver.getLeftY()) * speedPercentage, (Math.signum(ctlDriver.getRightX()))*(ctlDriver.getRightX()*ctlDriver.getRightX()) * speedPercentage);
        }else {
            setTankDrive(Math.signum(ctlDriver.getLeftY())*(ctlDriver.getLeftY()*ctlDriver.getLeftY()) * speedPercentage, (Math.signum(ctlDriver.getRightY()))*(ctlDriver.getRightY()*ctlDriver.getRightY()) * speedPercentage);
        }

        //Brake button logic
        if(btnBrake.getPressed()){
            Chassis.enableBrake();
        }else if(btnBrake.getReleased()){
            Chassis.disableBrake();
        }

        //Runway logic
        if(btnSpeakerShot.get()) {
            Runway.speakerShot();
            if(tmrSpinUp.get() > 0.25){
                Runway.enableDirector();
            } else {
                Runway.disableDirector();
            }
        } else if (btnAmpShot.get()) {
            Runway.ampShot();
            if(tmrSpinUp.get() > 0.25){
                Runway.enableDirector();
            } else {
                Runway.disableDirector();
            }
        } else if (btnFieldShot.get()){
            Runway.fieldShot();
            if(tmrSpinUp.get() > 0.25){
                Runway.enableDirector();
            } else {
                Runway.disableDirector();
            }
        } else if (btnIntake.get()) {
            Runway.reverseReels();
            Runway.reverseDirector();
            Runway.enableLED();
        } else {
            Runway.disable();
            tmrSpinUp.reset();
        }
/*
        //Hanger function
        if(btnHanger_Override.get()) {
            Hanger.enableOverride();
        } else {
            Hanger.disableOverride();
        }

        if(btnHanger_Reset.getPressed()) {
            Hanger.resetPosition();
        }
*/
        if(btnEnableHanger.get()){
            Hanger.enable();
        }else{
            Hanger.disable();
        }

        //Update Subsystems
        Chassis.periodic();
        Runway.periodic();
        Hanger.periodic();
    }
}
