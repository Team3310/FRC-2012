package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.main.RobotMap;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author rhhs
 */
public class ElevatorCAN extends Subsystem{
    
    // Tolerance, calibration info
    private final static int    PULSES_PER_ROTATION = 360;
    
    // PID Constants
    private final static double KP = 5000.0;  // Tetrix motor 10k too high, 5k stops hard but right on, 2k small error 
    private final static double KI = 0.0;
    private final static double KD = 0.0;
    
    private int m_errorCount = 0;
    
    private CANJaguar m_motorController;
    private DigitalInput m_upperLimitSwitch;
    
    public ElevatorCAN() {
        try {
            m_motorController = new CANJaguar(RobotMap.ELEVATOR_MOTOR_CAN_ID, CANJaguar.ControlMode.kPercentVbus);
            m_upperLimitSwitch = new DigitalInput(RobotMap.ELEVATOR_UPPER_SWITCH_DIO_PORT);
            stop();
        } 
        catch (CANTimeoutException ex) {
            System.out.println("CAN timeout initializing elevator.  Message = " + ex.getMessage());
            m_errorCount++;
        } 
        catch (Exception ex) {
            System.out.println("Unknown exception initializing elevator.  Message = " + ex.getMessage());
            m_errorCount++;
        } 
    }
    
    // We need to switch the controller mode when we command the Elevator to rotate manually 
    // or to a relative position using the CANJaguar built in PID controller
    private void setPositionController() throws CANTimeoutException {
        if (m_motorController.getControlMode() != CANJaguar.ControlMode.kPosition) {
            m_motorController.changeControlMode(CANJaguar.ControlMode.kPosition);
            m_motorController.configEncoderCodesPerRev(PULSES_PER_ROTATION);
            m_motorController.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
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
        // Note: When a button is HELD down this is called repeatedly.  
        try {
            setPercentVBusController();
            m_motorController.setX(speed);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN timeout start elevator.  Message = " + ex.getMessage());
            m_errorCount++;
        }
    }

    public void stop() {
        try {
            m_motorController.setX(0);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN timeout stop elevator.  Message = " + ex.getMessage());
            m_errorCount++;
        }
    }
    
    // Methods for PID control to a position
    public void setRelativeAngle(double relativeRotations) {
        try {
            setPositionController();
            m_motorController.setX(relativeRotations);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN timeout setRelativeAngle elevator.  Message = " + ex.getMessage());
            m_errorCount++;
        }
    }

    public boolean upperLimitSwitch() {
        return m_upperLimitSwitch.get();
    }
    
    public void initDefaultCommand() {
    }
    
    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
        try {
//        SmartDashboard.putData("Elevator PID", (SendablePIDController)getPIDController());
            SmartDashboard.putDouble("Elevator Encoder Rotations", m_motorController.getPosition());
            SmartDashboard.putDouble("Elevator PID error", Math.abs(m_motorController.getPosition() - m_motorController.getX()));
            SmartDashboard.putDouble("Elevator PID X", m_motorController.getX());
            SmartDashboard.putInt("Elevator Error Count", m_errorCount);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN timeout updateStatus Elevator.  Message = " + ex.getMessage());
            m_errorCount++;
        }
    }
}
