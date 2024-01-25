package frc.robot.constants;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import frc.robot.utils.ModuleMap;
import frc.robot.utils.ModuleMap.MODULE_POSITION;
import java.util.Map;

public final class SWERVE {

  public static final class DRIVE {
    public static final double kTrackWidth = Units.inchesToMeters(24);
    public static final double kWheelBase = Units.inchesToMeters(24);

    public static final Map<MODULE_POSITION, Translation2d> kModuleTranslations =
        Map.of(
            MODULE_POSITION.FRONT_LEFT,
            new Translation2d(kWheelBase / 2, kTrackWidth / 2),
            MODULE_POSITION.FRONT_RIGHT,
            new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
            MODULE_POSITION.BACK_LEFT,
            new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
            MODULE_POSITION.BACK_RIGHT,
            new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

    public static final SwerveDriveKinematics kSwerveKinematics =
        new SwerveDriveKinematics(
            ModuleMap.orderedValues(kModuleTranslations, new Translation2d[0]));
    public static final double kMaxRotationRadiansPerSecond = Math.PI * 2.0;
    public static final double kMaxRotationRadiansPerSecondSquared = Math.PI * 2.0;
    public static final double kLimitedRotationRadiansPerSecond = kMaxRotationRadiansPerSecond / 5;
    public static final double kP_X = 2.5;
    public static final double kI_X = 0;
    public static final double kD_X = 0;
    public static final double kP_Y = 2.5;
    public static final double kI_Y = 0;
    public static final double kD_Y = 0;
    public static double frontLeftCANCoderOffset = 197.75376;
    public static double frontRightCANCoderOffset = 352.61712;
    public static double backLeftCANCoderOffset = 10.1952;
    public static double backRightCANCoderOffset = 211.55256;
    public static double kMaxSpeedMetersPerSecond = Units.feetToMeters(18);
    public static final double kLimitedSpeedMetersPerSecond = kMaxSpeedMetersPerSecond / 5;
    public static double kP_Theta = 5.0;
    public static double kI_Theta = 0;
    public static double kD_Theta = 0.5;
  }

  public static class MODULE {
    public static final double kDriveMotorGearRatio = 6.12;
    public static final double kTurnMotorGearRatio = 150.0 / 7.0;
    public static final double kWheelDiameterMeters = Units.inchesToMeters(4);

    public static final DCMotor kDriveGearbox = DCMotor.getFalcon500(1);
    public static final DCMotor kTurnGearbox = DCMotor.getFalcon500(1);

    public static final double kSlipCurrent = 300.0;
    public static final double kDriveInertia = 0.001;
    public static final double kTurnInertia = 0.00001;
    public static final boolean kTurnInverted = true;

    public static double kP_Theta = 8.0;
    public static double kI_Theta = 0;
    public static double kD_Theta = 0.5;

    public static final double ksDriveVoltsRotation = 0.24085;
    public static final double kvDriveVoltSecondsPerRotation = 2.4597;
    public static final double kaDriveVoltSecondsSquaredPerRotation = 0.033818;

    public static final double ksTurnVoltsRotation = (0.24085 / 12.0);
    public static final double kvTurnVoltSecondsPerRotation = (2.4597 / 12.0);
    public static final double kaTurnVoltSecondsSquaredPerRotation = (0.033818 / 12.0);
  }

  public static final double ksDriveVoltSecondsPerMeter = 0.32;
  public static final double kvDriveVoltSecondsSquaredPerMeter = 1.51;
  public static final double kaDriveVoltSecondsSquaredPerMeter = 0.27;

  public static final DCMotor kDriveGearbox = DCMotor.getFalcon500(1);
  public static final DCMotor kTurnGearbox = DCMotor.getFalcon500(1);

  public static final double kSlipCurrent = 300.0;
  public static final double kDriveInertia = 0.001;
  public static final double kTurnInertia = 0.00001;
  public static final boolean kTurnInverted = true;

  //    public static final double ksDriveVoltsRotation = 0.11286;
  //    public static final double kvDriveVoltSecondsPerRotation = 0.10079;
  //    public static final double kaDriveVoltSecondsSquaredPerRotation = 0.040151;

  //    public static final double ksDriveVoltsRotation = (0.11286 / 12.0);
  //    public static final double kvDriveVoltSecondsPerRotation = (0.10079 / 12.0);
  //    public static final double kaDriveVoltSecondsSquaredPerRotation = (0.040151 / 12.0);

  //    public static final double ksDriveVoltsRotation = (0.32 / 12);
  //    public static final double kvDriveVoltSecondsPerRotation = (1.51 / 12);
  //    public static final double kaDriveVoltSecondsSquaredPerRotation = (0.27 / 12);

}
