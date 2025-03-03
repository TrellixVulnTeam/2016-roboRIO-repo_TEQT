// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc100.SehtTestProject.subsystems;

import org.usfirst.frc100.SehtTestProject.RobotMap;
import org.usfirst.frc100.SehtTestProject.commands.*;
//import org.usfirst.frc100.SethRobot.commands.Driveeeeeeeeeee;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

import edu.wpi.first.wpilibj.command.Subsystem;


/**
 *
 */
public class DriveTrain extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final CANTalon leftMaster = RobotMap.driveTrainLeftMaster;
    private final CANTalon leftFollower = RobotMap.driveTrainLeftFollower;
    private final CANTalon rightMaster = RobotMap.driveTrainRightMaster;
    private final CANTalon rightFollower = RobotMap.driveTrainRightFollower;
    private final RobotDrive driveyThingy = RobotMap.driveTrainDriveyThingy;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS


    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

    	setDefaultCommand(new Driveeeeeeeeeee());

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
    
    public void TankDrive(Joystick joy){
    	driveyThingy.tankDrive(joy.getRawAxis(1), joy.getRawAxis(3));
    }
    
    public void Move(double x, double y){
    	driveyThingy.tankDrive(x, y);
    }
}

