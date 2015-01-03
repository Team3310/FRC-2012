package edu.rhhs.frc.commands;

/**
 * @author rhhs
 */
public class ConveyorStop extends CommandBase {
    
    public ConveyorStop() {
        requires(m_conveyor);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_conveyor.stop();
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
