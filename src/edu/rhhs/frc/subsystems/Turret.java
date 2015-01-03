package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.main.RobotMap;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author rhhs
 */
public class Turret extends PIDSubsystem {
    
    // Tolerance, calibration info
    private final static double MAX_ALLOWABLE_ERROR_DEG = 5;
    private final static double PULSES_PER_ROTATION = 360;
    
    // PID Constants
    private final static double KP = 0.025;
    private final static double KI = 0.0;
    private final static double KD = 0.004;
    
    private double m_error = 0;
    
    private CANJaguar m_motor;
    private Encoder m_encoder;
    
    public Turret() {
        super(KP, KI, KD);

        try {
            m_motor = new CANJaguar(RobotMap.TURRET_MOTOR_CAN_ID, CANJaguar.ControlMode.kPercentVbus);
            m_encoder = new Encoder(RobotMap.TURRET_ENCODER_A_DIO_PORT, RobotMap.TURRET_ENCODER_B_DIO_PORT, false, CounterBase.EncodingType.k4X);
            m_encoder.setDistancePerPulse(PULSES_PER_ROTATION / 360.0);  // pulses per deg
        } 
        catch (CANTimeoutException e) {
            System.out.println("Error initializing turret, CAN error.  Message = " + e.getMessage());
        } 
        catch (Exception ex) {
            System.out.println("Unknown exception initializing turret.  Message = " + ex.getMessage());
        } 
        finally {
            this.disable();
        }
    }
    
    // Methods for manual control
    public void start(double speed) {
        // When a button is HELD down this is called repeatedly.  The disable() function sets the motor to 0
        // which causes the motor to switch between commanded speed and 0.  We only want to call disable once
        if (getPIDController().isEnable()) {
            this.disable();   
        }
        setMotorSpeed(speed);
    }
    
    private void setMotorSpeed(double speed) {
        try {
            m_motor.setX(speed);
        } 
        catch (CANTimeoutException e) {
            System.out.println("Set motor speed CAN error.  Message = " + e.getMessage());
        }
    }

    public void stop() {
        setMotorSpeed(0);
    }
    
    // Methods for PID control to a position
    public void startPID() {
        m_encoder.reset();
        m_encoder.start();
        this.enable();
    }
    
    public void setRelativeAngle(double angleDeg) {
        this.setSetpoint(angleDeg);
    }

    public boolean atSetPoint() {
        m_error = Math.abs(getPIDController().getSetpoint() - returnPIDInput());
        return m_error < MAX_ALLOWABLE_ERROR_DEG;
    }
    
    protected double returnPIDInput() {
        return m_encoder.getDistance();
    }

    protected void usePIDOutput(double output) {
        setMotorSpeed(output);
    }

    public void initDefaultCommand() {
    }
    
    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
//        SmartDashboard.putData("Turret PID", (SendablePIDController)getPIDController());
        SmartDashboard.putDouble("Turret Encoder Distance", m_encoder.getDistance());
        SmartDashboard.putDouble("Turret PID error", getPIDController().getError());
        SmartDashboard.putBoolean("Turret atSetPoint", atSetPoint());
    }
}
