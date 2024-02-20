package frc.robot.subsystem;

import com.ctre.phoenix6.hardware.TalonFX;

/**
 * Manages the Hanger
 */
public class Hanger {

    private static TalonFX hanger_L= new TalonFX(8);
    private static TalonFX hanger_R = new TalonFX(9);
    private static double mLeftHangerPower = 0.0;
    private static double mRightHangerPower = 0.0;

    /**
     * prevents other instances of the class being made
     */
    private Hanger(){}

    /**
     * sets motor speeds for the hanger
     * @param power
     */
    public static void setHangerPower(double power){
        mLeftHangerPower = power;
        mRightHangerPower = power;
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
        hanger_L.setInverted(true);
    }

    /**
     * sets up dashboard
     */
    public static void initDashboard(){}

    /**
     * loops and updates motor powers
     */
    public static void periodic(){
        hanger_L.set(mLeftHangerPower);
        hanger_R.set(mRightHangerPower);
    }
}
