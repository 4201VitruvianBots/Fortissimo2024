// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  /*
   * Notes on the offseason robot shooter:
   * Shooter gear ratio is 1:1.6 (32 on the motor/20 on the shooter)
   * Note spinning can be achieved by spinning one shooter motor faster than the other
   * OP's shooter uses 8000 rpm on one motor and 4000 rpm on the other to induce spin, we'll do the same
   * Phong basically just copied OP's robot lol
   */
  
  /** Creates a new Shooter. */
  public Shooter() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
