package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.main.RobotMap;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author rhhs
 */
public class CameraLight extends Subsystem {

    private Relay m_lightRelay = null;
    private boolean m_lightStatus = false;

    public CameraLight() {
        try {
            m_lightRelay = new Relay(RobotMap.CAMERA_LIGHT_RELAY_PORT, Relay.Direction.kForward);
        } 
        catch (Exception e) {
            System.out.println("Unknown error initializing camera light.  Message = " + e.getMessage());
        }
    }

    public void setLight(boolean status) {
        m_lightRelay.set(status == true ? Relay.Value.kOn : Relay.Value.kOff);
        m_lightStatus = status;
    }
    
    public boolean getLightStatus() {
        return m_lightStatus;
    }
    
    public void initDefaultCommand() {
    }
    
    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
        SmartDashboard.putBoolean("Camera Light Status", m_lightStatus);
    }

}
