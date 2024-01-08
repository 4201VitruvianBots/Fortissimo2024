// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.autos.DriveStraight;
import frc.robot.commands.intake.SetPercentOutput;
import frc.robot.commands.swerve.SetSwerveDrive;
import frc.robot.constants.USB;
import frc.robot.simulation.FieldSim;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.RobotTime;
import frc.robot.subsystems.SwerveDrive;

public class RobotContainer {
  private final SwerveDrive m_swerveDrive = new SwerveDrive();
  private final FieldSim m_fieldSim = new FieldSim(m_swerveDrive);
  private final RobotTime m_robotTime = new RobotTime();
  private final Intake m_intake = new Intake();

  private final SendableChooser<Command> m_autoChooser = new SendableChooser<>();

  private final Joystick leftJoystick = new Joystick(USB.leftJoystick);
  private final Joystick rightJoystick = new Joystick(USB.rightJoystick);
  private final CommandXboxController xboxController =
      new CommandXboxController(USB.xBoxController);

  private final PS4Controller m_testController = new PS4Controller(USB.testController);

  public RobotContainer() {
    initializeSubsystems();
    configureBindings();
    initializeAutoChooser();
  }

  private void initializeSubsystems() {
    if (RobotBase.isReal()) {
      m_swerveDrive.setDefaultCommand(
          new SetSwerveDrive(
              m_swerveDrive,
              () -> leftJoystick.getRawAxis(1),
              () -> leftJoystick.getRawAxis(0),
              () -> rightJoystick.getRawAxis(0)));
    } else {
      m_swerveDrive.setDefaultCommand(
          new SetSwerveDrive(
              m_swerveDrive,
              () -> -m_testController.getRawAxis(1),
              () -> -m_testController.getRawAxis(0),
              () -> -m_testController.getRawAxis(2)));
    }

    m_intake.setDefaultCommand(
        new SetPercentOutput(m_intake, xboxController.getLeftY(), xboxController.getRightY()));
  }

  private void configureBindings() {}

  public void initializeAutoChooser() {
    m_autoChooser.setDefaultOption("DriveStraight", new DriveStraight(m_swerveDrive, m_fieldSim));

    SmartDashboard.putData("AutoChooser", m_autoChooser);
  }

  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return m_autoChooser.getSelected();
  }
}
