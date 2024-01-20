// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.CAN;
import frc.robot.utils.CtreUtils;
import org.littletonrobotics.junction.Logger;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
  private final TalonFX intakeMotor1 = new TalonFX(CAN.intakeMotor1);

  private final TalonFX intakeMotor2 = new TalonFX(CAN.intakeMotor2);

  public Intake() {
    CtreUtils.configureTalonFx(intakeMotor1, new TalonFXConfiguration());
    CtreUtils.configureTalonFx(intakeMotor2, new TalonFXConfiguration());
  }

  public void setSpeed(double speed1, double speed2) {
    intakeMotor1.set(speed1);
    intakeMotor2.set(speed2);
  }

  public void updateSmartDashboard() {}

  public void updateLog() {
    Logger.recordOutput("Intake/Motor1 Speed", intakeMotor1.getVelocity().getValue());
    Logger.recordOutput("Intake/Motor2 Speed", intakeMotor2.getVelocity().getValue());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateSmartDashboard();
    updateLog();
  }
}
