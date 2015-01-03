package edu.rhhs.frc.subsystems;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.rhhs.frc.main.OI;
import edu.rhhs.frc.commands.DriveWithJoystick;
import edu.rhhs.frc.main.GlobalProperties;
import edu.rhhs.frc.main.RobotMap;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

public class Chassis extends Subsystem {

    private RobotDrive m_drive;
    private boolean m_fineControl;

    public Chassis() {
        try {
            m_drive = new RobotDrive(
                    new CANJaguar(RobotMap.DRIVE_LEFT_FRONT_CAN_ID), 
                    new CANJaguar(RobotMap.DRIVE_LEFT_REAR_CAN_ID), 
                    new CANJaguar(RobotMap.DRIVE_RIGHT_FRONT_CAN_ID), 
                    new CANJaguar(RobotMap.DRIVE_RIGHT_REAR_CAN_ID));
                    
            m_drive.setSafetyEnabled(false);
        } 
        catch (CANTimeoutException e) {
            System.out.println("CAN timeout initializing chassis.  Message = " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Unknown error initializing chassis.  Message = " + e.getMessage());
        }
    }

    public void initDefaultCommand() {
        setDefaultCommand(new DriveWithJoystick()); // set default command   
    }
    
    public void driveWithJoystick() {
        if (m_drive != null) {
            if (m_fineControl) {
                m_drive.arcadeDrive(
                        OI.getInstance().getDriverJoystick().getAxis(Joystick.AxisType.kY) * GlobalProperties.SCALING_FOR_FINE_CONTROL,
                        OI.getInstance().getDriverJoystick().getAxis(Joystick.AxisType.kX) * GlobalProperties.SCALING_FOR_FINE_CONTROL);
            } else {
                m_drive.arcadeDrive(OI.getInstance().getDriverJoystick());
            }
        }
    }
    
    public boolean isFineControl() {
        return m_fineControl;
    }

    public void setFineControl(boolean fineControl) {
        this.m_fineControl = fineControl;
    }
    
    // Test code...
    public void straight() { // sets the motor speeds to drive straight (no turn)
        m_drive.arcadeDrive(1.0, 0.0);
    }

    public void turnLeft() { // sets the motor speeds to start a left turn
        m_drive.arcadeDrive(0.0, 1.0);
    }

    public void turnRight() { // sets the motor speeds to start a right turn
        m_drive.arcadeDrive(0.0, -1.0);
    }
}
