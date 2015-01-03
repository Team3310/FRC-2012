/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

/**
 * @author rhhs
 */
public class ElevatorTimed extends CommandBase {
    
    private double m_timeout;
    private double m_speed;

    public ElevatorTimed(double timeout, double speed) {
        m_timeout = timeout;
        m_speed = speed;
        requires(m_elevator);
    }

    protected void initialize() {
        setTimeout(m_timeout);
        m_elevator.start(m_speed);
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return isTimedOut();
    }

    protected void end() {
        m_elevator.stop();
    }

    protected void interrupted() {
        m_elevator.stop();
    }
}
