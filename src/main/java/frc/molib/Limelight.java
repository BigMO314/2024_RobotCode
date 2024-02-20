package frc.molib;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.molib.dashboard.Entry;

/**
 * Interface for the Limelight table on NetworkTables
 */
public final class Limelight {
	public enum LEDMode {
		kDefault(0),
		kOff(1),
		kBlink(2),
		kOn(3);

		public final int value;
		private LEDMode(int value) { this.value = value; }
	}

	public enum CamMode {
		kVisionProcessor(0),
		kDriverCam(1);
		
		public final int value;
		private CamMode(int value) { this.value = value; }
	}

	public enum StreamMode {
		kStandard(0),
		kPrimaryPiP(1),
		kSecondaryPiP(2);

		public final int value;
		private StreamMode(int value){ this.value = value; }
	}
	
	private static final NetworkTable tblLimelight = NetworkTableInstance.getDefault().getTable("limelight");

	private static final Entry<Double>	entHasTarget = new Entry<Double>(tblLimelight, "tv");
	private static final Entry<Double> 	entPosX = new Entry<Double>(tblLimelight, "tx");
	private static final Entry<Double> 	entPosY = new Entry<Double>(tblLimelight, "ty");
	private static final Entry<Double> 	entWidth = new Entry<Double>(tblLimelight, "thor");
	private static final Entry<Double> 	entHeight = new Entry<Double>(tblLimelight, "tver");
	private static final Entry<Double> 	entArea = new Entry<Double>(tblLimelight, "ta");
	
	private static final Entry<Integer>	entLEDMode = new Entry<Integer>(tblLimelight, "ledMode");
	private static final Entry<Integer>	entCamMode = new Entry<Integer>(tblLimelight, "camMode");
	private static final Entry<Integer>	entPipeline = new Entry<Integer>(tblLimelight, "pipeline");
	private static final Entry<Integer>	entStreamMode = new Entry<Integer>(tblLimelight, "stream");

	private Limelight() {}

	public static boolean hasTarget() { return entHasTarget.get() == 1; }
	public static double getPosX() { return entPosX.get(); }
	public static double getPosY() { return entPosY.get(); }
	public static double getWidth() { return entWidth.get(); }
	public static double getHeight() { return entHeight.get(); }
	public static double getArea() { return entArea.get(); }
	
	public static void setLEDMode(LEDMode mode) { entLEDMode.set(mode.value); }
	public static void setCamMode(CamMode mode) { entCamMode.set(mode.value); }
	public static void setPipeline(int pipeline) { entPipeline.set(pipeline); }
	public static void setStream(StreamMode mode) { entStreamMode.set(mode.value); }

	public static LEDMode getLEDMode() { return LEDMode.values()[entLEDMode.get()]; }
	public static CamMode getCamMode() { return CamMode.values()[entCamMode.get()]; }
	public static int getPipeline() { return entPipeline.get(); }
	public static StreamMode getStreamMode() { return StreamMode.values()[entStreamMode.get()]; }	
}