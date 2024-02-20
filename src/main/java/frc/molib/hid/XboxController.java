package frc.molib.hid;

import frc.molib.utilities.MOUtility;

/**
 * A simple wrapper class on {@link edu.wpi.first.wpilibj.XboxController} that provides additional functionality such as:
 * built-in deadzones on axes, option to invert the Y-axis of the joysticks, simple control over the rumble feature, 
 * and the abililty to read the triggers as individual buttons.
 */
public class XboxController extends edu.wpi.first.wpilibj.XboxController {
	private double mDeadzoneThreshold = 0.1;
	private double mTriggerThreshold = 0.5;
	private boolean mIsYAxisInverted = false;
	
	/**
	 * Construct an instance of an Xbox Controller.
	 * @param port The port on the Driver Station that the controller is assigned to.
	 */
	public XboxController(int port) { super(port); }
	
	public void configDeadzoneThreshold(double value) { mDeadzoneThreshold = value; }
	public void configTriggerThreshold(double value) { mTriggerThreshold = value; }
	public void configYAxisInverted(boolean isInverted) { mIsYAxisInverted = isInverted; }

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
	 * @param axis The axis to read, using the Axis enumeration
	 * @return The value of the axis
	 */
	public double getRawAxis(Axis axis) { 
		return getRawAxis(axis.value); 
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

	@Override
	public double getLeftY() {
		if(mIsYAxisInverted) return super.getLeftY();
		else return -super.getLeftY();
	}

	@Override
	public double getRightY() {
		if(mIsYAxisInverted) return super.getRightY();
		else return -super.getRightY();
	}
	
	/**
	 * Get the Left Trigger as if it were a button.
	 * @return Whether the Left Trigger is pressed past the configured threshold
	 */
	public boolean getLeftTrigger() { 
		return super.getLeftTriggerAxis() > mTriggerThreshold; 
	}
	
	/**
	 * Get the Right Trigger as if it were a button.
	 * @return Whether the Right Trigger is pressed past the configured threshold
	 */
	public boolean getRightTrigger() { 
		return super.getRightTriggerAxis() > mTriggerThreshold; 
	}

	/**
	 * Treat the two triggers as one combined axis, 
	 * left being negative and right being positive.
	 * @return Combined axis value
	 */
	public double getTriggerAxis() { 
		return getRightTriggerAxis() - getLeftTriggerAxis(); 
	}
}