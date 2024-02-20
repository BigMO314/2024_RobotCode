package frc.molib.dashboard;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilderImpl;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * <p>Creates an option chooser in dashboard</p>
 * <i>Need to implement {@link SendableRegistry#update}</i>
 * 
 * @param <OptionType> Enumeration tied to the list of options.
 * 
 * @deprecated As of 2023 Season; currently does not update in dashboard, fix in progress. 
 * <p>Please use {@link edu.wpi.first.wpilibj.smartdashboard.SendableChooser}</p>
 */
public class Chooser<OptionType extends Enum<OptionType>> {
	private final NetworkTable mParentTable;
	private final String mKey;

	private final SendableChooser<OptionType> chsSendable = new SendableChooser<OptionType>();

	/**
	 * Constructor
	 * @param parentTable 	Parent NetworkTable
	 * @param key			Identifier key
	 * @param defaultOption	Default selected option
	 */
	public Chooser(NetworkTable parentTable, String key, OptionType defaultOption) {
		mParentTable = parentTable;
		mKey = key;

		for(OptionType enumValue : defaultOption.getDeclaringClass().getEnumConstants())
			chsSendable.addOption(enumValue.toString(), enumValue);
		chsSendable.setDefaultOption(defaultOption.toString(), defaultOption);

		ChooserManager.addChooser(this);
	}

	/**
	 * Must be run at the start, <i>but after NetworkTables has connected,</i> for it to appear in NetworkTables
	 */
	public void init() {
		NetworkTable tblData = mParentTable.getSubTable(mKey);
		SendableBuilderImpl builder = new SendableBuilderImpl();
		builder.setTable(tblData);
		SendableRegistry.publish(chsSendable, builder);
		builder.startListeners();
		tblData.getEntry(".name").setString(mKey);
	}

	/**
	 * Retrieves the currently selected option in dashboard.
	 * @return Selected object. If one is not selected, returns the default option
	 */
	public OptionType get() { return chsSendable.getSelected(); }

	public void update() {
		SendableRegistry.update(chsSendable);
	}
}
