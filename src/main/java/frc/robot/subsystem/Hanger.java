package frc.robot.subsystem;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTable;
import frc.molib.dashboard.Entry;
import frc.robot.Robot;

/**
 * Manages the Hanger
 */
public class Hanger {

    private static NetworkTable tblHanger = Robot.tblSubsystem.getSubTable("Hanger");
    private static Entry<Double> entWinchPosition = new Entry<Double>(tblHanger, "Winch Position");

    private static TalonFX mtrWinch= new TalonFX(8);
    private static double mHangerPower = 0.0;
    private static boolean mOverrideSafety = false;

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
        resetPosition();
    }

    /**
     * sets up dashboard
     */
    public static void initDashboard(){
        
    }
    
    public static void updateDashboard(){
        entWinchPosition.set(getPosition());
    }

    /**
     * disables Hanger related motors
     */
    public static void disable(){
        setHangerPower(0.0);
    }

    public static void enableOverride() { mOverrideSafety = true; }

    public static void disableOverride() { mOverrideSafety = false; }



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
    public static void extend(){
        setHangerPower(1.0);
    }

    /**
     * lowers the Hanger
     */
    public static void retract(){
        setHangerPower(-1.0);
    }

    public static double getPosition(){
        return mtrWinch.getPosition().getValue();
    }

    public static void resetPosition(){
        mtrWinch.setPosition(0.0);
    }

    /**
     * loops and updates motor powers
     */
    public static void periodic(){
        if(!mOverrideSafety) {
            if(mtrWinch.getPosition().getValue() <= 0.0){
                mHangerPower = MathUtil.clamp(mHangerPower, 0.0, 1.0);
            }
        }

        mtrWinch.set(mHangerPower);

    }
}
