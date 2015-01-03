
package edu.rhhs.frc.main;

import edu.rhhs.frc.buttons.DigitalIOSwitch;
import edu.rhhs.frc.commands.*;
import edu.rhhs.frc.vision.Target;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // Another type of button you can create is a DigitalIOButton, which is
    // a button or switch hooked up to the cypress module. These are useful if
    // you want to build a customized operator interface.
    // Button button = new DigitalIOButton(1);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
 
    private static OI instance = null;
    private Joystick m_stickDriver;
    private Joystick m_stickShooter;
   
    private OI() {
        // Driver stick
        m_stickDriver = new Joystick(RobotMap.DRIVER_JOYSTICK_USB_PORT);

        JoystickButton fineControlTrigger = new JoystickButton(m_stickDriver, RobotMap.FINE_DRIVE_CONTROL_BUTTON);
        fineControlTrigger.whenPressed(new FineDriveControlEnable());
        fineControlTrigger.whenReleased(new FineDriveControlDisable());
 
        JoystickButton conveyorStartTrigger = new JoystickButton(m_stickDriver, RobotMap.CONVEYOR_START_BUTTON);
        conveyorStartTrigger.whenPressed(new ConveyorStart());
                                    
        JoystickButton conveyorStopTrigger = new JoystickButton(m_stickDriver, RobotMap.CONVEYOR_STOP_BUTTON);
        conveyorStopTrigger.whenPressed(new ConveyorStop());

        // Shooter stick
        m_stickShooter = new Joystick(RobotMap.SHOOTER_JOYSTICK_USB_PORT);
        
        JoystickButton turretRightTrigger = new JoystickButton(m_stickShooter, RobotMap.TURRET_RIGHT_BUTTON);
        turretRightTrigger.whileHeld(new TurretRotate(0.5));
        turretRightTrigger.whenReleased(new TurretStop());

        JoystickButton turretLeftTrigger = new JoystickButton(m_stickShooter, RobotMap.TURRET_LEFT_BUTTON);
        turretLeftTrigger.whileHeld(new TurretRotate(-0.5));
        turretLeftTrigger.whenReleased(new TurretStop());
    
        JoystickButton cameraTrigger = new JoystickButton(m_stickShooter, RobotMap.CAMERA_SAVE_IMAGE_BUTTON);
        cameraTrigger.whenPressed(new WriteImage());

        JoystickButton elevatorStartTrigger = new JoystickButton(m_stickShooter, RobotMap.ELEVATOR_START_BUTTON);
        elevatorStartTrigger.whenPressed(new ElevatorStart(1.0));
        
        JoystickButton elevatorStopTrigger = new JoystickButton(m_stickShooter, RobotMap.ELEVATOR_STOP_BUTTON);
        elevatorStopTrigger.whenPressed(new ElevatorStop());
        
        JoystickButton turretTrimRightTrigger = new JoystickButton(m_stickShooter, RobotMap.TURRET_TRIM_RIGHT_BUTTON);
        turretTrimRightTrigger.whenPressed(new CameraAngleTrim(0.5));
        
        JoystickButton turretTrimLeftTrigger = new JoystickButton(m_stickShooter, RobotMap.TURRET_TRIM_LEFT_BUTTON);
        turretTrimLeftTrigger.whenPressed(new CameraAngleTrim(-0.5));
                
        JoystickButton shootTrimDistancePosTrigger = new JoystickButton(m_stickShooter, RobotMap.SHOOT_DISTANCE_POS_BUTTON);
        shootTrimDistancePosTrigger.whenPressed(new CameraDistanceTrim(0.5));
                
        JoystickButton shootTrimDistanceNegTrigger = new JoystickButton(m_stickShooter, RobotMap.SHOOT_DISTANCE_NEG_BUTTON);
        shootTrimDistanceNegTrigger.whenPressed(new CameraDistanceTrim(-0.5));
                
        JoystickButton shootManualTrigger = new JoystickButton(m_stickShooter, RobotMap.SHOOT_MANUAL_BUTTON);;
        shootManualTrigger.whenPressed(new ElevatorRunUntilSwitch(1.0));
                
        // Hardware switches
//        DigitalIOSwitch elevatorStartSwitch = new DigitalIOSwitch(RobotMap.ELEVATOR_LOWER_SWITCH_DIO_PORT);
//        elevatorStartSwitch.whenPressed(new ElevatorSetRelativeAngle(3));

        DigitalIOSwitch elevatorStartSwitch = new DigitalIOSwitch(RobotMap.ELEVATOR_LOWER_SWITCH_DIO_PORT);
        elevatorStartSwitch.whenPressed(new ElevatorTimed(2, 1.0));
        
        // SmartDashboard buttons
        InternalButton cameraLight = new InternalButton();
        cameraLight.whenReleased(new CameraLightToggle());
        SmartDashboard.putData("Camera Light Toggle", cameraLight);

        InternalButton setLeftTarget = new InternalButton();
        setLeftTarget.whenReleased(new CameraTargetSet(Target.LEFT));
        SmartDashboard.putData("Left", setLeftTarget);
 
        InternalButton setRightTarget = new InternalButton();
        setRightTarget.whenReleased(new CameraTargetSet(Target.RIGHT));
        SmartDashboard.putData("Right", setRightTarget);

        InternalButton setTopTarget = new InternalButton();
        setTopTarget.whenReleased(new CameraTargetSet(Target.TOP));
        SmartDashboard.putData("Top", setTopTarget);

        InternalButton setBottomTarget = new InternalButton();
        setBottomTarget.whenReleased(new CameraTargetSet(Target.BOTTOM));
        SmartDashboard.putData("Bottom", setBottomTarget);
   }

    public Joystick getDriverJoystick() {
        return m_stickDriver;
    }
    
    public Joystick getShooterJoystick() {
        return m_stickShooter;
    }
    
    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }
}

