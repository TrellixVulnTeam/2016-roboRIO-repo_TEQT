package org.usfirst.frc100.LegoArmWithEncoder.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc100.LegoArmWithEncoder.Robot;

/**
 *
 */
public class ClawClose extends Command {

    public ClawClose() {
        // Use requires() here to declare subsystem dependencies
        requires (Robot.claw);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.claw.close();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
