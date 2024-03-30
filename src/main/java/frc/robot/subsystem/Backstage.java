package frc.robot.subsystem;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.NetworkTable;
import frc.molib.PIDController;
import frc.molib.dashboard.Entry;
import frc.molib.sensors.DigitalInput;
import frc.robot.Robot;

public class Backstage {
    public static enum Position {
        RAISED(10.0),
        FLOOR(112.0);

        public final double angle;

        private Position(double angle) { this.angle = angle; }
    }

    private static NetworkTable tblBackstage = Robot.tblSubsystem.getSubTable("Backstage");
    private static Entry<Boolean> entBackstageLoaded = new Entry<Boolean>(tblBackstage, "Backstage Loaded");
    private static Entry<Boolean> entRaised = new Entry<Boolean>(tblBackstage, "Backstage Raised");
    private static Entry<Double> entPivotAngle = new Entry<Double>(tblBackstage, "Backstage Angle");

    private static VictorSPX mtrIntake = new VictorSPX(9);
    private static VictorSPX mtrAssistantDirector = new VictorSPX(10);
    private static TalonFX mtrPivot = new TalonFX(11);

    private static DigitalInput phoLoaded = new DigitalInput(3, false);
    private static DigitalInput limRaised_R = new DigitalInput(4, true);
    private static DigitalInput limRaised_L = new DigitalInput(5, true);

    private static final PIDController pidPivot_Angle = new PIDController(0.03, 0, 0);

    private static double mIntakePower = 0.0;
    private static double mAssistantDirectorPower = 0.0;
    private static double mPivotPower = 0.0;
    
    private Backstage(){}

    
    public static void init(){
        //inversions
        mtrIntake.setInverted(false);
        mtrAssistantDirector.setInverted(true);
        mtrPivot.setInverted(true);

        //brake modes
        mtrIntake.setNeutralMode(NeutralMode.Coast);
        mtrAssistantDirector.setNeutralMode(NeutralMode.Coast);
        mtrPivot.setNeutralMode(NeutralModeValue.Brake);

        //Sensor Resets
        //resetPosition();

        // PID config
        pidPivot_Angle.configOutputRange(-0.50, 0.50); //FIXME: Increase PID output when tuned
        pidPivot_Angle.configAtSetpointTime(0.1);
        pidPivot_Angle.setTolerance(0.0);

        disablePIDs();
    }

    public static void initDashboard(){
        //TODO: init dashboard stuff
    }

    public static void updateDashboard(){
        entBackstageLoaded.set(isLoaded());
        entRaised.set(isRaised());
        entPivotAngle.set(getPosition());
    }

    public static void disable(){
        disableIntake();
        disableAssistantDirector();
        disablePivot();

        disablePIDs();
    }

    public static void disablePIDs(){
        pidPivot_Angle.disable();
    }

////Sensors

    public static double getPosition() {
        return (mtrPivot.getPosition().getValue() * 360.0) / 106.67; //FIXME: CONFIRM
    }

    public static void resetPosition() {
        mtrPivot.setPosition(0.0);
    }

    public static boolean isLoaded() {
        return phoLoaded.get();
    }

    public static boolean isRaised() { 
        return limRaised_R.get() || limRaised_L.get();
    }

////Intake

    public static void setIntakePower(double power) {
        mIntakePower = power;
    }

    public static void enableIntake() {
        setIntakePower(0.90);
    }

    public static void reverseIntake() {
        setIntakePower(-0.75);
    }

    public static void jamfixIntake(){
        setIntakePower(-0.50);
    }

    public static void disableIntake() {
        setIntakePower(0.0);
    }

////Assistant Director

    public static void setAssistantDirectorPower(double power) {
        mAssistantDirectorPower = power;
    }

    public static void enableAssistantDirector() {
        setAssistantDirectorPower(0.95);
    }

    public static void reverseAssistantDirector() {
        setAssistantDirectorPower(-.75);
    }

    public static void disableAssistantDirector() {
        setAssistantDirectorPower(0.0);
    }

////Pivot

    public static void setPivotPower(double power) {
        mPivotPower = power;
    }

    public static void disablePivot() {
        setPivotPower(0.0);
    }

////Pivot PID

    public static void goToAngle(double angle) {
        pidPivot_Angle.setSetpoint(angle);
        pidPivot_Angle.enable();
    }

    public static void goToPosition(Position position) {
        goToAngle(position.angle);
    }

    public static boolean isAtAngle() {
        return pidPivot_Angle.atSetpoint();
    }

    public static void disablePivotPID() {
        pidPivot_Angle.disable();
    }

//////////////////////////////////////////////////

    public static void periodic(){
        if(pidPivot_Angle.isEnabled()) setPivotPower(pidPivot_Angle.calculate(getPosition()));

        if(isRaised()) {
            resetPosition();
            mPivotPower = MathUtil.clamp(mPivotPower, 0.0, 1.0);
        }

        mtrIntake.set(ControlMode.PercentOutput, mIntakePower);
        mtrAssistantDirector.set(ControlMode.PercentOutput, mAssistantDirectorPower);
        mtrPivot.set(mPivotPower);
    }


}
