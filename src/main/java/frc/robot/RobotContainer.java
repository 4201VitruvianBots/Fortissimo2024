// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.constants.USB;
import frc.robot.constants.SWERVE.DRIVE;
import frc.robot.constants.*; 
import frc.robot.commands.characterization.SwerveDriveDynamic;
import frc.robot.commands.characterization.SwerveDriveQuasistatic;
import frc.robot.commands.characterization.SwerveTurnDynamic;
import frc.robot.commands.characterization.SwerveTurnQuasistatic;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;

// import frc.robot.commands.autos.*;

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
  private final CommandPS4Controller testController = new CommandPS4Controller(USB.testController);

  private final CommandSwerveDrivetrain m_swerveDrive =
  new CommandSwerveDrivetrain(
      SWERVE.DrivetrainConstants,
      SWERVE.FrontLeftConstants,
      SWERVE.FrontRightConstants,
      SWERVE.BackLeftConstants,
      SWERVE.BackRightConstants);
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    initAutoChooser();
    configureBindings();
    m_vision.registerSwerveDrive(m_swerveDrive);
    m_controls.registerDriveTrain(m_swerveDrive);
    m_swerveDrive.registerTelemetry(m_telemetry::telemeterize);
    m_swerveDrive.registerVisionSubsystem(m_vision);

  }

  private void initalizeSubsystems() {
    if (RobotBase.isReal()) {
      m_swerveDrive.setDefaultCommand(
          m_swerveDrive.applyChassisSpeeds(
              () ->
                  new ChassisSpeeds(
                      leftJoystick.getRawAxis(1) * DRIVE.kMaxSpeedMetersPerSecond,
                      leftJoystick.getRawAxis(0) * DRIVE.kMaxSpeedMetersPerSecond,
                      rightJoystick.getRawAxis(0) * DRIVE.kMaxRotationRadiansPerSecond)));
    } else {
      m_swerveDrive.setDefaultCommand(
          m_swerveDrive.applyChassisSpeeds(
              () ->
                  new ChassisSpeeds(
                      -m_testController.getRawAxis(0) * DRIVE.kMaxSpeedMetersPerSecond,
                      m_testController.getRawAxis(1) * DRIVE.kMaxSpeedMetersPerSecond,
                      -m_testController.getRawAxis(2) * DRIVE.kMaxRotationRadiansPerSecond)));

      m_testController
          .cross()
          .whileTrue(new SetTrackingState(m_swerveDrive, TRACKING_STATE.SPEAKER));
    }
  }
  // Use this method to define your trigger->command mappings.
  private void configureBindings() {}

  private void initAutoChooser() {
    // m_autoChooser.addDefaultOption("Do Nothing", new WaitCommand(0));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return null;
  }
  public void disabledInit(){
    m_swerveDrive.applyRequest(SwerveRequest.ApplyChassisSpeeds::new);
    m_swerveDrive.setTrackingState(TRACKING_STATE.NONE);
  }
}
