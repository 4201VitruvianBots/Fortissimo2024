// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.CAN;
import frc.robot.constants.INTAKE;

public class Intake extends SubsystemBase {
  /*
   * Notes on the offseason robot intake:
   * Intake has single Kraken X60 motor
   * Under bumper intake
   * Intake side = fronthbbbbbbbbbbbbbbbbbbbbbbbbhbh
   */
  /** Creates a new Intake. */
  private TalonFX intakeMotor = new TalonFX(CAN.intakeMotor);
  
  public Intake() {
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.Slot0.kP =INTAKE.kP;
    config.Slot0.kI =INTAKE.kI;
    config.Slot0.kI =INTAKE.kD;
    config.Feedback.SensorToMechanismRatio = INTAKE.gearRatio;
    intakeMotor.getConfigurator().apply(config);
   }
public void setSpeed
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
