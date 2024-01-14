// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.characterization;

import com.ctre.phoenix6.SignalLogger;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.subsystems.SwerveDrive;
import frc.robot.utils.SysidUtils;

public class SwerveDriveDynamic extends SequentialCommandGroup {
  /** Creates a new SwerveDriveDynamic. */
  public SwerveDriveDynamic(SwerveDrive swerveDrive, SysIdRoutine.Direction direction) {
    var routine = SysidUtils.getSwerveDriveRoutine();

    Command initCommand = new InstantCommand((swerveDrive::initDriveSysid));
    Command sysidCommand = routine.dynamic(direction);

    SwerveModuleState[] states = {
      new SwerveModuleState(),
      new SwerveModuleState(),
      new SwerveModuleState(),
      new SwerveModuleState(),
    };

    addCommands(
        new InstantCommand(() -> swerveDrive.setSwerveModuleStates(states, false)),
        new WaitCommand(1),
        initCommand,
        new WaitCommand(1),
        new InstantCommand(SignalLogger::start),
        sysidCommand
            .withTimeout(10)
            .andThen(SignalLogger::stop)
            .andThen(() -> swerveDrive.setChassisSpeed(new ChassisSpeeds())));
  }
}
