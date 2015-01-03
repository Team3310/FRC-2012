package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.main.RobotMap;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Utility;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author rhhs
 */
public class ShooterCANEncoderRPM extends PIDSubsystem {
    
    // Tolerance, calibration info
    private final static double MAX_ALLOWABLE_ERROR_RPM = 100;
    private final static double PULSES_PER_ROTATION = 250;
    private final static int NUM_AVERAGED_CYCLES = 5;
    
    // PID Constants
    private final static double KP = 0.000363;
    private final static double KI = 0.0000249;
    private final static double KD = 0.0;
    
    private double m_error = 0;
    
    private CANJaguar m_motorControllerAM1;
    private CANJaguar m_motorControllerAM2;
    private CANJaguar m_motorControllerFP1;
    private CANJaguar m_motorControllerFP2;
    
    private Encoder m_encoder;
    
    private int m_rpmIndex;
    private double[] m_motorRPM;
    private double m_averagedMotorRPM;
    private long lastTimeMicroSeconds;
    
    public ShooterCANEncoderRPM() {
        super(KP, KI, KD);

        try {
            m_motorControllerAM1 = new CANJaguar(RobotMap.SHOOTER_MOTOR_AM_1_CAN_ID, CANJaguar.ControlMode.kPercentVbus);
            m_motorControllerAM2 = new CANJaguar(RobotMap.SHOOTER_MOTOR_AM_2_CAN_ID, CANJaguar.ControlMode.kPercentVbus);
            m_motorControllerFP1 = new CANJaguar(RobotMap.SHOOTER_MOTOR_FP_1_CAN_ID, CANJaguar.ControlMode.kPercentVbus);
            m_motorControllerFP2 = new CANJaguar(RobotMap.SHOOTER_MOTOR_FP_2_CAN_ID, CANJaguar.ControlMode.kPercentVbus);

            m_motorRPM = new double[NUM_AVERAGED_CYCLES];
            resetMotorRPM();
            
            m_encoder = new Encoder(RobotMap.SHOOTER_ENCODER_A_DIO_PORT, RobotMap.SHOOTER_ENCODER_B_DIO_PORT, false, CounterBase.EncodingType.k1X);
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

    private void resetMotorRPM() {
        for (int i = 0; i < NUM_AVERAGED_CYCLES; i++) {
        	m_motorRPM[i] = 0.0;
        }
        lastTimeMicroSeconds = Utility.getFPGATime();
        m_averagedMotorRPM = 0;
    }
        
	// Update the current speed calculation
    public void updateMotorRPM() { 
    	long currentTimeMicroSeconds = Utility.getFPGATime();
    	long deltaTimeMillis = currentTimeMicroSeconds - lastTimeMicroSeconds;
    	lastTimeMicroSeconds = currentTimeMicroSeconds;
    	
    	// RPM = counts/microsec * 1000000 microsec/sec * 60 sec/min * 1/counts_per_rev
    	double replacedMotorRPM = m_motorRPM[m_rpmIndex];
    	m_motorRPM[m_rpmIndex] = (m_encoder.getRaw() / (double)deltaTimeMillis) * 60000000 / PULSES_PER_ROTATION;
    	m_encoder.reset();

    	m_rpmIndex++;
    	if (m_rpmIndex == NUM_AVERAGED_CYCLES) {
    		m_rpmIndex = 0;
    	}
    	m_averagedMotorRPM += (m_motorRPM[m_rpmIndex] - replacedMotorRPM) / NUM_AVERAGED_CYCLES;
    }
    
    public void stop() {
        setMotorSpeed(0);
        this.disable();
    }
    
    // Methods for PID control to a position
    public void startPID() {
    	resetMotorRPM();
    	m_encoder.start();
        this.enable();
    }
    
    public void setRPM(double rpm) {
        this.setSetpoint(rpm);
    }

    public boolean atSetPoint() {
        m_error = Math.abs(getPIDController().getSetpoint() - returnPIDInput());
        return m_error < MAX_ALLOWABLE_ERROR_RPM;
    }
    
    protected double returnPIDInput() {
        return m_averagedMotorRPM;
    }

    protected void usePIDOutput(double percentVbus) {
        setMotorSpeed(percentVbus);
    }

    private void setMotorSpeed(double percentVbus) {
        try {
        	// Don't let the shooter wheel go backwards
        	if (percentVbus < 0) {
        		percentVbus = 0;
        	}
            m_motorControllerAM1.setX(percentVbus);
            m_motorControllerAM2.setX(percentVbus);
            m_motorControllerFP1.setX(percentVbus);
            m_motorControllerFP2.setX(percentVbus);
        } 
        catch (CANTimeoutException e) {
            System.out.println("Set motor speed CAN error.  Message = " + e.getMessage());
        }
    }

    public void initDefaultCommand() {
    }
    
    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
//        SmartDashboard.putData("Shooter PID", (SendablePIDController)getPIDController());
        SmartDashboard.putDouble("Shooter Average RPM", m_averagedMotorRPM);
        SmartDashboard.putDouble("Shooter Instant RPM", m_motorRPM[m_rpmIndex]);
        SmartDashboard.putDouble("Shooter PID error", getPIDController().getError());
        SmartDashboard.putBoolean("Shooter atSetPoint", atSetPoint());
    }
}
