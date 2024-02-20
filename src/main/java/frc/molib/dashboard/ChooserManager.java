package frc.molib.dashboard;

import java.util.Vector;

import frc.molib.utilities.Console;

@SuppressWarnings({"deprecation", "rawtypes"})
public class ChooserManager {
    private static Vector<Chooser> m_Choosers = new Vector<Chooser>();
    private static Thread mUpdateThread = new Thread() {
        //TODO: Determine if ChooserManager should run in it's own thread
        @Override public void run() {
            Console.logMsg("Chooser Manager: Started");
            while(true) {
                ChooserManager.updateAll();
            }
        }
    };

    public static void start() {
        if(!mUpdateThread.isAlive()) mUpdateThread.start();
    }

    public static void addChooser(Chooser chooser) { m_Choosers.add(chooser); }

    public static void removeChooser(Chooser chooser) { m_Choosers.remove(chooser); }

    public static void removeAll() { m_Choosers.clear(); }

    public static void updateAll() {
        for(Chooser chsTemp : m_Choosers) chsTemp.update();
    }
}
