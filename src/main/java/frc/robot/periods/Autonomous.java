package frc.robot.periods;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.molib.buttons.ButtonManager;
import frc.molib.utilities.Console;
import frc.robot.Robot;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Runway;

/**
 * Code that moves the robot without HIDs based on dashboard inputs
 */
@SuppressWarnings("unused")
public class Autonomous {
    /**
     * Starting Position options for autonomous
     */
    private enum StartingPosition{
        CSPEAKER("Speaker Center"),
        LSPEAKER("Speaker Left"),
        RSPEAKER("Speaker Right");

        private final String label;
        private StartingPosition(String label){
            this.label = label;
        }
        @Override public String toString(){
            return label;
        }
    }
    /**
     * Sequences of robot action options for autonomous
     */
    private enum Sequence{
        NOTHING("don't."){
            @Override public void run(){
                switch(mStage){
                    case 0:
                        Robot.disableSubsystems();
                        mStage++;
                        break;
                    default:
                        Robot.disableSubsystems();
                    }
                }
            },
        SCOREANDTRAVEL("Score and back up"){
            @Override public void run(){
                switch(mSelectedStartingPosition){
                    case CSPEAKER:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Starting Sequence\"" + Sequence.SCOREANDTRAVEL.toString() + "\"-" + mSelectedStartingPosition.toString());
                            case 1:
                                Console.logMsg("Charging Reels");
                                Runway.enableReels();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 2:
                                if (tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 3:
                                Console.logMsg("Shooting");
                                Runway.enableDirector();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 4:
                                if (tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 5:
                                Console.logMsg("Cut");
                                Runway.disable();
                                mStage++;
                                break;
                            case 6:
                                Console.logMsg("Backing up");
                                Chassis.goToDistance(96.0);//TODO: Input correct distance value
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 7:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 2.5) mStage++;
                                break;
                            case 8:
                                Console.logMsg("and POSE");
                                Robot.disableSubsystems();
                                mStage++;
                                break;
                            default:
                                Robot.disableSubsystems();
                        } break;
                    case LSPEAKER:
                        switch(mStage){

                        } break;
                    case RSPEAKER:
                switch(mStage){

                        }break;
                    }
                }
            },
        TRAVEL("Back up"){
        @Override public void run(){
            switch(mStage){
                case 0:
                    Console.logMsg("Starting Sequence\"" + Sequence.TRAVEL.toString() + "\"-" + mSelectedStartingPosition.toString());
                    mStage++;
                    break;
                case 1:
                    Console.logMsg("Starting Drive Backwards");
                    Chassis.goToDistance(96.0);//FIXME: find out proper value to drive backwards to achieve travel point and implement
                    tmrStageTimeOut.reset();
                    mStage++;
                    break;
                case 2:
                    if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 2.5) mStage++;
                    break;
                case 3:
                    Console.logMsg("Conditions met :)");
                    Chassis.disable();
                    mStage++;
                    break;
                case 4:
                    Console.logMsg("Sequence Complete\"" + Sequence.TRAVEL.toString() + "\"-" + mSelectedStartingPosition.toString());
                    mStage++;
                    break;
                default:
                    Robot.disableSubsystems();
                }
            }
        };
 
        private static final Timer tmrStageTimeOut = new Timer();
        private final String label;
        private static int mStage = 0;

        private Sequence(String label){
            this.label = label;
        }

        @Override public String toString(){
            return label;
        }

        public void init() {
            tmrStageTimeOut.start();
            tmrStageTimeOut.reset();
            mStage = 0;
        }

        public abstract void run();
    }

    private static SendableChooser<Sequence> chsSequence = new SendableChooser<Sequence>();
    private static Sequence mSelectedSequence;
    private static SendableChooser<StartingPosition> chsStartingPosition = new SendableChooser<StartingPosition>();
    private static StartingPosition mSelectedStartingPosition;
    private static Alliance mAlliance;

    /**
     * Prevents seperate instances of this class being made
     */
    private Autonomous(){}

    /**
     * Code that runs once at the beginning of the Autonomous period
     */
    public static void init(){
        ButtonManager.clearFlags();

        mSelectedSequence = chsSequence.getSelected();
        mSelectedStartingPosition = chsStartingPosition.getSelected();

        mSelectedSequence.init();
    }

    /**
     * Sets up the dashboard options
     */
    public static void initDashboard(){
        chsSequence.addOption(Sequence.NOTHING.label, Sequence.NOTHING);
        chsSequence.addOption(Sequence.SCOREANDTRAVEL.label, Sequence.SCOREANDTRAVEL);
        chsSequence.setDefaultOption(Sequence.NOTHING.label, Sequence.NOTHING);
        SmartDashboard.putData("Sequence", chsSequence);

        chsStartingPosition.addOption(StartingPosition.CSPEAKER.label, StartingPosition.CSPEAKER);
        chsStartingPosition.addOption(StartingPosition.LSPEAKER.label, StartingPosition.LSPEAKER);
        chsStartingPosition.addOption(StartingPosition.RSPEAKER.label, StartingPosition.RSPEAKER);
        chsStartingPosition.setDefaultOption(StartingPosition.CSPEAKER.label, StartingPosition.CSPEAKER);
        SmartDashboard.putData("Starting Position", chsStartingPosition);
    }
    /**
     * Updates subsystems and pulls values
     */
    public static void periodic(){
        mSelectedSequence.run();

        Chassis.periodic();
        Runway.periodic();
    }
}
