package frc.robot.periods;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.molib.buttons.Button;
import frc.molib.buttons.ButtonManager;
import frc.molib.hid.Joystick;
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
        TORTOISE("Tortoise", .05),
        SLOW("Slow", .40),
        NORMAL("Normal", .70),
        FAST("Fast", .95);

        public final String label;
        public final double percentage;

        private SpeedPercentage(String label, double percentage){
            this.label = label;
            this.percentage = percentage;
        }
    }
   
    public static enum DriveType{
        TANK("Tank"),
        CHEESY("Cheesy"),
        ARCADE("Arcade");

        public final String label;
        private DriveType(String label){
            this.label = label;
        }
        @Override public String toString(){
            return label;
        }
    }
   private static SendableChooser<SpeedPercentage> chsSpeedPercentage = new SendableChooser<SpeedPercentage>();
   private static SpeedPercentage mSelectedSpeedPercentage;
   private static SendableChooser<DriveType> chsDriveType = new SendableChooser<DriveType>();
   private static DriveType mSelectedDriveType;
   private static XboxController ctlDrive = new XboxController(0);
    private static XboxController ctlOperator = new XboxController(1);
    //Make buttons here
    private static final Button btnEnableReel = new Button(){
        @Override public boolean get() { return ctlOperator.getRightTrigger(); }
    };

    private static final Button btnReveseReel = new Button(){
        @Override public boolean get() { return ctlOperator.getLeftTrigger(); }
    };

    private static final Button btnEnableDirector = new Button(){
        @Override public boolean get() { return ctlDrive.getRightTrigger(); }
    };
    private static final Button btnAmpShot = new Button(){
        @Override public boolean get() {return ctlOperator.getRightBumper();}
    };

    private static final Button btnRaiseHanger = new Button(){
        @Override public boolean get() { return ctlDrive.getAButton(); }
    };

    private static final Button btnLowerHanger = new Button(){
        @Override public boolean get() { return ctlDrive.getBButton(); }
    };

 

    /**
     * prevents other instances of the Teleoperated class being made
     */
    private Teleoperated(){}

    /**
     * Runs once at the beginning of Tele-Op and configures motors
     */
    public static void init(){
        ctlDrive.configYAxisInverted(true);
        mSelectedSpeedPercentage  = chsSpeedPercentage.getSelected();
        ButtonManager.clearFlags();
        mSelectedDriveType = chsDriveType.getSelected();
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

        SmartDashboard.putData("Zoomy Percentage", chsSpeedPercentage);

        chsDriveType.addOption(DriveType.TANK.label, DriveType.TANK);
        chsDriveType.addOption(DriveType.ARCADE.label, DriveType.ARCADE);
        chsDriveType.addOption(DriveType.CHEESY.label, DriveType.CHEESY);

        chsDriveType.setDefaultOption(DriveType.TANK.label, DriveType.TANK);

        SmartDashboard.putData("Drive Type", chsDriveType);
    }
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
    private static void setArcadeDrive(double throttle, double steering){
        setTankDrive(throttle + steering, throttle - steering);
    }

    /**
     *loops and updates values      
     */
    public static void periodic(){

        
        double speedPercentage = mSelectedSpeedPercentage.percentage;
        if(mSelectedDriveType == DriveType.ARCADE){
            setArcadeDrive(Math.signum(ctlDrive.getLeftY())*(ctlDrive.getLeftY()*ctlDrive.getLeftY()*speedPercentage), (Math.signum(ctlDrive.getLeftX()))*(ctlDrive.getLeftX()*ctlDrive.getLeftX())*speedPercentage);
        }else if(mSelectedDriveType == DriveType.CHEESY){
            setArcadeDrive(Math.signum(ctlDrive.getLeftY())*(ctlDrive.getLeftY()*ctlDrive.getLeftY()*speedPercentage), (Math.signum(ctlDrive.getRightX()))*(ctlDrive.getRightX()*ctlDrive.getRightX())*speedPercentage);
        }else {
            setTankDrive(Math.signum(ctlDrive.getLeftY())*(ctlDrive.getLeftY()*ctlDrive.getLeftY())*speedPercentage, (Math.signum(ctlDrive.getRightY()))*(ctlDrive.getRightY()*ctlDrive.getRightY())*speedPercentage);
        }

        if(btnEnableReel.get()){
            Runway.enableReels();
            Runway.enableLED();
            if(btnEnableDirector.get()) {
                Runway.enableDirector();
            } else {
                Runway.disableDirector();
            }
        } else if(btnAmpShot.get()){
            Runway.setReelPower(.15, .20);
            if(btnEnableDirector.get()) {
                Runway.enableDirector();
            } else {
                Runway.disableDirector();
            }
        }else if(btnReveseReel.get()){
            Runway.reverseReels();
            Runway.reverseDirector();
            Runway.enableLED();
        }else{
            Runway.disable();
        }

        if(btnLowerHanger.get()){
            Hanger.lowerHanger();
        }else if(btnRaiseHanger.get()){
            Hanger.raiseHanger();
        }else{
            Hanger.disable();
        }


        //Update Subsystems
        Chassis.periodic();
        Runway.periodic();
        Hanger.periodic();
    }
}
