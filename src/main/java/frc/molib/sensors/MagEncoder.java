package frc.molib.sensors;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;

import com.ctre.phoenix.motorcontrol.can.BaseTalon;

/**
 * Represents the Magnetic Encoder attached to one of the CTRE Talon Motor Controllers
 * @see com.ctre.phoenix.motorcontrol.can.BaseTalon
 * @see com.ctre.phoenix.motorcontrol.can.TalonSRX
 * @see com.ctre.phoenix.motorcontrol.can.TalonFX
 */
public class MagEncoder implements Sendable{
	private final BaseTalon mtrSource;
	private double mDistancePerPulse;
	
	/**
	 * Constructor
	 * @param mtrSource The CTRE Talon Motor Montroller the Mag Encoder is attached to.
	 */
	public MagEncoder(BaseTalon mtrSource) { 
		this.mtrSource = mtrSource; 
		SendableRegistry.addLW(this, "Mag Encoder", mtrSource.getDeviceID());
	}
	
	public double getDistancePerPulse() { return mDistancePerPulse; }
	public void configDistancePerPulse(double distancePerPulse) { mDistancePerPulse = distancePerPulse; }

	public double getDistance() { return mtrSource.getSelectedSensorPosition(0) * mDistancePerPulse; }
	public double getRate() { return mtrSource.getSelectedSensorVelocity(0) * 10.0 * mDistancePerPulse; }
	public void reset() { mtrSource.setSelectedSensorPosition(0, 0, 0); }

	@Override
	public void initSendable(SendableBuilder builder) {
		builder.setSmartDashboardType("MOLib Mag Encoder");

		builder.addDoubleProperty("Speed", this::getRate, null);
		builder.addDoubleProperty("Distance", this::getDistance, null);
	}
}