/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

/**
 * @author rhhs
 */
public class TurnLeft extends CommandBase {

    private double m_timeout;

    public TurnLeft(double timeout) {
        m_timeout = timeout;
        requires(m_chassis);
    }

    protected void initialize() {
        setTimeout(m_timeout);
    }

    protected void execute() {
        m_chassis.turnLeft();
    }

    protected boolean isFinished() {
        return isTimedOut();
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}