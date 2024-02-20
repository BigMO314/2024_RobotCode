package frc.molib.hid;

import frc.molib.utilities.MOUtility;

public class GenericHID extends edu.wpi.first.wpilibj.GenericHID {
    private double mDeadzoneThreshold = 0.1;
	
	/**
	 * Construct an instance of an Xbox Controller.
	 * @param port The port on the Driver Station that the controller is assigned to.
	 */
	public GenericHID(int port) { super(port); }
	
	public void configDeadzoneThreshold(double value) { mDeadzoneThreshold = value; }

	/**
	 * Enable the rumble function of the controller.
	 * @param value Power of the vibration
	 */
	public void setRumble(double value) { setRumble(value, value); }
	
	/**
	 * Enable the rumble function to each side of the controller.
	 * @param leftValue 	Power of the left vibration
	 * @param rightValue	Power of the right vibration
	 */
	public void setRumble(double leftValue, double rightValue) {
		super.setRumble(RumbleType.kLeftRumble, leftValue);
		super.setRumble(RumbleType.kRightRumble, rightValue);
	}

	/**
   * Get the value of the axis with the deadzone applied.
   *
   * @param axis The axis to read, starting at 0.
   * @return The value of the axis.
   */
	@Override
	public double getRawAxis(int axis) { 
		return MOUtility.deadenAxis(super.getRawAxis(axis), mDeadzoneThreshold); 
	}
}
