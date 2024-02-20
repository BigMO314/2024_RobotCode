package frc.robot.subsystem;

import java.awt.Color;

import com.ctre.phoenix.led.CANdle.LEDStripType;

import edu.wpi.first.hal.LEDJNI;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
@SuppressWarnings("unused")
public class Lights {
    private static AddressableLED ledAligned = new AddressableLED(0);
    private static AddressableLEDBuffer LedAlignedBuffer = new AddressableLEDBuffer(60);

    private Lights(){

    }

    public static void enableLED(){
        LedAlignedBuffer.setLED(0, edu.wpi.first.wpilibj.util.Color.kWhite);
    }

    public static void disabledLED(){
        LedAlignedBuffer.setLED(0, edu.wpi.first.wpilibj.util.Color.kBlack);
    }

   
    public static void init(){
        ledAligned.setLength(LedAlignedBuffer.getLength());
        ledAligned.setData(LedAlignedBuffer);
        ledAligned.start();
    }

    public static void periodic(){

    }
}
