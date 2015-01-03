package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.main.RobotMap;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author rhhs
 */
public class Conveyor extends Subsystem {
    
    private Relay m_relay;
    private boolean m_status;
    
    public Conveyor() {
        try {
            m_relay = new Relay(RobotMap.CONVEYOR_RELAY_PORT, Relay.Direction.kForward);
        } 
        catch (Exception e) {
            System.out.println("Unknown error initializing conveyor.  Message = " + e.getMessage());
        }
    }
    
    public void start() {
        m_relay.set(Relay.Value.kOn);
        m_status = true;
    }

    public void stop() {
        m_relay.set(Relay.Value.kOff);
        m_status = false;
    }
        
    public void initDefaultCommand() {
    }
    
    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
        SmartDashboard.putBoolean("Conveyor Status", m_status);
    }
}
