package frc.robot.subsystem;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTable;
import frc.molib.dashboard.Entry;
import frc.molib.sensors.DigitalInput;
import frc.robot.Robot;


/**
 * Manages the Hanger
 */
@SuppressWarnings("unused")
public class Hanger {

    private static NetworkTable tblHanger = Robot.tblSubsystem.getSubTable("Hanger");
    private static Entry<Double> entWinchPosition = new Entry<Double>(tblHanger, "Winch Position");
    private static Entry<Boolean> entLeftRetracted = new Entry<Boolean>(tblHanger, "Left Retracted");
    private static Entry<Boolean> entRightRetracted = new Entry<Boolean>(tblHanger, "Right Retracted");
    private static Entry<Boolean> entRetracted = new Entry<Boolean>(tblHanger, "Retracted");
    private static Entry<Boolean> entHasExtended = new Entry<Boolean>(tblHanger, "Has Extended");
    private static Entry<Boolean> entSafetyStopped = new Entry<Boolean>(tblHanger, "Safety Stopped");


    private static DigitalInput limHangerRetracted_L = new DigitalInput(2, true);
    private static DigitalInput limHangerRetracted_R = new DigitalInput(1, true);

    

    private static TalonFX mtrWinch= new TalonFX(8);

    
    private static double mWinchPower = 0.0;
    private static boolean mOverrideSafety = false;
    private static boolean mHasExtended = false;

    private static double winchHalfway = 120.0;


    /**
     * prevents other instances of the class being made
     */
    private Hanger(){}

    /**
     * configures motors
     */
    public static void init(){
        mtrWinch.setInverted(true);
        mtrWinch.setNeutralMode(NeutralModeValue.Brake);
        //resetPosition();
    }

    /**
     * sets up dashboard
     */
    public static void initDashboard(){
        
    }
    
    public static void updateDashboard(){
        entWinchPosition.set(getPosition());
        entLeftRetracted.set(limHangerRetracted_L.get());
        entRightRetracted.set(limHangerRetracted_R.get());
        entRetracted.set(isRetracted());
    }

    /**
     * disables Hanger related motors
     */
    public static void disable(){
        setWinchPower(0.0);
    }


    //HANGER LIMITS

    public static boolean isRetracted(){
        return limHangerRetracted_L.get() || limHangerRetracted_R.get();
    }

    /**
     * sets motor speeds for the hanger
     * @param power
     */
    public static void setWinchPower(double power){
        mWinchPower = power;
    }    

    /**
     * raises the Hanger
     */
    public static void enable(){
        setWinchPower(0.50);
    }

    public static void retract(){
        setWinchPower(-0.50);
    }

    public static double getPosition(){
        return mtrWinch.getPosition().getValue();
    }

    public static void setPositionZero(){
        mtrWinch.setPosition(0.0);
    }

    public static void setPositionHalfway(){
        mtrWinch.setPosition(winchHalfway);
    }

    public static void resetFlag(){
        mHasExtended = false;
    }

    /**
     * loops and updates motor powers
     */
    public static void periodic(){
        /*if(!mOverrideSafety) {
            if(isRetracted()){
                mHangerPower = MathUtil.clamp(mHangerPower, 0.0, 1.0);
            }
        }*/

        /*if(!isRetracted()){
            mHasExtended = true;
        }

        if(isRetracted() && mHasExtended){
            setWinchPower(0.0);
        }*/


        if(isRetracted()){
            if(getPosition() <= winchHalfway){
                setPositionZero();
                mWinchPower = MathUtil.clamp(mWinchPower, 0.0, 1.0);
            } else {
                mWinchPower = MathUtil.clamp(mWinchPower, -1.0, 0.0);
            }
        }

        mtrWinch.set(mWinchPower);

    }
}
