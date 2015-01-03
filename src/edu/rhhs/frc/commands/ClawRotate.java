package edu.rhhs.frc.commands;

/**
 * @author rhhs
 */
public class ClawRotate extends CommandBase {
    
    private double m_speed;

    public ClawRotate(double speed) {
        m_speed = speed;
        requires(m_claw);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_turret.start(m_speed);
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
        m_turret.stop();
    }
}
