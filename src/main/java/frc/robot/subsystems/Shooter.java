// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.CAN;
import frc.robot.constants.FLYWHEEL;
import frc.robot.utils.CtreUtils;
import org.littletonrobotics.junction.Logger;
import frc.robot.constants.FLYWHEEL;

public class Shooter extends SubsystemBase {
  private final TalonFX flywheelmotor1 = new TalonFX(CAN.flywheel1);
  private final TalonFX flywheelmotor2 = new TalonFX(CAN.flywheel2);
  private double m_rpm;
  private double m_headingOffset;
  private double flywheelRPMRatio = 1.0;

  private final TalonFX[] m_shooterMotors = {
    new TalonFX(CAN.flywheel1), new TalonFX(CAN.flywheel2)
  };

  private double m_percentOutput;
  private double flywheelPercentRatio = 1.0;
  private final DutyCycleOut m_dutyCycleRequest = new DutyCycleOut(0);
  private final VelocityVoltage m_velocityRequest = new VelocityVoltage(0);

  private final SimpleMotorFeedforward m_feedForward =
      new SimpleMotorFeedforward(FLYWHEEL.kS, FLYWHEEL.kV, FLYWHEEL.kA);
  private SimpleMotorFeedforward m_currentFeedForward = m_feedForward;

  // private final ConfigFactoryDefault configSelectedFeedbackSensor = new Config
  /* Creates a new Intake. */
  public Shooter() {
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.Slot0.kV = FLYWHEEL.kV;
    config.Slot0.kP = FLYWHEEL.kP;
    config.Slot0.kI = FLYWHEEL.kI;
    config.Slot0.kD = FLYWHEEL.kD;
    CtreUtils.configureTalonFx(m_shooterMotors[0], config);

    // flywheel motor 1
    m_shooterMotors[1].setControl(new Follower(m_shooterMotors[0].getDeviceID(), true));
  }

  // values that we set
  public void setPercentOutput(double percentOutput) {
    m_shooterMotors[0].setControl(m_dutyCycleRequest.withOutput(percentOutput));
  }

  public double ShootNStrafeAngle(double RobotPoseX, double RobotPoseY, double RobotVelocityX, double RobotVelocityY) {
    return Math.atan2(
    (FLYWHEEL.NoteVelocity * Math.sin(this.shootangle(RobotPoseX, RobotPoseY)) - RobotVelocityY),
    (FLYWHEEL.NoteVelocity * Math.cos(this.shootangle(RobotPoseX, RobotPoseY)) - RobotVelocityX));
  }

  public double shootangle(Double RobotPoseX, double RobotPoseY) {
    if (Controls.IsBlueAllaince())
    {
    return (
        Math.atan2((FLYWHEEL.SPEAKER.BlueSpeakerTLY - RobotPoseY), (FLYWHEEL.SPEAKER.BlueSpeakerTLX - RobotPoseX)) 
      + Math.atan2((FLYWHEEL.SPEAKER.BlueSpeakerTRY - RobotPoseY), (FLYWHEEL.SPEAKER.BlueSpeakerTRX - RobotPoseX))
      + Math.atan2((FLYWHEEL.SPEAKER.BlueSpeakerBLY - RobotPoseY), (FLYWHEEL.SPEAKER.BlueSpeakerBLX - RobotPoseX))
      + Math.atan2((FLYWHEEL.SPEAKER.BlueSpeakerBRY - RobotPoseY), (FLYWHEEL.SPEAKER.BlueSpeakerBRX - RobotPoseX)))/4;
    }else
    {
    return (
        Math.atan2((FLYWHEEL.SPEAKER.RedSpeakerTLY - RobotPoseY), (FLYWHEEL.SPEAKER.RedSpeakerTLX - RobotPoseX)) 
      + Math.atan2((FLYWHEEL.SPEAKER.RedSpeakerTRY - RobotPoseY), (FLYWHEEL.SPEAKER.RedSpeakerTRX - RobotPoseX))
      + Math.atan2((FLYWHEEL.SPEAKER.RedSpeakerBLY - RobotPoseY), (FLYWHEEL.SPEAKER.RedSpeakerBLX - RobotPoseX))
      + Math.atan2((FLYWHEEL.SPEAKER.RedSpeakerBRY - RobotPoseY), (FLYWHEEL.SPEAKER.RedSpeakerBRX - RobotPoseX)))/4;
    }
  }

  // values that we are pulling
  public double getRPM1() {
    return m_rpm;
  }

  public void setPidValues(double v, double p, double i, double d) {
    TalonFXConfiguration config = new TalonFXConfiguration();

    // Get the current motor configs to not erase everything
    m_shooterMotors[0].getConfigurator().refresh(config);
    config.Slot0.kV = v;
    config.Slot0.kP = p;
    config.Slot0.kI = i;
    config.Slot0.kD = d;
    CtreUtils.configureTalonFx(m_shooterMotors[0], config);
  }

  public void setSimpleMotorFeedForward(double s, double v, double a) {
    m_currentFeedForward = new SimpleMotorFeedforward(s, v, a);
  }

  // values that we are pulling
  public double getPercentOutput() {
    return m_percentOutput;
  }

  private void updateLogger() {
    Logger.recordOutput("Flywheel/PercentOutput", getPercentOutput());
  }

  private void updateShuffleboard() {}

  @Override
  public void periodic() {
    updateShuffleboard();
    updateLogger();
  }
}
