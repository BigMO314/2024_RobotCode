package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.networktables.NetworkTable;
import frc.molib.PIDController;
import frc.molib.dashboard.Entry;
import frc.molib.sensors.DigitalInput;
import frc.robot.Robot;

public class Backstage {
    public static enum Position {
        RAISED(0.0),
        FLOOR(110.0);

        public final double angle;

        private Position(double angle) { this.angle = angle; }
    }

    private static NetworkTable tblBackstage = Robot.tblSubsystem.getSubTable("Backstage");
    private static Entry<Boolean> entBackstageLoaded = new Entry<Boolean>(tblBackstage, "Backstage Loaded");

    private static VictorSPX mtrIntake = new VictorSPX(8);
    private static VictorSPX mtrAssistantDirector = new VictorSPX(9);
    private static TalonFX mtrArm = new TalonFX(10);

    private static DigitalInput phoLoaded = new DigitalInput(3, true);

    private static final PIDController pidArm_Angle = new PIDController(0, 0, 0);

    private static double mIntakePower = 0.0;
    private static double mAssistantDirectorPower = 0.0;
    private static double mArmPower = 0.0;
    
    private Backstage(){}

    
    public static void init(){
        //inversions
        mtrIntake.setInverted(false);
        mtrAssistantDirector.setInverted(false);
        mtrArm.setInverted(false);

        //brake modes
        mtrIntake.setNeutralMode(NeutralMode.Coast);
        mtrAssistantDirector.setNeutralMode(NeutralMode.Coast);
        mtrArm.setNeutralMode(NeutralModeValue.Coast);

        //Sensor Resets
        resetPosition();

        // PID config
        pidArm_Angle.configOutputRange(-0.25, 0.25); //FIXME: Increase PID output when tuned
        pidArm_Angle.configAtSetpointTime(0.1);
        pidArm_Angle.setTolerance(0.0);
    }

    public static void initDashboard(){
        //TODO: init dashboard stuff
    }

    public static void updateDashboard(){
        entBackstageLoaded.set(isLoaded());
    }

    public static void disable(){
        disableIntake();
        disableAssistantDirector();
        disableArm();

        disablePIDs();
    }

    public static void disablePIDs(){
        pidArm_Angle.disable();
    }

////Sensors

    public static double getPosition() {
        return mtrArm.getPosition().getValue() * 360.0 * 1.0; //FIXME: Get proper math for arm gearbox
    }

    public static void resetPosition() {
        mtrArm.setPosition(0.0);
    }

    public static boolean isLoaded() {
        return phoLoaded.get();
    }

////Intake

    public static void setIntakePower(double power) {
        mIntakePower = power;
    }

    public static void enableIntake() {
        setIntakePower(1.0);
    }

    public static void reverseIntake() {
        setIntakePower(-1.0);
    }

    public static void disableIntake() {
        setIntakePower(0.0);
    }

////Assistant Director

    public static void setAssistantDirectorPower(double power) {
        mAssistantDirectorPower = power;
    }

    public static void enableAssistantDirector() {
        setAssistantDirectorPower(1.0);
    }

    public static void reverseAssistantDirector() {
        setAssistantDirectorPower(-1.0);
    }

    public static void disableAssistantDirector() {
        setAssistantDirectorPower(0.0);
    }

////Stage Hand

    public static void setStangehandPower(double power) {
        mArmPower = power;
    }

    public static void disableArm() {
        setAssistantDirectorPower(0.0);
    }

////Stage Hand PID

    public static void goToAngle(double angle) {
        pidArm_Angle.setSetpoint(angle);
        pidArm_Angle.enable();
    }

    public static void goToPosition(Position position) {
        goToAngle(position.angle);
    }

    public static boolean isAtAngle() {
        return pidArm_Angle.atSetpoint();
    }

    public static void disableArmPID() {
        pidArm_Angle.disable();
    }

//////////////////////////////////////////////////

    public static void periodic(){
        if(pidArm_Angle.isEnabled()) setStangehandPower(pidArm_Angle.calculate(getPosition()));

        mtrIntake.set(ControlMode.PercentOutput, mIntakePower);
        mtrAssistantDirector.set(ControlMode.PercentOutput, mAssistantDirectorPower);
        mtrArm.set(mArmPower);
    }


}
