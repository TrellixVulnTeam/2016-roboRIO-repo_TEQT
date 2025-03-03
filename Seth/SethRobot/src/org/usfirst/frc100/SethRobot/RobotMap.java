// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc100.SethRobot;



import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.interfaces.Gyro;
// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static SpeedController driveTrainRight;
    public static SpeedController driveTrainLeft;
    public static DifferentialDrive driveTrainRobotDriveTrain;
    
    public static WPI_TalonSRX rightMaster;
    public static WPI_TalonSRX rightFollower;
    public static WPI_TalonSRX leftMaster;
    public static WPI_TalonSRX leftFollower;
    public static ADXRS450_Gyro gyro;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public static void init() {
    	rightMaster = new WPI_TalonSRX(5);
    	rightMaster.set(ControlMode.PercentOutput,0);
    	rightFollower = new WPI_TalonSRX(4);
    	rightFollower.set(ControlMode.Follower, 5);
    	rightFollower.setSafetyEnabled(false);
    	leftMaster = new com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX(3);
    	leftMaster.set(ControlMode.PercentOutput,2);
    	leftFollower = new WPI_TalonSRX(2);
    	leftFollower.set(ControlMode.Follower, 3);
    	leftFollower.setSafetyEnabled(false);
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        driveTrainRight = new Talon(0);
      //  LiveWindow.addActuator("DriveTrain", "Right", (Talon) rightMaster);
        
        driveTrainLeft = new Talon(1);
       // LiveWindow.addActuator("DriveTrain", "Left", (Talon) leftMaster);
        
        driveTrainRobotDriveTrain = new DifferentialDrive(rightMaster, leftMaster);
        
        driveTrainRobotDriveTrain.setSafetyEnabled(true);
        driveTrainRobotDriveTrain.setExpiration(0.1);
        driveTrainRobotDriveTrain.setMaxOutput(1.0);
        gyro = new ADXRS450_Gyro();
        gyro.calibrate();

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }
}
