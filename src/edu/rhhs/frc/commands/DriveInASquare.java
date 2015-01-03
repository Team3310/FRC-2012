/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * @author rhhs
 */
public class DriveInASquare extends CommandGroup {
    
    public DriveInASquare() {

        addSequential(new DriveStraight(1));
        addSequential(new TurnLeft(1));
        addSequential(new DriveStraight(1));
        addSequential(new TurnLeft(1));
        addSequential(new DriveStraight(1));
        addSequential(new TurnLeft(1));
        addSequential(new DriveStraight(1));
    }
   }

