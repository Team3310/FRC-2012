package edu.rhhs.frc.commands;

/**
 * @author rhhs
 */
public class ElevatorSetRelativeAngle extends CommandBase {
    
    private static final double TIMEOUT = 10.0;   // Just in case it can't get to the angle
    private double m_relativeRotations;
    private double m_timeout;
    
    public ElevatorSetRelativeAngle(double relativeRotations) {
        this.m_relativeRotations = relativeRotations;
        m_timeout = TIMEOUT;
        requires(m_elevator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        setTimeout(m_timeout);
        m_elevator.setRelativeAngle(m_relativeRotations);
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
        m_elevator.stop();
    }
}
