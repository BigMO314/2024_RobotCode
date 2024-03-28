package frc.robot.periods;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.molib.buttons.ButtonManager;
import frc.molib.utilities.Console;
import frc.robot.Robot;
import frc.robot.subsystem.Backstage;
import frc.robot.subsystem.Chassis;
import frc.robot.subsystem.Hanger;
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
        DRIVER_STATION_WALL("Driver Station Wall"),
        SPEAKER_CENTER("Speaker Center"),
        SPEAKER_SOURCE_SIDE("Speaker Source Side"),
        SPEAKER_AMP_SIDE("Speaker Amp Side");

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
        TWO("2s", 2.0),
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
                        Console.logMsg("Doing...nothing. Well, rezeroing, but nothing other than that.");
                        mStage++;
                    case 1:
                        Backstage.setPivotPower(-0.25);
                        mStage++;
                        break;
                    case 2:
                        if(Backstage.isRaised()){
                            Backstage.goToPosition(Backstage.Position.RAISED);
                            if(tmrStageTimeOut.get() > mSelectedStartDelay.time) mStage++;
                        }
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
                        Backstage.setPivotPower(-0.25);
                        mStage++;
                        break;
                    case 1:
                        if(Backstage.isRaised()){
                            Backstage.goToPosition(Backstage.Position.RAISED);
                            if(tmrStageTimeOut.get() > mSelectedStartDelay.time) mStage++;
                        }
                        break;
                    case 2:
                        Console.logMsg("Spinning Reels");
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
                        Console.logMsg("Shooting");
                        Runway.enableDirector();
                        tmrStageTimeOut.reset();
                        mStage++;
                        break;
                    case 5:
                        if(tmrStageTimeOut.get() > 1.0){
                            mStage++;
                        }
                        break;
                    case 6:
                        Console.logMsg("Done");
                        Robot.disableSubsystems();
                        mStage++;
                        break;
                    default:
                        Robot.disableSubsystems();
                        break;
                }
            }
        },
        JUSTTRAVEL("Just Travel"){
        @Override public void run(){
            switch(mStage){
                case 0:
                    Backstage.setPivotPower(-0.25);
                    mStage++;
                    break;
                case 1:
                    if(Backstage.isRaised()){
                        Backstage.goToPosition(Backstage.Position.RAISED);
                        if(tmrStageTimeOut.get() > mSelectedStartDelay.time) mStage++;
                    }
                    break;
                case 2:
                    Console.logMsg("Starting Sequence\"" + Sequence.JUSTTRAVEL.toString() + "\"-" + mSelectedStartingPosition.toString());
                    mStage++;
                    break;
                case 3:
                    Console.logMsg("Starting Drive Forwards");
                    Chassis.resetDistance();
                    Chassis.setDrivePower(0.30, 0.30);
                    tmrStageTimeOut.reset();
                    mStage++;
                    break;
                case 4:
                    if((Chassis.getDriveDistance() >= 96.0) || tmrStageTimeOut.get() > 2.5) mStage++;
                    break;
                case 5:
                    Console.logMsg("Conditions met :)");
                    Chassis.disable();
                    mStage++;
                    break;
                case 6:
                    Console.logMsg("Sequence Complete\"" + Sequence.JUSTTRAVEL.toString() + "\"-" + mSelectedStartingPosition.toString());
                    mStage++;
                    break;
                default:
                    Robot.disableSubsystems();
                    }
                }
        },
        SHOOTTRAVEL("Shoot and Travel"){
            @Override public void run(){
                switch(mSelectedStartingPosition){
                    case SPEAKER_CENTER:
                        switch(mStage){
                            case 0:
                                Backstage.setPivotPower(-0.25);
                                mStage++;
                                break;
                            case 1:
                                if(Backstage.isRaised()){
                                    Backstage.goToPosition(Backstage.Position.RAISED);
                                    if(tmrStageTimeOut.get() > mSelectedStartDelay.time) mStage++;
                                }
                                break;
                            case 2:
                                Console.logMsg("Starting Sequence\"" + Sequence.SHOOTTRAVEL.toString() + "\"-" + mSelectedStartingPosition.toString());
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
                                Chassis.setDrivePower(-0.30, -0.30);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 9:
                                if((Chassis.getDriveDistance() <= -48.0)|| tmrStageTimeOut.get() > 2.5) mStage++;
                                break;
                            case 10:
                                Console.logMsg("and Turning");
                                Chassis.resetAngle();
                                Chassis.disableDistancePID();
                                Chassis.goToAngle(180);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 11:
                                if(Chassis.isAtAngle() || tmrStageTimeOut.get() > 2.5) mStage++;
                                break;
                            case 12:
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
                                Backstage.setPivotPower(-0.25);
                                mStage++;
                                break;
                            case 1:
                                if(Backstage.isRaised()){
                                    Backstage.goToPosition(Backstage.Position.RAISED);
                                    if(tmrStageTimeOut.get() > mSelectedStartDelay.time) mStage++;
                                }
                                break;
                            case 2:
                                Console.logMsg("Starting Sequence\"" + Sequence.SHOOTTRAVEL.toString() + "\"-" + mSelectedStartingPosition.toString());
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
                                if(DriverStation.getAlliance().get() == Alliance.Red){ 
                                    Chassis.setDrivePower(-.25, -.30);
                                }else{
                                    Chassis.setDrivePower(-.30, -.25);
                                }
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 9:
                                if((Chassis.getDriveDistance() <= -144.00) || tmrStageTimeOut.get() > 2.5) mStage++;
                                break;
                            case 10:
                                Console.logMsg("and Turning");
                                Chassis.resetAngle();
                                Chassis.disableDistancePID();
                                if(DriverStation.getAlliance().get() == Alliance.Red){ 
                                    Chassis.goToAngle(-150.00);
                                }else{
                                    Chassis.goToAngle(150.00);
                                }
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 11:
                                if(Chassis.isAtAngle() || tmrStageTimeOut.get() > 2.5) mStage++;
                                break;
                            case 12:
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
                                Backstage.setPivotPower(-0.25);
                                mStage++;
                                break;
                            case 1:
                                if(Backstage.isRaised()){
                                    Backstage.goToPosition(Backstage.Position.RAISED);
                                    if(tmrStageTimeOut.get() > mSelectedStartDelay.time) mStage++;
                                }
                                break;
                            case 2:
                                Console.logMsg("Starting Sequence\"" + Sequence.SHOOTTRAVEL.toString() + "\"-" + mSelectedStartingPosition.toString());
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
                                Chassis.setDrivePower(-0.30, -0.30);
                                Chassis.goToDistance(-24.0);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 9:
                                if((Chassis.getDriveDistance() <= -24.0) || tmrStageTimeOut.get() > 2.5) mStage++;
                                break;
                            case 10:
                                Console.logMsg("and Turning");
                                Chassis.resetAngle();
                                Chassis.disableDistancePID();
                                if(DriverStation.getAlliance().get() == Alliance.Red){ 
                                    Chassis.goToAngle(150.00);
                                }else{
                                    Chassis.goToAngle(-150.00);
                                }
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 11:
                                if(Chassis.isAtAngle() || tmrStageTimeOut.get() > 2.5) mStage++;
                                break;
                            case 12:
                                Console.logMsg("Good, now going forward");
                                mStage++;
                                break;
                            case 13:
                                Chassis.disableAnglePID();
                                Chassis.resetDistance();
                                Chassis.setDrivePower(0.30, 0.30);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 14:
                                if((Chassis.getDriveDistance() >= 60.0) || tmrStageTimeOut.get() > 2.5) mStage++;
                                break;
                            case 15:
                                Console.logMsg("done");
                                Robot.disableSubsystems();
                                mStage++;
                                break;
                            default:
                                Robot.disableSubsystems();
                        }break;
                    default:
                        switch(mStage){
                            case 0:
                                Console.logMsg("Invalid starting position.");
                                mStage++;
                            default:
                                Robot.disableSubsystems();
                        }
                    }
                }
            },
        SHOOT_AND_SHOOT_AGAIN("Shoot and Shoot Again") {
            private double turnAngle = 30.0;
            @Override public void run() {
                switch(mSelectedStartingPosition) {
                    case SPEAKER_AMP_SIDE:
                    case SPEAKER_SOURCE_SIDE:
                        switch(mStage) {
                            case 0:
                                Console.logMsg("Starting Sequence\"" + Sequence.SHOOT_AND_SHOOT_AGAIN.toString() + "\"-" + mSelectedStartingPosition.toString());
                                Backstage.setPivotPower(-0.25);
                                mStage++;
                                break;
                            case 1:
                                if(Backstage.isRaised()){
                                    Backstage.goToPosition(Backstage.Position.RAISED);
                                    if(tmrStageTimeOut.get() > mSelectedStartDelay.time) mStage++;
                                }
                                break;
                            case 2:
                                Console.logMsg("Spinning Reels...");
                                Runway.speakerShot();
                                Backstage.goToPosition(Backstage.Position.FLOOR);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 3:
                                if(tmrStageTimeOut.get() > 0.25) mStage++;
                                break;
                            case 4:
                                Console.logMsg("Shooting...");
                                Runway.enableDirector();
                                tmrStageTimeOut.reset();
                                mStage++;
                            case 5:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 6:
                                Console.logMsg("Turning...");
                                Chassis.resetAngle();
                                Runway.disableReels();
                                Runway.disableDirector();
                                if((DriverStation.getAlliance().get() == Alliance.Red && mSelectedStartingPosition == StartingPosition.SPEAKER_AMP_SIDE) || (DriverStation.getAlliance().get() == Alliance.Blue && mSelectedStartingPosition == StartingPosition.SPEAKER_SOURCE_SIDE)){ 
                                    Chassis.setDrivePower(-0.125, 0.0);
                                }else{
                                    Chassis.setDrivePower(0.0, -0.125);
                                }
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 7:
                                if(Math.abs(Chassis.getDriveAngle()) >= turnAngle || tmrStageTimeOut.get() > 2.0) mStage++;
                                break;
                            case 8:
                                Console.logMsg("Backing up...");
                                Chassis.setDrivePower(-0.125, -0.125);
                                Chassis.resetDistance();
                                Runway.enableDirector();
                                Backstage.enableIntake();
                                Backstage.enableAssistantDirector();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 9:
                                if(Backstage.isLoaded()){
                                    Runway.disableDirector();
                                    Backstage.disableIntake();
                                    Backstage.disableAssistantDirector();
                                }
                                if(tmrStageTimeOut.get() > 4.0 || Chassis.getDriveDistance() <= -53.0 || Backstage.isLoaded()) mStage++;
                                break;
                            case 10:
                                Console.logMsg("Driving Forward...");
                                Chassis.resetDistance();
                                Chassis.setDrivePower(0.125, 0.125);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 11:
                                if(Backstage.isLoaded()){
                                    Runway.disableDirector();
                                    Backstage.disableIntake();
                                    Backstage.disableAssistantDirector();
                                }
                                if(tmrStageTimeOut.get() > 4.0 || Chassis.getDriveDistance() >= 55.00) mStage++;
                                break;
                            case 12:
                                Console.logMsg("Turning...");
                                Chassis.resetAngle();
                                if((DriverStation.getAlliance().get() == Alliance.Red && mSelectedStartingPosition == StartingPosition.SPEAKER_AMP_SIDE) || (DriverStation.getAlliance().get() == Alliance.Blue && mSelectedStartingPosition == StartingPosition.SPEAKER_SOURCE_SIDE)){ 
                                    Chassis.setDrivePower(0.125, 0.0);
                                }else{
                                    Chassis.setDrivePower(0.0, 0.125);
                                }
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 13:
                                if(Backstage.isLoaded()){
                                    Runway.disableDirector();
                                    Backstage.disableIntake();
                                    Backstage.disableAssistantDirector();
                                }
                                if((Math.abs(Chassis.getDriveAngle()) >= turnAngle) || tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 14:
                                Console.logMsg("Waiting for Note...");
                                Chassis.disable();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 15:
                                if(Backstage.isLoaded() || tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 16:
                                Console.logMsg("Adjusting...");
                                Runway.reverseDirector();
                                Backstage.disableIntake();
                                Backstage.disableAssistantDirector();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 17:
                                if(Runway.isLoaded() || tmrStageTimeOut.get() > 0.25) mStage++;
                                break;
                            case 18:
                                Console.logMsg("Ramping...");
                                Chassis.disable();
                                Runway.speakerShot();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 19:
                                if(tmrStageTimeOut.get() > 0.250) mStage++;
                                break;
                            case 20:
                                Console.logMsg("Shooting...");
                                Runway.enableDirector();
                                Backstage.goToPosition(Backstage.Position.RAISED);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 21:
                                if(tmrStageTimeOut.get() > 0.5) mStage++;
                                break;
                            case 22:
                                Robot.disableSubsystems();
                                mStage++;
                                break;
                            case 23:
                                Console.logMsg("End of Sequence"); mStage++;
                            default:
                                Robot.disableSubsystems();
                        } break;
                    case SPEAKER_CENTER:
                        switch(mStage) {
                            case 0:
                                Console.logMsg("Starting Sequence\"" + Sequence.SHOOT_AND_SHOOT_AGAIN.toString() + "\"-" + mSelectedStartingPosition.toString());
                                Backstage.setPivotPower(-0.25);
                                mStage++;
                                break;
                            case 1:
                                if(Backstage.isRaised()){
                                    Backstage.goToPosition(Backstage.Position.RAISED);
                                    if(tmrStageTimeOut.get() > mSelectedStartDelay.time) mStage++;
                                }
                                break;
                            case 2:
                                Console.logMsg("Spinning Reels...");
                                Runway.speakerShot();
                                Backstage.goToPosition(Backstage.Position.FLOOR);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 3:
                                if(tmrStageTimeOut.get() > 0.25) mStage++;
                                break;
                            case 4:
                                Console.logMsg("Shooting...");
                                Runway.enableDirector();
                                tmrStageTimeOut.reset();
                                mStage++;
                            case 5:
                                if(tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 6:
                                Console.logMsg("Backing up...");
                                Chassis.setDrivePower(-0.125, -0.125);
                                Chassis.resetDistance();
                                Runway.disableReels();
                                Runway.enableDirector();
                                Backstage.enableIntake();
                                Backstage.enableAssistantDirector();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 7:
                                if(Backstage.isLoaded()){
                                    Runway.disableDirector();
                                    Backstage.disableIntake();
                                    Backstage.disableAssistantDirector();
                                }
                                if(tmrStageTimeOut.get() > 4.0 || Chassis.getDriveDistance() <= -53.0 || Backstage.isLoaded()) mStage++;
                                break;
                            case 8:
                                Console.logMsg("Driving Forward...");
                                Chassis.resetDistance();
                                Chassis.setDrivePower(0.125, 0.125);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 9:
                                if(Backstage.isLoaded()){
                                    Runway.disableDirector();
                                    Backstage.disableIntake();
                                    Backstage.disableAssistantDirector();
                                }
                                if(tmrStageTimeOut.get() > 4.0 || Chassis.getDriveDistance() >= 55.00) mStage++;
                                break;
                            case 10:
                                Console.logMsg("Waiting for Note...");
                                Chassis.disable();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 11:
                                if(Backstage.isLoaded() || tmrStageTimeOut.get() > 1.0) mStage++;
                                break;
                            case 12:
                                Console.logMsg("Adjusting...");
                                Runway.reverseDirector();
                                Backstage.disableIntake();
                                Backstage.disableAssistantDirector();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 13:
                                if(Runway.isLoaded() || tmrStageTimeOut.get() > 0.25) mStage++;
                                break;
                            case 14:
                                Console.logMsg("Ramping...");
                                Chassis.disable();
                                Runway.speakerShot();
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 15:
                                if(tmrStageTimeOut.get() > 0.250) mStage++;
                                break;
                            case 16:
                                Console.logMsg("Shooting...");
                                Runway.enableDirector();
                                Backstage.goToPosition(Backstage.Position.RAISED);
                                tmrStageTimeOut.reset();
                                mStage++;
                                break;
                            case 17:
                                if(tmrStageTimeOut.get() > 0.5) mStage++;
                                break;
                            case 18:
                                Robot.disableSubsystems();
                                mStage++;
                                break;
                            case 19:
                                Console.logMsg("End of Sequence"); mStage++;
                            default:
                                Robot.disableSubsystems();
                        } break;
                    default:
                        switch(mStage) {
                            case 0:
                                Console.logMsg("Invalid starting position");
                                mStage++; break;
                            default:
                                Robot.disableSubsystems();
                        } break;
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
            Backstage.disablePIDs();
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
        chsSequence.addOption(Sequence.SHOOTSTAY.label, Sequence.SHOOTSTAY);
        chsSequence.addOption(Sequence.JUSTTRAVEL.label, Sequence.JUSTTRAVEL);
        chsSequence.addOption(Sequence.SHOOTTRAVEL.label, Sequence.SHOOTTRAVEL);
        chsSequence.addOption(Sequence.SHOOT_AND_SHOOT_AGAIN.label, Sequence.SHOOT_AND_SHOOT_AGAIN);
        chsSequence.setDefaultOption(Sequence.NOTHING.label, Sequence.NOTHING);
        SmartDashboard.putData("Sequence", chsSequence);

        chsStartDelay.addOption(StartDelay.ZERO.label, StartDelay.ZERO);
        chsStartDelay.addOption(StartDelay.TWO.label, StartDelay.TWO);
        chsStartDelay.addOption(StartDelay.FIVE.label, StartDelay.FIVE);
        chsStartDelay.addOption(StartDelay.TEN.label, StartDelay.TEN);
        chsStartDelay.setDefaultOption(StartDelay.ZERO.label, StartDelay.ZERO);
        SmartDashboard.putData("Start Delay", chsStartDelay);

        chsStartingPosition.addOption(StartingPosition.SPEAKER_CENTER.label, StartingPosition.SPEAKER_CENTER);
        chsStartingPosition.addOption(StartingPosition.SPEAKER_SOURCE_SIDE.label, StartingPosition.SPEAKER_SOURCE_SIDE);
        chsStartingPosition.addOption(StartingPosition.SPEAKER_AMP_SIDE.label, StartingPosition.SPEAKER_AMP_SIDE);
        chsStartingPosition.addOption(StartingPosition.DRIVER_STATION_WALL.label, StartingPosition.DRIVER_STATION_WALL);
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
        Hanger.periodic();
        Backstage.periodic();
    }
}
