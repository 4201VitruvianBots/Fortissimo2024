// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {
  /*
   * Notes on the offseason robot arm:
   * Arm gear ratio: 1:114 (32/3675 to be exact)
   * Arm to amp position: 126 degrees overall
   * Arm full range of motion: ~180 degrees
   * Arm has single Kraken X60 motor
   * Arm has mounting angle of 40.406 (40) degrees
   * Arm has encoder (CANcoder most likely)
   * We'll be doing arm climbing with hooks on top of the arm
   */
  
  /** Creates a new Arm. */
  public Arm() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
