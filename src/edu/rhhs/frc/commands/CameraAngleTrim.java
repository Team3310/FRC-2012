package edu.rhhs.frc.commands;

/**
 * @author rhhs
 */
public class CameraAngleTrim extends CommandBase {
    
    private double m_trimValue;
    
    public CameraAngleTrim(double trimValue) {
        m_trimValue = trimValue;
        requires(m_camera);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_camera.setAngleOffset(m_camera.getAngleOffset() + m_trimValue);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
