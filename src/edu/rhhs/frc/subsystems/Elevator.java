package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.main.RobotMap;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author rhhs
 */
public class Elevator extends Subsystem {
    
    private Relay m_relay;
    private DigitalInput m_upperLimitSwitch;
    private boolean m_status;
   
    public Elevator(){
        try {
            m_relay = new Relay(RobotMap.ELEVATOR_RELAY_PORT, Relay.Direction.kForward);
            m_upperLimitSwitch = new DigitalInput(RobotMap.ELEVATOR_UPPER_SWITCH_DIO_PORT);
        } 
        catch (Exception e) {
            System.out.println("Unknown error initializing elevator.  Message = " + e.getMessage());
        }
    }
    
    public void start(){
        m_relay.set(Relay.Value.kOn);
        m_status = true;
    }
    
    public void stop(){
        m_relay.set(Relay.Value.kOff);
        m_status = false;
    }
    
    public boolean upperLimitSwitch() {
        return m_upperLimitSwitch.get();
    }
    
    public void initDefaultCommand() {
    }

    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
        SmartDashboard.putBoolean("Elevator Status", m_status);
    }
}
