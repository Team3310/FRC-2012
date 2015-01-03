package edu.rhhs.frc.subsystems;

import edu.rhhs.frc.commands.ShooterSetSpeed;
import edu.rhhs.frc.main.OI;
import edu.rhhs.frc.main.RobotMap;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Utility;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author rhhs
 */
public class ShooterCANThrottle extends Subsystem{
    
    private final static double PULSES_PER_ROTATION = 250;
    private final static int NUM_AVERAGED_CYCLES = 5;

    private int m_errorCount = 0;
    private double m_throttle = -1;
    private double m_percentVBus = 0;
    
    private CANJaguar m_motorControllerAM1;
    private CANJaguar m_motorControllerAM2;
    private CANJaguar m_motorControllerFP1;
    private CANJaguar m_motorControllerFP2;
    
    private Encoder m_encoder;
    
    private int m_rpmIndex;
    private double[] m_motorRPM;
    private double m_averagedMotorRPM;
    private long lastTimeMicroSeconds;
    
    public ShooterCANThrottle() {
        try {
            m_motorControllerAM1 = new CANJaguar(RobotMap.SHOOTER_MOTOR_AM_1_CAN_ID, CANJaguar.ControlMode.kPercentVbus);
            m_motorControllerAM2 = new CANJaguar(RobotMap.SHOOTER_MOTOR_AM_2_CAN_ID, CANJaguar.ControlMode.kPercentVbus);
            m_motorControllerFP1 = new CANJaguar(RobotMap.SHOOTER_MOTOR_FP_1_CAN_ID, CANJaguar.ControlMode.kPercentVbus);
            m_motorControllerFP2 = new CANJaguar(RobotMap.SHOOTER_MOTOR_FP_2_CAN_ID, CANJaguar.ControlMode.kPercentVbus);

            m_motorRPM = new double[NUM_AVERAGED_CYCLES];
            resetMotorRPM();
            
            m_encoder = new Encoder(RobotMap.SHOOTER_ENCODER_A_DIO_PORT, RobotMap.SHOOTER_ENCODER_B_DIO_PORT, false, CounterBase.EncodingType.k1X);
        } 
        catch (CANTimeoutException ex) {
            System.out.println("CAN timeout initializing shooter.  Message = " + ex.getMessage());
            m_errorCount++;
        } 
        catch (Exception ex) {
            System.out.println("Unknown exception initializing shooter.  Message = " + ex.getMessage());
            m_errorCount++;
        } 
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new ShooterSetSpeed()); // set default command   
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

    public void setThrottleSpeed() {
        try {
            // Convert the -1->1 throttle input to 0->1 percentVBus (speed) input
            m_throttle = OI.getInstance().getShooterJoystick().getRawAxis(RobotMap.LOGITECH_ATTACK_3_THROTTLE_AXIS);
            m_percentVBus = -0.5 * m_throttle + 0.5;
            
        	// Just in case... don't let the shooter wheel go backwards
        	if (m_percentVBus < 0) {
        		m_percentVBus = 0;
        	}

        	// Set the speed controller input
            m_motorControllerAM1.setX(m_percentVBus);
            m_motorControllerAM2.setX(m_percentVBus);
            m_motorControllerFP1.setX(m_percentVBus);
            m_motorControllerFP2.setX(m_percentVBus);
        } catch (CANTimeoutException ex) {
            System.out.println("CAN timeout setSpeed shooter.  Message = " + ex.getMessage());
            m_errorCount++;
        }
    }

    /*
     * This function is called to update the SmartBoard display
     */
    public void updateStatus() {
        try {
            SmartDashboard.putDouble("Shooter Throttle Position", m_throttle);
            SmartDashboard.putDouble("Shooter Average RPM", m_averagedMotorRPM);
            SmartDashboard.putDouble("Shooter Instant RPM", m_motorRPM[m_rpmIndex]);
            SmartDashboard.putDouble("Shooter PercentVBus", m_percentVBus);
            SmartDashboard.putDouble("Shooter getX", m_motorControllerAM1.getX());
            SmartDashboard.putInt("Shooter Error Count", m_errorCount);
        } 
        catch (CANTimeoutException ex) {
            System.out.println("CAN timeout updateStatus shooter.  Message = " + ex.getMessage());
            m_errorCount++;
        }
    }
}
