package edu.rhhs.frc.commands;

/**
 * @author rhhs
 */
public class ElevatorStop extends CommandBase {
    
   public ElevatorStop() {
        requires(m_elevator);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_elevator.stop();
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
