package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.molib.dashboard.Entry;
import frc.molib.lights.DigitalLight;
import frc.molib.sensors.DigitalInput;
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
    private static DigitalInput phoLoaded = new DigitalInput(0, false);

    private static DigitalLight ledIntake = new DigitalLight(PneumaticsModuleType.CTREPCM, 0);

    private static double mTopReelPower = 0.0;
    private static double mBottomReelPower = 0.0;
    private static double mDirectorPower = 0.0;

    /**
     * Stops other instances of the Runway being made
     */
    private Runway(){}

    /**
     * Disables entire Runway, no more shashaying
     */
    public static void disable(){
        disableDirector();
        disableReels();
        disableLED();
    }

    /**
     * Configures runway Motors
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
     * Sets up the dashboard
     */
    public static void initDashboard(){

    }

    public static void updateDashboard() {
        entLoaded.set(isLoaded());
    }

    public static boolean isLoaded(){
        return phoLoaded.get();
    }
    
    /**
     * Sets the top and botom reel power to 0
     */
    public static void disableReels(){
        setReelPower(0.0, 0.0);
    }

    /**
     * Enables the reels
     */
    public static void speakerShot(){
        setReelPower(0.65, 0.95);
    }

    public static void ampShot(){
        setReelPower(0.15, 0.20);
    }

    /**
     * Sets the reel power
     * @param topPower [-1.0 to 1.0]
     * @param bottomPower [-1.0 to 1.0]
     */

    public static void setReelPower(double topPower, double bottomPower){
        mTopReelPower = topPower;
        mBottomReelPower = bottomPower;
    }

    /**
     * Reverses the reels, was 0.5
     */
    public static void reverseReels(){
        setReelPower(-0.20, -0.20);
    }

    /**
     * Disables the director
     */
    public static void disableDirector(){
        setDirectorPower(0.0);
    }

    /**
     * Fires the director
     */
    public static void enableDirector(){
        setDirectorPower(1.0);
       
    }

    /**
     * Rehires the director
     */
    public static void reverseDirector(){
        setDirectorPower(-0.5);
        
    }

    /**
     * Sets the director speed
     * @param directorPower
     */
    public static void setDirectorPower(double directorPower){
        mDirectorPower = directorPower;
    }

    /**
     * Turns on the LEDs
     */
    public static void enableLED(){
        ledIntake.turnOn();
    }

    /**
     * Turns off the LEDs
     */
    public static void disableLED(){
        ledIntake.turnOff();
    }

    /**
     * Updates motor power
     */
    public static void periodic(){
        if(isLoaded()){
            mDirectorPower = MathUtil.clamp(mDirectorPower, 0.0, 1.0);
        }

        mtrReel_T.set(mTopReelPower);
        mtrReel_B.set(mBottomReelPower);
        mtrDirector.set(ControlMode.PercentOutput, mDirectorPower);
    }
}
