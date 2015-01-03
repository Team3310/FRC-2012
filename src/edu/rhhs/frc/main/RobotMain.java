/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.rhhs.frc.main;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.rhhs.frc.commands.CommandBase;
import edu.rhhs.frc.commands.DriveInASquare;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends IterativeRobot {

    Command autonomousCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // instantiate the command used for the autonomous period
        autonomousCommand = new DriveInASquare();

        // Initialize all subsystems
        CommandBase.init();
        updateStatus();
        System.out.println("Robot Init Completed");
    }

    public void disabledInit() {
        updateStatus();
        System.out.println("Disabled Init Completed");
    }
    
    public void autonomousInit() {
        // schedule the autonomous command (example)
        autonomousCommand.start();
        updateStatus();
        System.out.println("Autonomous Init Completed");
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        updateStatus();
    }

    public void teleopInit() {
	// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        autonomousCommand.cancel();
        updateStatus();
        System.out.println("Teleop Init Completed");
   }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        updateStatus();
    }
    
    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
        try {
            CommandBase.m_camera.updateStatus();
            CommandBase.m_cameraLight.updateStatus();
            CommandBase.m_conveyor.updateStatus();
            CommandBase.m_elevator.updateStatus();
            CommandBase.m_turret.updateStatus();
            CommandBase.m_claw.updateStatus();
            CommandBase.m_shooter.updateStatus();
        }
        catch (Exception e) {
            // Do nothing... just don't want to crash the robot
        }
    }
}
