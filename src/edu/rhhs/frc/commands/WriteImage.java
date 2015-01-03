package edu.rhhs.frc.commands;

import edu.rhhs.frc.vision.Target;

/**
 * @author rhhs
 */
public class WriteImage extends CommandBase {
    
    public WriteImage() {
        requires(m_camera);
        requires(m_cameraLight);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        
        // Make sure the light is on first
        boolean previousCameraStatus = m_cameraLight.getLightStatus();
        if (previousCameraStatus == false) {
            m_cameraLight.setLight(true);
        }
        
        m_camera.writeImages();
        
        // Put light status back...
         if (m_cameraLight.getLightStatus() != previousCameraStatus) {
            m_cameraLight.setLight(previousCameraStatus);
        }

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return m_camera.isImageProcessingFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
