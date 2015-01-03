package edu.rhhs.frc.main;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    
    // USB ports
    public static final int DRIVER_JOYSTICK_USB_PORT = 1;
    public static final int SHOOTER_JOYSTICK_USB_PORT = 2;
    public static final int LOGITECH_ATTACK_3_THROTTLE_AXIS = 3;
    
    // Driver Joystick Buttons
    public static final int FINE_DRIVE_CONTROL_BUTTON = 1;
    public static final int CONVEYOR_START_BUTTON = 6;
    public static final int CONVEYOR_STOP_BUTTON = 7;
    
    // Shooter Joystick Buttons
    public static final int SHOOT_AUTO_BUTTON = 1;
    public static final int SHOOT_MANUAL_BUTTON = 2;
    public static final int TURRET_RIGHT_BUTTON = 5;
    public static final int TURRET_LEFT_BUTTON = 4;
    public static final int TURRET_TRIM_RIGHT_BUTTON = 8;
    public static final int TURRET_TRIM_LEFT_BUTTON = 9;
    public static final int SHOOT_DISTANCE_POS_BUTTON = 11;
    public static final int SHOOT_DISTANCE_NEG_BUTTON = 10;
    public static final int ELEVATOR_START_BUTTON = 6;
    public static final int ELEVATOR_STOP_BUTTON = 7;
    public static final int CAMERA_SAVE_IMAGE_BUTTON = 3;
    
    // Digital Side Car PWM ports
    public static final int CLAW_MOTOR_PWM_PORT = 6;
    
    // CAN Id
    public static final int DRIVE_LEFT_FRONT_CAN_ID = 2;
    public static final int DRIVE_LEFT_REAR_CAN_ID = 3;
    public static final int DRIVE_RIGHT_FRONT_CAN_ID = 4;
    public static final int DRIVE_RIGHT_REAR_CAN_ID = 5;
    public static final int TURRET_MOTOR_CAN_ID = 8;
    public static final int ELEVATOR_MOTOR_CAN_ID = 8;
    public static final int CLAW_MOTOR_CAN_ID = 8;
    public static final int SHOOTER_MOTOR_AM_1_CAN_ID = 10;
    public static final int SHOOTER_MOTOR_AM_2_CAN_ID = 11;
    public static final int SHOOTER_MOTOR_FP_1_CAN_ID = 6;
    public static final int SHOOTER_MOTOR_FP_2_CAN_ID = 12;
   
    // Digital Side Car Relay ports
    public static final int CONVEYOR_RELAY_PORT = 1;
    public static final int ELEVATOR_RELAY_PORT = 2;
    public static final int CAMERA_LIGHT_RELAY_PORT = 3;
 
    // Digital IO ports
    public static final int ELEVATOR_LOWER_SWITCH_DIO_PORT = 1;
    public static final int ELEVATOR_UPPER_SWITCH_DIO_PORT = 2;
    public static final int SHOOTER_ENCODER_A_DIO_PORT = 3;     
    public static final int SHOOTER_ENCODER_B_DIO_PORT = 4;    
    public static final int TURRET_ENCODER_A_DIO_PORT = 7;    
    public static final int TURRET_ENCODER_B_DIO_PORT = 8;     
    
    // Analog Ports
    public static final int CLAW_POTENTIOMETER_ANALOG_PORT = 2; // Software PID
    public static final int ULTRASONIC_ANALOG_PORT = 2;
    
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static final int rangefinderPort = 1;
    // public static final int rangefinderModule = 1;
}
