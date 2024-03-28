package frc.robot.subsystem;

import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import frc.molib.PIDController;
import frc.molib.dashboard.Entry;
import frc.robot.Robot;

/**
Manages the Chassis 
*/
@SuppressWarnings("all")
public class Chassis {//make the Chassis class

    private static NetworkTable tblChassis = Robot.tblSubsystem.getSubTable("Chassis");
    private static Entry<Double> entDrive_Distance_L1 = new Entry<Double>(tblChassis, "Drive Distance - L1");
    private static Entry<Double> entDrive_Distance_L2 = new Entry<Double>(tblChassis, "Drive Distance - L2");
    private static Entry<Double> entDrive_Distance_R1 = new Entry<Double>(tblChassis, "Drive Distance - R1");
    private static Entry<Double> entDrive_Distance_R2 = new Entry<Double>(tblChassis, "Drive Distance - R2");
    private static Entry<Double> entDrive_Distance_Average = new Entry<Double>(tblChassis, "Drive Distance - Average");
    private static Entry<Double> entDrive_Angle = new Entry<Double>(tblChassis, "Gyro");
    private static Entry<Boolean> entDrive_AtDistance = new Entry<Boolean>(tblChassis, "Is At Distance");
    private static Entry<Double> entPID_Angle = new Entry<Double>(tblChassis, "PID Gyro");

    private static TalonFX mtrDrive_L1 = new TalonFX(1);//make the motors
    private static TalonFX mtrDrive_L2 = new TalonFX(2);
    private static TalonFX mtrDrive_R1 = new TalonFX(3);
    private static TalonFX mtrDrive_R2 = new TalonFX(4);

    private static ADXRS450_Gyro gyrDrive_Angle = new ADXRS450_Gyro(); 

    private static PIDController pidDrive_Distance = new PIDController(0.25, 0.0, 0.0);
    private static PIDController pidDrive_Angle = new PIDController(0.025, 0.000005, 0.001);
    private static double mLeftDrivePower = 0.0;//private variables within the class for left and right power
    private static double mRightDrivePower = 0.0;

    private static double mDriveGearRatio = 1.0/6.28;
    /**
     * Prevents seperate instances of the class being made
     */
    private Chassis(){}

    /**
     * disables Chassis motors
     */
    public static void disable(){
        setDrivePower(0.0, 0.0);
        disablePIDs();
    }

    /**
     * runs once and configures motors and PIDS
     */
    public static void init() {//happens once, in this case it happens once the robot turns on
        mtrDrive_L1.setInverted(true);//inverts the left two motors
        mtrDrive_L2.setInverted(true);
        mtrDrive_R1.setInverted(false);
        mtrDrive_R2.setInverted(false);
        //mtrDrive_L2.setControl(new Follower(mtrDrive_L1.getDeviceID(), false));//makes left 2 follow left 1
        //mtrDrive_R2.setControl(new Follower(mtrDrive_R1.getDeviceID(), false));//ditto but right


        gyrDrive_Angle.calibrate();

        pidDrive_Angle.configOutputRange(-0.25, 0.25);
        pidDrive_Angle.configAtSetpointTime(0.25);
        pidDrive_Angle.setTolerance(0.25);
        pidDrive_Distance.configOutputRange(-0.5, 0.5);
        pidDrive_Distance.configAtSetpointTime(0.25);
        pidDrive_Distance.setTolerance(0.25);

        disableDistancePID();
        disableAnglePID();
    }
    
    /**
     * Sets up the dashboard
     */
    public static void initDashboard(){

    }

    /** Updates dashboard values */
    public static void updateDashboard() {
        entDrive_Distance_L1.set(mtrDrive_L1.getPosition().getValue() * mDriveGearRatio * (4.0 * Math.PI));
        entDrive_Distance_L2.set(mtrDrive_L2.getPosition().getValue() * mDriveGearRatio * (4.0 * Math.PI));
        entDrive_Distance_R1.set(mtrDrive_R1.getPosition().getValue() * mDriveGearRatio * (4.0 * Math.PI));
        entDrive_Distance_R2.set(mtrDrive_R2.getPosition().getValue() * mDriveGearRatio * (4.0 * Math.PI));
        entDrive_Distance_Average.set(getDriveDistance());
        entDrive_AtDistance.set(pidDrive_Distance.atSetpoint());
        entDrive_Angle.set(gyrDrive_Angle.getAngle());
        entPID_Angle.set(pidDrive_Angle.calculate(getDriveAngle()));
    }

    /**
     * sets motor speeds
     */
    public static void setDrivePower(double leftPower, double rightPower){//function to set drive power via the function's inputs
        mLeftDrivePower = leftPower;
        mRightDrivePower= rightPower;
    }

    public static void enableBrake(){
        mtrDrive_L1.setNeutralMode(NeutralModeValue.Brake);
        mtrDrive_L2.setNeutralMode(NeutralModeValue.Brake);
        mtrDrive_R1.setNeutralMode(NeutralModeValue.Brake);
        mtrDrive_R2.setNeutralMode(NeutralModeValue.Brake);
    }

    public static void disableBrake(){
        mtrDrive_L1.setNeutralMode(NeutralModeValue.Coast);
        mtrDrive_L2.setNeutralMode(NeutralModeValue.Coast);
        mtrDrive_R1.setNeutralMode(NeutralModeValue.Coast);
        mtrDrive_R2.setNeutralMode(NeutralModeValue.Coast);
    }
    /**
     * returns how far a motor has driven in inches
     * @return
     */
    public static double getDriveDistance(){
        return ((((mtrDrive_L1.getPosition().getValue()+mtrDrive_L2.getPosition().getValue()+mtrDrive_R1.getPosition().getValue()+mtrDrive_R2.getPosition().getValue())/4.0)*(mDriveGearRatio)*(4.0*Math.PI))/0.965);
    }

    /**
     * returns how far a motor has driven in degrees
     * @return
     */
    public static double getDriveAngle(){
        return gyrDrive_Angle.getAngle();
    }

    /**
     * Disables PIDs
     */
    public static void disablePIDs(){
        disableDistancePID();
        disableAnglePID();
    }

    /**
     * disables the distance PID
     */
    public static void disableDistancePID(){
        pidDrive_Distance.disable();
    }

    /**
     * disables the angle PID
     */
    public static void disableAnglePID(){
        pidDrive_Angle.disable();
    }

    /**
     * moves the robot to a distance in inches
     * @param distance
     */
    public static void goToDistance(double distance){
        pidDrive_Distance.setSetpoint(distance);
        pidDrive_Distance.enable();
    }

    /**
     * rotates the robot to a set degree
     * @param angle
     */
    public static void goToAngle(double angle){
        pidDrive_Angle.setSetpoint(angle);
        pidDrive_Angle.enable();
    }

    /**
     * returns a true false to confirm a distance
     * @return
     */
    public static boolean isAtDistance(){
        return pidDrive_Distance.atSetpoint();
    }

    /**
     * returns a true false to confirm a degree
     * @return
     */
    public static boolean isAtAngle(){
        return pidDrive_Angle.atSetpoint();
    }

    /**
     * resets the read distance value
     */
    public static void resetDistance(){
        mtrDrive_L1.setPosition(0.0);
        mtrDrive_L2.setPosition(0.0);
        mtrDrive_R1.setPosition(0.0);
        mtrDrive_R2.setPosition(0.0);
    }

    /**
     * resets the read angle value
     */
    public static void resetAngle(){
        gyrDrive_Angle.reset();
    }

    /**
     * runs continuously to updates motor and PID values
     */
    public static void periodic(){//runs on loop
        if(pidDrive_Distance.isEnabled()){
            setDrivePower(pidDrive_Distance.calculate(getDriveDistance()), pidDrive_Distance.calculate(getDriveDistance()));
        } else if(pidDrive_Angle.isEnabled()){
            setDrivePower(pidDrive_Angle.calculate(getDriveAngle()), -pidDrive_Angle.calculate(getDriveAngle()));
        }
    /*
        entDrive_Distance_L1.set(mtrDrive_L1.getPosition().getValue() * mDriveGearRatio * (4.0 * Math.PI));
        entDrive_Distance_L2.set(mtrDrive_L2.getPosition().getValue() * mDriveGearRatio * (4.0 * Math.PI));
        entDrive_Distance_R1.set(mtrDrive_R1.getPosition().getValue() * mDriveGearRatio * (4.0 * Math.PI));
        entDrive_Distance_R2.set(mtrDrive_R2.getPosition().getValue() * mDriveGearRatio * (4.0 * Math.PI));
        
        entDrive_Distance_Average.set(getDriveDistance());
        entDrive_AtDistance.set(pidDrive_Distance.atSetpoint());

        entDrive_Angle.set(gyrDrive_Angle.getAngle());
        entPID_Angle.set(pidDrive_Angle.calculate(getDriveAngle()));
*/
        mtrDrive_L1.set(mLeftDrivePower);
        mtrDrive_L2.set(mLeftDrivePower);//actually sets the motor's power
        mtrDrive_R1.set(mRightDrivePower);
        mtrDrive_R2.set(mRightDrivePower);

    }
}
 