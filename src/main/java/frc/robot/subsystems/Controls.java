package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.ROBOT;
import org.littletonrobotics.junction.Logger;

@SuppressWarnings("RedundantThrows")
public class Controls extends SubsystemBase implements AutoCloseable {
  private boolean m_initState;
  private static DriverStation.Alliance m_allianceColor = DriverStation.Alliance.Red;

  public Controls() {
    m_initState = false;

    if (!ROBOT.disableLogging)
      Logger.recordOutput("Controls/Robot Serial Number", RobotController.getSerialNumber());
  }

  /**
   * Returns the robot's current alliance color
   *
   * @return Returns the current alliance color.
   */
  public static DriverStation.Alliance getAllianceColor() {
    return m_allianceColor;
  }

  public static boolean IsRedAlliance() {
    return (m_allianceColor == DriverStation.Alliance.Red);
  }

  public static boolean IsBlueAllaince() {
    return (m_allianceColor == DriverStation.Alliance.Blue);
  }

  public void setPDHChannel(boolean on) {
    // pdh.setSwitchableChannel(on);
  }

  public boolean getInitState() {
    return m_initState;
  }

  /**
   * Periodically check the DriverStation to get the Alliance color. This mainly runs when the robot
   * is disabled to avoid a bug where the robot tries to get the alliance color before it is
   * connected to a driver station.
   */
  private void updateAllianceColor() {
    var checkDsAlliance = DriverStation.getAlliance();

    checkDsAlliance.ifPresent(alliance -> m_allianceColor = alliance);
  }

  private void updateInitState() {
    if (DriverStation.isDisabled()) {}
  }

  /** Sends values to SmartDashboard */
  private void updateLogger() {
    Logger.recordOutput("Controls/AllianceColor", getAllianceColor());
  }

  @Override
  public void periodic() {
    if (RobotBase.isSimulation() || (RobotBase.isReal() && DriverStation.isDisabled())) {
      updateAllianceColor();
    }
    updateInitState();

    // This method will be called once per scheduler run
    if (!ROBOT.disableLogging) updateLogger();
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }

  @SuppressWarnings("RedundantThrows")
  @Override
  public void close() throws Exception {}
}
