// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc100.BALLista.commands;

import edu.wpi.first.wpilibj.PIDController;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc100.BALLista.Robot;
import org.usfirst.frc100.BALLista.RobotMap;

public class TurnToAngle extends Command {
	boolean cancelPID;
	public TurnToAngle() {

		requires(Robot.driveTrain);

	}

	public TurnToAngle(int angles) {
		if(angles > 300){
			cancelPID = true;
		}else{
			cancelPID = false;
			Robot.driveTrain.setDistances(angles);
		}
		
		requires(Robot.driveTrain);

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		// Get everything in a safe starting state.
		// Robot.driveTrain.pid.setPID(Robot.prefs.getDouble("pValue", .04),
		// Robot.prefs.getDouble("iValue", .00), Robot.prefs.getDouble("dValue",
		// .00), 0);
		Robot.driveTrain.pid.setAbsoluteTolerance(0.3);
		Robot.driveTrain.pid
				.setSetpoint((Robot.driveTrain.getAngles() + Robot.driveTrain
						.getDistances())); // Robot.driveTrain.getAngles+1
		//Robot.driveTrain.pid.reset();
		Robot.driveTrain.pid.enable();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if(Robot.driveTrain.pid.onTarget() || cancelPID == true) return true;
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		// Stop PID and the wheels
		Robot.driveTrain.pid.disable();
		Robot.driveTrain.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}

}
