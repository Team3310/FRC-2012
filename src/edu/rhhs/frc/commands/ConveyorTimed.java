package edu.rhhs.frc.commands;

/**
 * @author rhhs
 */
public class ConveyorTimed extends CommandBase {
    
    private double m_timeout;

    public ConveyorTimed(double timeout) {
        m_timeout = timeout;
        requires(m_conveyor);
    }

    protected void initialize() {
        setTimeout(m_timeout);
        m_conveyor.start();
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return isTimedOut();
    }

    protected void end() {
        m_conveyor.stop();
    }

    protected void interrupted() {
        m_conveyor.stop();
    }
}
