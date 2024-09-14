// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.constants.USB;
//import frc.robot.commands.autos.*;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  
  
//   private final LoggedDashboardChooser<Command> m_autoChooser =
//       new LoggedDashboardChooser<>("Auto Chooser");

  // Controller setup
  private final Joystick leftJoystick = new Joystick(USB.leftJoystick);
  private final Joystick rightJoystick = new Joystick(USB.rightJoystick);
  private final CommandXboxController driverController =
      new CommandXboxController(USB.xBoxController);
  private final CommandPS4Controller testController =
      new CommandPS4Controller(USB.testController);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    initAutoChooser();
    configureBindings();
  }

  // Use this method to define your trigger->command mappings.
  private void configureBindings() {
    
  }
  
  private void initAutoChooser() {
    //m_autoChooser.addDefaultOption("Do Nothing", new WaitCommand(0));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return null;
  }
}
