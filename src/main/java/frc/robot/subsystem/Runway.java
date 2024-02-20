package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.molib.dashboard.Entry;
import frc.robot.Robot;

/**
 * Manages the Runway
 */
public class Runway {
    private static NetworkTable tblRunway = Robot.tblSubsystem.getSubTable("Runway");
    private static Entry<Boolean> entLoaded = new Entry<Boolean>(tblRunway, "Loaded");

    private static VictorSPX mtrDirector = new VictorSPX(5);
    private static TalonFX mtrReel_T = new TalonFX(6);
    private static TalonFX mtrReel_B = new TalonFX(7);
    private static DigitalInput limLoaded = new DigitalInput(0);

    //private static DigitalLight ledIntake = new DigitalLight(PneumaticsModuleType.CTREPCM, 0);

    private static double mTopReelPower = 0.0;
    private static double mBottomReelPower = 0.0;
    private static double mDirectorPower = 0.0;

    /**
     * stops other instances of the Runway being made
     */
    private Runway(){}

    /**
     * disables entire Runway, no more shashaying
     */
    public static void disable(){
        disableDirector();
        disableReels();
        //disableLED();
    }

    /**
     * configures runway Motors
     */
    public static void init(){
        mtrReel_T.setInverted(true);
        mtrReel_B.setInverted(true);
        mtrDirector.setInverted(true);

        mtrReel_T.setNeutralMode(NeutralModeValue.Coast);
        mtrReel_B.setNeutralMode(NeutralModeValue.Coast);
        mtrDirector.setNeutralMode(NeutralMode.Brake);
    }

    /**
     * sets up the dashboard
     */
    public static void initDashboard(){

    }

    public static void updateDashboard() {
        entLoaded.set(!limLoaded.get());
    }
    
    /**
     * sets the top and botom reel power to 0
     */
    public static void disableReels(){
        setReelPower(0.0, 0.0);
    }

    /**
     * enables the reels
     */
    public static void enableReels(){
        setReelPower(0.75, 0.85);
    }

    /**
     * reverses the reels
     */
    public static void reverseReels(){
        setReelPower(-0.5, -0.5);
    }

    /**
     * sets the reel power
     * @param topPower [-1.0 to 1.0]
     * @param bottomPower [-1.0 to 1.0]
     */

    public static void setReelPower(double topPower, double bottomPower){
        mTopReelPower = topPower;
        mBottomReelPower = bottomPower;
    }

    /**
     * disables the director
     */
    public static void disableDirector(){
        setDirectorPower(0.0);
    }

    /**
     * fires the director
     */
    public static void enableDirector(){
        setDirectorPower(1.0);
       
    }

    /**
     * rehires the director
     */
    public static void reverseDirector(){
        setDirectorPower(-1.0);
        
    }

    /**
     * sets the director speed
     * @param directorPower
     */
    public static void setDirectorPower(double directorPower){
        mDirectorPower = directorPower;
    }

    /**
     * turns on the LEDs
     */
    public static void enableLED(){
        //ledIntake.turnOn();
    }

    /**
     * turns off the LEDs
     */
    public static void disableLED(){
        //ledIntake.turnOff();
    }

    /**
     * updates motor power
     */
    public static void periodic(){
        if(!limLoaded.get()){
            mDirectorPower = MathUtil.clamp(mDirectorPower, 0.0, 1.0);
            //setDirectorPower(0.0);
        }

        mtrReel_T.set(mTopReelPower);
        mtrReel_B.set(mBottomReelPower);
        mtrDirector.set(ControlMode.PercentOutput, mDirectorPower);
    }
}
