// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveDrive extends SubsystemBase {
  /*
   * Notes on offseason robot drivetrain:
   * Swerve offsets should be basically the same as Forte
   * Front side drive motors are flipped to not interfere with intake
   */

  /** Creates a new SwerveDrive. */
  public SwerveDrive() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
