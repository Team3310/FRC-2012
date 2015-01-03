package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.main.RobotMap;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author rhhs
 */
public class Claw extends PIDSubsystem {
    
    // Tolerance, calibration info
    private final static double MAX_ALLOWABLE_ERROR_VOLTS = 0.1;
    private final static double ZERO_DEG_OFFSET = 5;
    private final static double VOLTS_PER_DEG = 5.0/360;

    // Constants for preset positions (voltage from the potentiometer at various postions)
    public static final double UP = 90;
    public static final double DOWN = 0;
    
    // PID Constants
    private final static double kP = 2.0;
    private final static double kI = 0.0;
    private final static double kD = 0.0;
    
    private Jaguar m_motorController = null;
    private AnalogChannel m_potentiometer = null;
    
    public Claw() {
        super(kP, kI, kD);
        try {
            m_motorController = new Jaguar(RobotMap.CLAW_MOTOR_PWM_PORT);
            m_potentiometer = new AnalogChannel(RobotMap.CLAW_POTENTIOMETER_ANALOG_PORT);
            setPosition(UP);
        } 
        catch (Exception e) {
            System.out.println("Unknown error initializing claw.  Message = " + e.getMessage());
        }
    }
    
    // Methods for manual control
    public void start(double speed) {
        // When a button is HELD down this is called repeatedly.  The disable() function sets the motor to 0
        // which causes the motor to switch between commanded speed and 0.  We only want to call disable once
        if (getPIDController().isEnable()) {
            this.disable();   
        }
        m_motorController.set(speed);  
   }


    public void stop() {
        m_motorController.stopMotor();
    }
    
    // Methods for PID control to a position
    public void startPID() {
        this.enable();
    }
    
    public void setPosition(double positionDeg) {
        this.setSetpoint((ZERO_DEG_OFFSET + positionDeg) * VOLTS_PER_DEG);
    }

    public boolean atSetpoint() {
        return Math.abs(getPIDController().getSetpoint() - returnPIDInput()) < MAX_ALLOWABLE_ERROR_VOLTS;
    }
    
    protected double returnPIDInput() {
        return m_potentiometer.getVoltage();
    }

    protected void usePIDOutput(double output) {
        m_motorController.set(output);
    }

    public void initDefaultCommand() {
    }
    
    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
        SmartDashboard.putDouble("Claw Pot", m_potentiometer.getVoltage());
    }
}
