// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.sim.TalonFXSimState;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.CAN;
import frc.robot.constants.CLIMBER;
import frc.robot.constants.CLIMBER.CLIMBER_SETPOINT;
import frc.robot.constants.ROBOT;
import frc.robot.constants.ROBOT.CONTROL_MODE;
import frc.robot.utils.CtreUtils;
import org.littletonrobotics.junction.Logger;

public class Climber extends SubsystemBase {
  private final TalonFX[] elevatorClimbMotors = {
    new TalonFX(CAN.climbMotor1), new TalonFX(CAN.climbMotor2)
  };

  private final Follower follower = new Follower(0, false);

  private final StatusSignal<Double> m_positionSignal =
      elevatorClimbMotors[0].getPosition().clone();
  private final StatusSignal<Double> m_velocitySignal =
      elevatorClimbMotors[0].getVelocity().clone();
  private final StatusSignal<Double> m_leftCurrentSignal =
      elevatorClimbMotors[0].getTorqueCurrent().clone();
  private final StatusSignal<Double> m_rightCurrentSignal =
      elevatorClimbMotors[1].getTorqueCurrent().clone();

  private final MotionMagicTorqueCurrentFOC m_request =
      new MotionMagicTorqueCurrentFOC(getHeightMeters());

  private double m_desiredPositionMeters; // The height in meters our robot is trying to reach
  private final double m_upperLimitMeters = CLIMBER.upperLimitMeters;
  private final double m_lowerLimitMeters = CLIMBER.lowerLimitMeters;
  private CLIMBER_SETPOINT m_desiredSetpoint = CLIMBER_SETPOINT.FULL_RETRACT;

  private CONTROL_MODE m_controlMode = CONTROL_MODE.OPEN_LOOP;
  // Controlled by open loop
  private double m_joystickInput;
  private boolean m_limitJoystickInput;
  private boolean m_userSetpoint;

  private NeutralModeValue m_neutralMode = NeutralModeValue.Brake;

  private boolean elevatorClimbSate;

  public final ElevatorSim leftElevatorSim =
      new ElevatorSim(
          CLIMBER.gearbox,
          CLIMBER.gearRatio,
          CLIMBER.carriageMassKg,
          CLIMBER.sprocketRadiusMeters,
          CLIMBER.lowerLimitMeters,
          CLIMBER.upperLimitMeters,
          false,
          CLIMBER.lowerLimitMeters);
  public final ElevatorSim rightElevatorSim =
      new ElevatorSim(
          CLIMBER.gearbox,
          CLIMBER.gearRatio,
          CLIMBER.carriageMassKg,
          CLIMBER.sprocketRadiusMeters,
          CLIMBER.lowerLimitMeters,
          CLIMBER.upperLimitMeters,
          false,
          CLIMBER.lowerLimitMeters);

  private final TalonFXSimState m_simState1 = elevatorClimbMotors[0].getSimState();
  private final TalonFXSimState m_simState2 = elevatorClimbMotors[1].getSimState();

  /** Creates a new climberMechanism. */
  public Climber() {
    // Initialize Test Values
    TalonFXConfiguration config = new TalonFXConfiguration();
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    config.Feedback.SensorToMechanismRatio = CLIMBER.gearRatio;
    config.Slot0.kP = CLIMBER.kP;
    config.Slot0.kI = CLIMBER.kI;
    config.Slot0.kD = CLIMBER.kD;
    config.Slot0.kA = CLIMBER.kA;
    config.Slot0.kV = CLIMBER.kV;

    config.MotionMagic.MotionMagicAcceleration = CLIMBER.kMaxAccel;
    config.MotionMagic.MotionMagicCruiseVelocity = CLIMBER.kMaxVel;

    CtreUtils.configureTalonFx(elevatorClimbMotors[0], config);
    CtreUtils.configureTalonFx(elevatorClimbMotors[1], config);

    elevatorClimbMotors[0].setInverted(false);
    elevatorClimbMotors[1].setControl(
        follower
            .withMasterID(elevatorClimbMotors[0].getDeviceID())
            .withOpposeMasterDirection(true));

    SmartDashboard.putData(this);
  }

  public void setClimbState(boolean state) {
    elevatorClimbSate = state;
  }

  public boolean getClimbState() {
    return elevatorClimbSate;
  }

  public double getPercentOutput() {
    return elevatorClimbMotors[0].get();
  }

  public void setPercentOutput(double output) {
    setPercentOutput(output, false);
  }

  // sets the percent output of the elevator based on its position
  public void setPercentOutput(double output, boolean enforceLimits) {
    if (enforceLimits) {
      if (getHeightMeters() >= getUpperLimitMeters() - Units.inchesToMeters(1.2))
        output = Math.min(output, 0);

      if (getHeightMeters() <= getLowerLimitMeters() + Units.inchesToMeters(0.05))
        output = Math.max(output, 0);
    }

    elevatorClimbMotors[0].set(output);
  }

  public double getAvgCurrentDraw() {
    return (m_leftCurrentSignal.getValue() + m_rightCurrentSignal.getValue()) * 0.5;
  }

  // gets the position of the climber in meters
  public double getHeightMeters() {
    return getMotorRotations() * CLIMBER.sprocketRotationsToMeters;
  }

  // gets the position of the climber in encoder counts
  public double getMotorRotations() {
    return m_positionSignal.getValue();
  }

  // sets position in meters
  public void setSensorPosition(double meters) {
    elevatorClimbMotors[0].setPosition(meters / CLIMBER.sprocketRotationsToMeters);
  }

  public double getVelocityMetersPerSecond() {
    return m_velocitySignal.getValue() * CLIMBER.sprocketRotationsToMeters;
  }

  public void holdClimber() {
    setDesiredPositionMeters(getHeightMeters());
  }

  public void setDesiredSetpoint(double desiredSetpoint) {
    setDesiredPositionMeters(m_desiredSetpoint.getSetpointMeters());
  }

  public double getDesiredSetpoint() {
    return m_desiredPositionMeters;
  }

  public void setDesiredPositionMeters(double setpoint) {
    m_desiredPositionMeters =
        MathUtil.clamp(setpoint, CLIMBER.lowerLimitMeters, CLIMBER.upperLimitMeters);
  }

  public double getDesiredPositionMeters() {
    return m_desiredPositionMeters;
  }

  // Sets the setpoint to our current height, effectively keeping the elevator in place.
  public void resetMotionMagicState() {
    m_desiredPositionMeters = getHeightMeters();
    elevatorClimbMotors[0].setControl(m_request.withPosition(m_desiredPositionMeters));
  }

  public double getLowerLimitMeters() {
    return m_lowerLimitMeters;
  }

  public double getUpperLimitMeters() {
    return m_upperLimitMeters;
  }

  public void setJoystickLimit(boolean limit) {
    m_limitJoystickInput = limit;
  }

  public void setJoystickY(double m_joystickY) {
    m_joystickInput = m_joystickY;
  }

  public boolean isUserControlled() {
    return m_joystickInput != 0 && !m_userSetpoint;
  }

  public void setUserSetpoint(boolean bool) {
    m_userSetpoint = bool;
  }

  // Sets the control state of the elevator
  public void setClosedLoopControlMode(CONTROL_MODE mode) {
    m_controlMode = mode;
  }

  // Returns the current control state enum
  public CONTROL_MODE getClosedLoopControlMode() {
    return m_controlMode;
  }

  public boolean isClosedLoopControl() {
    return getClosedLoopControlMode() == CONTROL_MODE.CLOSED_LOOP;
  }

  public void setClimberNeutralMode(NeutralModeValue mode) {
    if (mode == m_neutralMode) return;
    m_neutralMode = mode;
    elevatorClimbMotors[0].setNeutralMode(mode);
    elevatorClimbMotors[1].setNeutralMode(mode);
  }

  public NeutralModeValue getNeutralMode() {
    return m_neutralMode;
  }

  public void teleopInit() {
    resetMotionMagicState();
    setDesiredPositionMeters(getHeightMeters());
  }

  private void updateLogger() {
    Logger.recordOutput("Climber/Control Mode", getClosedLoopControlMode());
    Logger.recordOutput("Climber/Height Meters", getHeightMeters());
    Logger.recordOutput("Climber/Motor Rotations", getMotorRotations());
    Logger.recordOutput("Climber/Climb State", getClimbState());
    Logger.recordOutput("Climber/Motor Output", getPercentOutput());
    Logger.recordOutput("Climber/Setpoint", getDesiredSetpoint());
    Logger.recordOutput("Climber/Supply Current", getAvgCurrentDraw());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    switch (m_controlMode) {
      case OPEN_LOOP:
        double percentOutput = m_joystickInput * CLIMBER.kPercentOutputMultiplier;

        if (m_limitJoystickInput)
          percentOutput = m_joystickInput * CLIMBER.kLimitedPercentOutputMultiplier;

        // TODO: Verify rotation to distance conversion before continuing
        setPercentOutput(percentOutput, false);

        if (DriverStation.isDisabled()) {
          setPercentOutput(0);
        }
        break;
      default:
      case CLOSED_LOOP:
        if (DriverStation.isEnabled())
          elevatorClimbMotors[0].setControl(m_request.withPosition(m_desiredPositionMeters));
        break;
    }
    if (ROBOT.logMode.get() <= ROBOT.LOG_MODE.NORMAL.get()) updateLogger();
  }

  @Override
  public void simulationPeriodic() {
    m_simState1.setSupplyVoltage(RobotController.getBatteryVoltage());
    m_simState2.setSupplyVoltage(RobotController.getBatteryVoltage());

    leftElevatorSim.setInputVoltage(
        MathUtil.clamp(elevatorClimbMotors[0].getMotorVoltage().getValue(), -12, 12));
    rightElevatorSim.setInputVoltage(
        MathUtil.clamp(elevatorClimbMotors[1].getMotorVoltage().getValue(), -12, 12));

    leftElevatorSim.update(RobotTime.getTimeDelta());
    rightElevatorSim.update(RobotTime.getTimeDelta());

    m_simState1.setRotorVelocity(
        leftElevatorSim.getVelocityMetersPerSecond()
            * CLIMBER.gearRatio
            * CLIMBER.sprocketRotationsToMeters);
    m_simState1.setRawRotorPosition(
        leftElevatorSim.getPositionMeters()
            * CLIMBER.gearRatio
            * CLIMBER.sprocketRotationsToMeters);
    m_simState2.setRotorVelocity(
        rightElevatorSim.getVelocityMetersPerSecond()
            * CLIMBER.gearRatio
            * CLIMBER.sprocketRotationsToMeters);
    m_simState2.setRawRotorPosition(
        rightElevatorSim.getPositionMeters()
            * CLIMBER.gearRatio
            * CLIMBER.sprocketRotationsToMeters);
  }
}
