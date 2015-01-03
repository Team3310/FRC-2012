package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.commands.ClawProportional;
import edu.rhhs.frc.main.OI;
import edu.rhhs.frc.main.RobotMap;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author rhhs
 */
public class ClawCANProportional extends Subsystem{
    
    // Tolerance, calibration info
    private final static double MAX_MOTOR_ROTATIONS_SOFT_LIMIT = 0.80;
    private final static double MIN_MOTOR_ROTATIONS_SOFT_LIMIT = 0.00; 
    private final static int    POTENTIOMETER_TURNS = 1;
    public final static double  CLAW_UP = 0.75; 
    public final static double  CLAW_DOWN = 0.25;
    
    // PID Constants
    private final static double KP = 600.0;  
    private final static double KI = 0.0;
    private final static double KD = 0.0;
    
    private int m_errorCount = 0;
    private double m_throttle = 0;
    
    private CANJaguar m_motorController;
    
    public ClawCANProportional() {
        try {
            m_motorController = new CANJaguar(RobotMap.CLAW_MOTOR_CAN_ID, CANJaguar.ControlMode.kPercentVbus);
            setPositionController();
        } 
        catch (CANTimeoutException ex) {
            System.out.println("CAN timeout initializing claw proportional.  Message = " + ex.getMessage());
            m_errorCount++;
        } 
        catch (Exception ex) {
            System.out.println("Unknown exception initializing claw proportional.  Message = " + ex.getMessage());
            m_errorCount++;
        } 
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new ClawProportional()); // set default command   
    }
    
    // We need to switch the controller mode when we command the Claw to rotate manually 
    // or to a relative position using the CANJaguar built in PID controller
    private void setPositionController() throws CANTimeoutException {
        if (m_motorController.getControlMode() != CANJaguar.ControlMode.kPosition) {
            m_motorController.changeControlMode(CANJaguar.ControlMode.kPosition);
            m_motorController.setPositionReference(CANJaguar.PositionReference.kPotentiometer);
            m_motorController.configSoftPositionLimits(MAX_MOTOR_ROTATIONS_SOFT_LIMIT, MIN_MOTOR_ROTATIONS_SOFT_LIMIT);
            m_motorController.configPotentiometerTurns(POTENTIOMETER_TURNS);
            m_motorController.setPID(KP, KI, KD);
        }
        
        // Need to do this after a mode change but also each time we command a new relative 
        // position as it resets the encoder counter to 0
        m_motorController.enableControl();   
    }
    
    private void setPercentVBusController() throws CANTimeoutException {
        if (m_motorController.getControlMode() != CANJaguar.ControlMode.kPercentVbus) {
            m_motorController.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            m_motorController.enableControl();
        }
    }

    // Methods for manual control
    public void start(double speed) {
//        // Note: When a button is HELD down this is called repeatedly.  
//        try {
//            setPercentVBusController();
//            m_motorController.setX(speed);
//        } catch (CANTimeoutException ex) {
//            System.out.println("CAN timeout start claw.  Message = " + ex.getMessage());
//            m_errorCount++;
//        }
    }

    public void stop() {
//        try {
//            setPercentVBusController();
//            m_motorController.setX(0);
//        } catch (CANTimeoutException ex) {
//            System.out.println("CAN timeout stop claw proportional.  Message = " + ex.getMessage());
//            m_errorCount++;
//        }
    }
    
    // Methods for PID control to a position
     public void setAngle(double rotations) {
//        try {
//            setPositionController();
//            m_motorController.setX(rotations);
//        } catch (CANTimeoutException ex) {
//            System.out.println("CAN timeout setAngle claw.  Message = " + ex.getMessage());
//            m_errorCount++;
//        }
    }

    public void setProportionalControl() {
        try {
            setPositionController();
            m_throttle = OI.getInstance().getDriverJoystick().getRawAxis(RobotMap.LOGITECH_ATTACK_3_THROTTLE_AXIS);
            double position = (m_throttle + 1) / 2 * (CLAW_UP - CLAW_DOWN) + CLAW_DOWN;
            m_motorController.setX(position);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN timeout setAngle claw proportional.  Message = " + ex.getMessage());
            m_errorCount++;
        }
    }

    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
        try {
//            SmartDashboard.putData("Claw PID", (SendablePIDController)getPIDController());
            SmartDashboard.putDouble("Claw Controller Position", m_throttle);
            SmartDashboard.putDouble("Claw Position", m_motorController.getPosition());
            SmartDashboard.putDouble("Claw PID error", Math.abs(m_motorController.getPosition() - m_motorController.getX()));
            SmartDashboard.putDouble("Claw PID X", m_motorController.getX());
            SmartDashboard.putInt("Claw Error Count", m_errorCount);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN timeout updateStatus claw proportional.  Message = " + ex.getMessage());
            m_errorCount++;
        }
    }
}
