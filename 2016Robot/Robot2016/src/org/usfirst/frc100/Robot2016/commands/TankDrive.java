// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc100.Robot2016.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc100.Robot2016.Robot;
import org.usfirst.frc100.Robot2016.RobotMap;

/**
 *
 */
public class TankDrive extends Command {

	boolean driveDirection = true;

    public TankDrive() {

        requires(Robot.driveTrain);

    }
    public TankDrive(boolean direction){
    	driveDirection = direction;
    	requires(Robot.driveTrain);
    }

    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    	if(driveDirection){
    	Robot.driveTrain.takeJoystickInputs(Robot.oi.getDriverController1().getX(), -Robot.oi.getDriverController2().getY());
    	}
    	else{
    	Robot.driveTrain.takeJoystickInputsReverse(Robot.oi.getDriverController1().getX(), Robot.oi.getDriverController2().getY());
    	}
    	SmartDashboard.putNumber("angle", RobotMap.internalGyro.getAngle());
    	SmartDashboard.putBoolean("value1", RobotMap.pickUpUpperLimit.get());
    	SmartDashboard.putBoolean("value2", RobotMap.pickUpLowerLimit.get());
    	//SmartDashboard.putNumber("tester", Robot.testValue);
    	//can't switch orientation after switch button 5 and 4 once. Fix it!

    	SmartDashboard.putBoolean("valuess", RobotMap.pickUpHomeLimit.get());
    	if(driveDirection){
    	Robot.driveTrain.takeJoystickInputs(Robot.oi.getDriverController1().getX(), -Robot.oi.getDriverController2().getY());
    	}else{
    	Robot.driveTrain.takeJoystickInputsReverse(Robot.oi.getDriverController1().getX(), Robot.oi.getDriverController2().getY());
    	}

    	SmartDashboard.putBoolean("orientation", driveDirection);


    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.stop();

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
