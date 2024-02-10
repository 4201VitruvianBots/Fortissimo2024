package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.CAN;
import frc.robot.constants.ROBOT;
import org.littletonrobotics.junction.Logger;

public class AmpShooter extends SubsystemBase {
  private final CANSparkMax ampMotor = new CANSparkMax(CAN.ampShooter, MotorType.kBrushless);
  private final SparkPIDController pidController = ampMotor.getPIDController();
  private final RelativeEncoder encoder = ampMotor.getEncoder();

  public AmpShooter() {
    ampMotor.restoreFactoryDefaults();
    encoder.setVelocityConversionFactor(0);
    pidController.setFeedbackDevice(encoder);
    pidController.setP(0.6);
    pidController.setI(0);
    pidController.setD(0);
    pidController.setFF(0);
    pidController.setOutputRange(0, 0);
  }

  public void setPercentOutput(double m_speed) {
    pidController.setReference(m_speed, CANSparkMax.ControlType.kVelocity);
  }

  public double getVelocity() {
    return encoder.getVelocity();
  }

  public double getPercentOutput() {
    return ampMotor.get();
  }

  private void updateShuffleboard() {
    SmartDashboard.putNumber("ampShooterPercent", this.getPercentOutput());
  }

  private void updateLogger() {
    Logger.recordOutput("AmpShooter/Velocity", getVelocity());
  }

  @Override
  public void periodic() {
    updateShuffleboard();
    if (!ROBOT.disableLogging) updateLogger();
  }
}
