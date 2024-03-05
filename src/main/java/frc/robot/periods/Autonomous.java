package frc.robot.periods;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.molib.buttons.ButtonManager;
import frc.molib.utilities.Console;
import frc.robot.Robot;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Runway;
//TODO: end backed up pretty far(10 ft) and turned around ready to go to feeder station
//TODO: make a wait/delay before auton moves &or start
//TODO: make shoot and stay
/**
 * Code that moves the robot without HIDs based on dashboard inputs
 */
@SuppressWarnings("unused")
public class Autonomous {
    /**
     * Starting Position options for autonomous
     */
    private enum StartingPosition{
        DRIVER_STATION_WALL("Driver Station Wall"),
        SPEAKER_CENTER("Speaker Center"),
        SPEAKER_SOURCE_SIDE("Speaker Source"),
        SPEAKER_AMP_SIDE("Speaker Amp");

        private final String label;
        private StartingPosition(String label){
            this.label = label;
        }
        @Override public String toString(){
            return label;
        }
    }

    private enum StartDelay{
        ZERO("0s", 0.0),
        FIVE("5s", 5.0),
        TEN("10s", 10.0);

        private final String label;
        public final double time;
        private StartDelay(String label, double time){
            this.label = label;
            this.time = time;
        }
        @Override public String toString(){
            return label;
        }
    }
    /**
     * Sequences of robot action options for autonomous
     */
    private enum Sequence{
        NOTHING("Do Nothing"){
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
        SHOOTANDTRAVEL("Shoot and Travel"){
            @Override public void run(){
                switch(mSelectedStartingPosition){
                    case SPEAKER_CENTER:
                        switch(mStage){
                            case 0:
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 1:
                                if(tmrStageTimeOut.get() > mSelectedStartDelay.time){
                                    mStage++;
                                }    
                                break;
                            case 2:
                                Console.logMsg("Starting Sequence\"" + Sequence.SHOOTANDTRAVEL.toString() + "\"-" + mSelectedStartingPosition.toString());
                                mStage++;
                                break;
                            case 3:
                                Console.logMsg("Charging Reels");
                                Runway.speakerShot();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 4:
                                if (tmrStageTimeOut.get() > 0.25) mStage++;
                                break;
                            case 5:
                                Console.logMsg("Shooting");
                                Runway.enableDirector();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 6:
                                if (tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 7:
                                Console.logMsg("Cut");
                                Runway.disable();
                                mStage++;
                                break;
                            case 8:
                                Console.logMsg("Backing up");
                                Chassis.resetDistance();
                                Chassis.goToDistance(-48.0);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 9:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 2.5) mStage++;
                                break;
                            case 10:
                                Console.logMsg("and POSE");
                                Robot.disableSubsystems();
                                mStage++;
                                break;
                            default:
                                Robot.disableSubsystems();
                        } break;
                    case SPEAKER_SOURCE_SIDE:
                        switch(mStage){
                            case 0:
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 1:
                                if(tmrStageTimeOut.get() > mSelectedStartDelay.time){
                                    mStage++;
                                }    
                                break;
                            case 2:
                                Console.logMsg("Starting Sequence\"" + Sequence.SHOOTANDTRAVEL.toString() + "\"-" + mSelectedStartingPosition.toString());
                                mStage++;
                                break;
                            case 3:
                                Console.logMsg("Charging Reels");
                                Runway.speakerShot();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 4:
                                if (tmrStageTimeOut.get() > 0.25) mStage++;
                                break;
                            case 5:
                                Console.logMsg("Shooting");
                                Runway.enableDirector();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 6:
                                if (tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 7:
                                Console.logMsg("Cut");
                                Runway.disable();
                                mStage++;
                                break;
                            case 8:
                                Console.logMsg("Backing up");
                                Chassis.resetDistance();
                                Chassis.goToDistance(-96.0);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 9:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 2.5) mStage++;
                                break;
                            case 10:
                                Console.logMsg("done");
                                Robot.disableSubsystems();
                                mStage++;
                                break;
                            default:
                                Robot.disableSubsystems();
                        } break;
                    case SPEAKER_AMP_SIDE:
                        switch(mStage){ 
                            case 0:
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 1:
                                if(tmrStageTimeOut.get()>mSelectedStartDelay.time){
                                    mStage++;
                                }    
                                break;
                            case 2:
                                Console.logMsg("Starting Sequence\"" + Sequence.SHOOTANDTRAVEL.toString() + "\"-" + mSelectedStartingPosition.toString());
                                mStage++;
                                break;
                            case 3:
                                Console.logMsg("Charging Reels");
                                Runway.speakerShot();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 4:
                                if (tmrStageTimeOut.get() > .25) mStage++;
                                break;
                            case 5:
                                Console.logMsg("Shooting");
                                Runway.enableDirector();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 6:
                                if (tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 7:
                                Console.logMsg("Cut");
                                Runway.disable();
                                mStage++;
                                break;
                            case 8:
                                Console.logMsg("Backing up");
                                Chassis.resetDistance();
                                Chassis.goToDistance(-24.0);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 9:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 2.5) mStage++;
                                break;
                            case 10:
                                Console.logMsg("and Turning");
                                Chassis.resetAngle();
                                Chassis.disableDistancePID();
                               if(DriverStation.getAlliance().get() == Alliance.Red){ 
                                    Chassis.goToAngle(-30.00);
                                }else{
                                    Chassis.goToAngle(30.00);
                                }
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 11:
                                if(Chassis.isAtAngle() || tmrStageTimeOut.get() > 2.5) mStage++;
                                break;
                            case 12:
                                Console.logMsg("Good, now backing up");
                                mStage++;
                                break;
                            case 13:
                                Chassis.disableAnglePID();
                                Chassis.resetDistance();
                                Chassis.goToDistance(-36.0);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 14:
                                if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 2.5) mStage++;
                                break;
                            case 15:
                                Console.logMsg("done");
                                Robot.disableSubsystems();
                                mStage++;
                                break;
                            default:
                                Robot.disableSubsystems();
                        }break;
                    }
                }
            },
        TRAVEL("Travel"){
        @Override public void run(){
            switch(mStage){
                case 0:
                    tmrStageTimeOut.reset();
                    mStage++;
                    break;
                case 1:
                    if(tmrStageTimeOut.get()>mSelectedStartDelay.time){
                        mStage++;
                    }    
                    break;
                case 2:
                    Console.logMsg("Starting Sequence\"" + Sequence.TRAVEL.toString() + "\"-" + mSelectedStartingPosition.toString());
                    mStage++;
                    break;
                case 3:
                    Console.logMsg("Starting Drive Backwards");
                    Chassis.goToDistance(-96.0);
                    tmrStageTimeOut.reset();
                    mStage++;
                    break;
                case 4:
                    if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 2.5) mStage++;
                    break;
                case 5:
                    Console.logMsg("Conditions met :)");
                    Chassis.disable();
                    mStage++;
                    break;
                case 6:
                    Console.logMsg("Sequence Complete\"" + Sequence.TRAVEL.toString() + "\"-" + mSelectedStartingPosition.toString());
                    mStage++;
                    break;
                default:
                    Robot.disableSubsystems();
                    }
                }
            },
        SHOOTSTAY("Shoot and Stay"){
            @Override public void run(){
                switch ((mStage)) {
                    case 0:
                        tmrStageTimeOut.reset();
                    case 1:
                        if(tmrStageTimeOut.get() > mSelectedStartDelay.time){
                            mStage++;
                        }
                        break;
                    case 2:
                        Runway.speakerShot();
                        tmrStageTimeOut.reset();
                        mStage++;
                        break;
                    case 3:
                        if(tmrStageTimeOut.get() > 0.25){
                        mStage++;
                        }
                        break;
                    case 4:
                        Runway.enableDirector();
                        tmrStageTimeOut.reset();
                        mStage++;
                        break;
                    case 5:
                        if(tmrStageTimeOut.get() > 1.0){
                            mStage++;
                        }
                    case 6:
                        Robot.disableSubsystems();
                    break;
                    default:
                        Robot.disableSubsystems();
                        break;
                }
            }
        },
        SHOOTCROSS("Shoot and Cross Field"){//TODO:Make
            @Override public void run(){
                switch(mSelectedStartingPosition){
                    case SPEAKER_CENTER:
                        switch (mStage) {
                            case 0:
                                Runway.speakerShot();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            default:
                                Robot.disableSubsystems();   
                        }
                }
            }
        },
        JUSTCROSS("Just Cross Field"){//TODO: Make
            @Override public void run(){
                switch(mStage){
                    case 0:
                    tmrStageTimeOut.reset();
                    mStage++;
                    break;
                case 1:
                    if(tmrStageTimeOut.get() > mSelectedStartDelay.time){
                        mStage++;
                    }    
                    break;
                case 2:
                    Chassis.goToDistance(-96.0);
                    tmrStageTimeOut.reset();
                    mStage++;
                    break;
                case 3:
                    if(Chassis.isAtDistance() || tmrStageTimeOut.get() > 5.0){
                        mStage++;
                        break;
                    }
                case 4:
                    Robot.disableSubsystems();
                    mStage++;
                    break;
                default: Robot.disableSubsystems();
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
            tmrStageTimeOut.restart();
            mStage = 0;
        }

        public abstract void run();
    }

    //Sendable Choosers
    private static SendableChooser<Sequence> chsSequence = new SendableChooser<Sequence>();
    private static SendableChooser<StartingPosition> chsStartingPosition = new SendableChooser<StartingPosition>();
    private static SendableChooser<StartDelay> chsStartDelay = new SendableChooser<StartDelay>();

    //Buffer Variables
    private static Sequence mSelectedSequence;
    private static StartingPosition mSelectedStartingPosition;
    private static Alliance mAlliance;
    private static StartDelay mSelectedStartDelay;

    /**
     * Prevents seperate instances of this class being made
     */
    private Autonomous(){}

    /**
     * Code that runs once at the beginning of the Autonomous period
     */
    public static void init(){

        Robot.disableSubsystems();

        Chassis.enableBrake();

        ButtonManager.clearFlags();

        mSelectedSequence = chsSequence.getSelected();
        mSelectedStartingPosition = chsStartingPosition.getSelected();
        mSelectedStartDelay = chsStartDelay.getSelected();

        mSelectedSequence.init();
    }

    /**
     * Sets up the dashboard options
     */
    public static void initDashboard(){
        chsSequence.addOption(Sequence.NOTHING.label, Sequence.NOTHING);
        chsSequence.addOption(Sequence.SHOOTANDTRAVEL.label, Sequence.SHOOTANDTRAVEL);
        chsSequence.addOption(Sequence.JUSTCROSS.label, Sequence.JUSTCROSS);
        chsSequence.addOption(Sequence.SHOOTCROSS.label, Sequence.SHOOTCROSS);
        chsSequence.addOption(Sequence.SHOOTSTAY.label, Sequence.SHOOTSTAY);
        chsSequence.addOption(Sequence.TRAVEL.label, Sequence.TRAVEL);
        chsSequence.setDefaultOption(Sequence.NOTHING.label, Sequence.NOTHING);
        SmartDashboard.putData("Sequence", chsSequence);

        chsStartDelay.addOption(StartDelay.ZERO.label, StartDelay.ZERO);
        chsStartDelay.addOption(StartDelay.FIVE.label, StartDelay.FIVE);
        chsStartDelay.addOption(StartDelay.TEN.label, StartDelay.TEN);
        chsStartDelay.setDefaultOption(StartDelay.ZERO.label, StartDelay.ZERO);
        SmartDashboard.putData("Start Delay", chsStartDelay);

        chsStartingPosition.addOption(StartingPosition.SPEAKER_CENTER.label, StartingPosition.SPEAKER_CENTER);
        chsStartingPosition.addOption(StartingPosition.SPEAKER_SOURCE_SIDE.label, StartingPosition.SPEAKER_SOURCE_SIDE);
        chsStartingPosition.addOption(StartingPosition.SPEAKER_AMP_SIDE.label, StartingPosition.SPEAKER_AMP_SIDE);
        chsStartingPosition.setDefaultOption(StartingPosition.SPEAKER_CENTER.label, StartingPosition.SPEAKER_CENTER);
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
