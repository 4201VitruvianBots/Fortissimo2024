// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.constants;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

public final class VISION {
  public static final double aprilTagLimelightCamerasHeight = Units.inchesToMeters(35);
  public static final double aprilTagLimelightCamerasYaw = 54; // Just the angle offset from center of robot bar to center of camera
  public static final double aprilTagLimelightCamerasPitch = 0;
    public static final double aprilTagLimelightCamerasRoll = 0;
  public static final double aprilTagLimelightCamerasXDistanceFromCenter = Units.inchesToMeters(35); // TODO
  public static final double aprilTagLimelightCamerasYDistanceFromCenter = Units.inchesToMeters(35); // TODO

  public static final AprilTagFieldLayout aprilTagFieldLayout =
      AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();

  // Camera offset from robot center in meters. Will need to update this based on which camera is on right and which is on left (add negative values)
  public static final Transform3d robotToAprilTagLimelightCameraA =
      new Transform3d(new Translation3d(aprilTagLimelightCamerasXDistanceFromCenter, aprilTagLimelightCamerasYDistanceFromCenter, aprilTagLimelightCamerasHeight), new Rotation3d(0, 0, 0));

  // Camera offset from robot center in meters. Will need to update this based on which camera is on right and which is on left (add negative values)
  public static final Transform3d robotToAprilTagLimelightCameraB =
      new Transform3d(new Translation3d(aprilTagLimelightCamerasXDistanceFromCenter, aprilTagLimelightCamerasYDistanceFromCenter, aprilTagLimelightCamerasHeight), new Rotation3d(0, 0, 0));

  public enum CAMERA_TYPE {
    LIMELIGHT,
    PHOTONVISION
  }

  public enum PIPELINE {
    APRILTAGS(0);
    private final int pipeline;

    PIPELINE(final int pipeline) {
      this.pipeline = pipeline;
    }

    public int get() {
      return pipeline;
    }
  }

  public enum CAMERA_SERVER {
    FRONT("10.42.1.11");
    private final String ip;

    CAMERA_SERVER(final String ip) {
      this.ip = ip;
    }

    @Override
    public String toString() {
      return ip;
    }
  }
}
