// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.CAN;
import frc.robot.constants.INTAKE;
import frc.robot.constants.INTAKE.INTAKE_STATE;
import frc.robot.constants.ROBOT;
import frc.robot.utils.CtreUtils;
import org.littletonrobotics.junction.Logger;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */
  private boolean m_isIntaking = false;

  private INTAKE_STATE m_state = INTAKE_STATE.NONE;

  private final TalonFX intakeMotor1 = new TalonFX(CAN.intakeMotor1);
  private final TalonFX intakeMotor2 = new TalonFX(CAN.intakeMotor2);

  public Intake() {
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.Slot0.kP = INTAKE.kP;
    config.Slot0.kI = INTAKE.kI;
    config.Slot0.kD = INTAKE.kD;
    config.Feedback.SensorToMechanismRatio = INTAKE.gearRatio;
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
    CtreUtils.configureTalonFx(intakeMotor1, config);
    CtreUtils.configureTalonFx(intakeMotor2, config);
  }

  public void setSpeed(double speed1, double speed2) {
    intakeMotor1.set(speed1);
    intakeMotor2.set(speed2);
  }

  public double getSpeed() {
    return intakeMotor1.get();
  }

  public void setIntaking(boolean isIntaking) {
    m_isIntaking = isIntaking;
  }

  public boolean isIntaking() {
    return m_isIntaking;
  }

  //   public void setIntakingState(INTAKE_STATE speed) {
  //     m_state = speed;
  //   }

  public INTAKE_STATE getIntakeState() {
    return m_state;
  }

  public void updateSmartDashboard() {}

  public void updateLogger() {
    Logger.recordOutput("Intake/Motor1 Speed", intakeMotor1.getVelocity().getValue());
    Logger.recordOutput("Intake/Motor2 Speed", intakeMotor2.getVelocity().getValue());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    updateSmartDashboard();
    if (!ROBOT.disableLogging) updateLogger();
  }
}
