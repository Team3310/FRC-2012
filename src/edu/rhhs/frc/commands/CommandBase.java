package edu.rhhs.frc.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.rhhs.frc.main.OI;
import edu.rhhs.frc.subsystems.*;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static OI m_oi;
    public static Camera m_camera = new Camera();
    public static CameraLight m_cameraLight = new CameraLight();
    public static Chassis m_chassis = new Chassis();
    public static Conveyor m_conveyor = new Conveyor();
    public static ElevatorCAN m_elevator = new ElevatorCAN();
    public static TurretCANEncoder m_turret = new TurretCANEncoder();
    public static ClawCANProportional m_claw = new ClawCANProportional();
    public static ShooterCANThrottle m_shooter = new ShooterCANThrottle();
    
    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        m_oi = OI.getInstance();
   }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
