package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.main.RobotMap;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author rhhs
 */
public class TurretCANEncoder extends Subsystem{
    
    // Tolerance, calibration info
    private final static double MOTOR_ROTATIONS_PER_TURRET_DEG = 1/90;
    private final static double MAX_MOTOR_ROTATIONS_SOFT_LIMIT = 45 * MOTOR_ROTATIONS_PER_TURRET_DEG;
    private final static double MIN_MOTOR_ROTATIONS_SOFT_LIMIT = -45 * MOTOR_ROTATIONS_PER_TURRET_DEG;
    private final static double MAX_ALLOWABLE_ERROR_DEG = 0.5;
    private final static int    PULSES_PER_ROTATION = 360;
    
    // PID Constants
    private final static double KP = 5000.0;  // Tetrix motor 10k too high, 5k stops hard but right on, 2k small error 
    private final static double KI = 0.0;
    private final static double KD = 0.0;
    
    private int m_errorCount = 0;
    private double m_error = 0;
    
    private CANJaguar m_motorController;
    
    public TurretCANEncoder() {
        try {
            m_motorController = new CANJaguar(RobotMap.TURRET_MOTOR_CAN_ID, CANJaguar.ControlMode.kPercentVbus);
        } 
        catch (CANTimeoutException ex) {
            System.out.println("CAN timeout initializing turret.  Message = " + ex.getMessage());
            m_errorCount++;
        } 
        catch (Exception ex) {
            System.out.println("Unknown exception initializing turret.  Message = " + ex.getMessage());
            m_errorCount++;
        } 
    }
    
    // We need to switch the controller mode when we command the turret to rotate manually 
    // or to a relative position using the CANJaguar built in PID controller
    private void setPositionController() throws CANTimeoutException {
        if (m_motorController.getControlMode() != CANJaguar.ControlMode.kPosition) {
            m_motorController.changeControlMode(CANJaguar.ControlMode.kPosition);
            m_motorController.configEncoderCodesPerRev(PULSES_PER_ROTATION);
            m_motorController.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            m_motorController.configSoftPositionLimits(MAX_MOTOR_ROTATIONS_SOFT_LIMIT, MIN_MOTOR_ROTATIONS_SOFT_LIMIT);
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
            System.out.println("CAN timeout start turret.  Message = " + ex.getMessage());
            m_errorCount++;
        }
    }

    public void stop() {
        try {
            m_motorController.setX(0);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN timeout stop turret.  Message = " + ex.getMessage());
            m_errorCount++;
        }
    }
    
    // Methods for PID control to a position
    public void setRelativeAngle(double relativeAngleDeg) {
        try {
            setPositionController();
            m_motorController.setX(relativeAngleDeg * MOTOR_ROTATIONS_PER_TURRET_DEG);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN timeout setRelativeAngle turret.  Message = " + ex.getMessage());
            m_errorCount++;
        }
    }

    public boolean atSetPoint() {
        try {
            m_error = Math.abs(m_motorController.getPosition() - m_motorController.getX());
        } catch (CANTimeoutException ex) {
            System.out.println("CAN timeout atSetPoint turret.  Message = " + ex.getMessage());
            m_errorCount++;
        }
        return m_error < MAX_ALLOWABLE_ERROR_DEG;
    }
    
    public void initDefaultCommand() {
    }
    
    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
        try {
//        SmartDashboard.putData("Turret PID", (SendablePIDController)getPIDController());
            SmartDashboard.putDouble("Turret Encoder Distance", m_motorController.getPosition());
            SmartDashboard.putDouble("Turret PID error", Math.abs(m_motorController.getPosition() - m_motorController.getX()));
            SmartDashboard.putDouble("Turret PID X", m_motorController.getX());
            SmartDashboard.putInt("Turret Error Count", m_errorCount);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN timeout updateStatus turret.  Message = " + ex.getMessage());
            m_errorCount++;
        }

    }
}
