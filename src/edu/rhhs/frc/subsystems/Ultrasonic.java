package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.main.RobotMap;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author rhhs
 */
public class Ultrasonic extends Subsystem {
    
    private AnalogChannel m_analogUltrasonicSensor;
    private static double VOLTS_PER_INCH = 5.0/512.0;
    private double m_distance = 0;
    
    public Ultrasonic() {
        try {
            m_analogUltrasonicSensor = new AnalogChannel(RobotMap.ULTRASONIC_ANALOG_PORT);
        } 
        catch (Exception e) {
            System.out.println("Unknown error initializing ultrasonic.  Message = " + e.getMessage());
        }
    }

    public void WriteDistance() {
        double voltage = m_analogUltrasonicSensor.getAverageVoltage();
        m_distance = voltage / VOLTS_PER_INCH;
    }
   
    public void initDefaultCommand() {
    }
    
    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
        SmartDashboard.putDouble("Ultrasonic Distance", m_distance);
    }    
}
