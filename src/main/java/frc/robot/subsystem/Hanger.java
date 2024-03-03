package frc.robot.subsystem;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.MathUtil;

/**
 * Manages the Hanger
 */
public class Hanger {

    private static TalonFX hanger= new TalonFX(8);
    private static double mHangerPower = 0.0;

    /**
     * prevents other instances of the class being made
     */
    private Hanger(){}

    /**
     * sets motor speeds for the hanger
     * @param power
     */
    public static void setHangerPower(double power){
        mHangerPower = power;
    }    

    /**
     * raises the Hanger
     */
    public static void raiseHanger(){
        setHangerPower(0.7);
    }

    /**
     * lowers the Hanger
     */
    public static void lowerHanger(){
        setHangerPower(-0.7);
    }

    /**
     * disables Hanger related motors
     */
    public static void disable(){
        setHangerPower(0.0);
    }

    /**
     * configures motors
     */
    public static void init(){
        hanger.setInverted(true);
    }

    /**
     * sets up dashboard
     */
    public static void initDashboard(){}

    /**
     * loops and updates motor powers
     */
    public static void periodic(){
        hanger.set(mHangerPower);
      
        if(hanger.getPosition().getValue() <= 0.0){
            mHangerPower = MathUtil.clamp(mHangerPower, 0.0, 1.0);
        }
    }
}
