// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc100.RandomTest.commands;
import edu.wpi.first.wpilibj.command.Command;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.usfirst.frc100.RandomTest.Robot;
import org.usfirst.frc100.RandomTest.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;

/**
 *
 */
public class MotionVel extends Command {

	private double P; 
	private double I; 
	private double D;
	private double F;
	private double PL; 
	private double IL; 
	private double DL;
	private double FL;
	private double time;
	private boolean finish; 
	private int counter; 
	FalconPathPlanner path;
	Timer timer; 
	
    public MotionVel() {
    	requires(Robot.driveTrain);

  
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	finish = false;
    	counter = 0;
    	P = Robot.prefs.getDouble("P",
				0);
    	I = Robot.prefs.getDouble("I",
				0);
    	D = Robot.prefs.getDouble("D",
				0);
    	F = Robot.prefs.getDouble("F",
				0);
    	PL = Robot.prefs.getDouble("PL",
				0);
    	IL = Robot.prefs.getDouble("IL",
				0);
    	DL = Robot.prefs.getDouble("DL",
				0);
    	FL = Robot.prefs.getDouble("FL",
				0);
    	time = Robot.prefs.getDouble("time", 0);
    	RobotMap.driveTrainTalonSRX1.config_kF(0, F, 10); //.123
    	RobotMap.driveTrainTalonSRX1.config_kP(0, P, 10); //.2
        RobotMap.driveTrainTalonSRX1.config_kI(0, I, 10);
    	RobotMap.driveTrainTalonSRX1.config_kD(0, D, 10);
    
    	RobotMap.driveTrainTalonSRX2.config_kF(0, FL, 10); //.34 //.22
    	RobotMap.driveTrainTalonSRX2.config_kP(0, PL, 10); //.189
    	RobotMap.driveTrainTalonSRX2.config_kI(0, IL, 10); //2.0E-4
    	RobotMap.driveTrainTalonSRX2.config_kD(0, DL, 10); //0
    	
    	timer = new Timer();
    	
    	double[][] waypoints = new double[][]{
    		//{1, 0},
    		//{1, 10.5}
    		{0, 1.5},
			{3, 2},
			{8, 4.5},
			{10.5,4.5},
    		
    	}; 

    	double totalTime = time; //max seconds we want to drive the path
    	double timeStep = .1; //period of control loop on Rio, seconds
    	double robotTrackWidth = 2.33; //distance between left and right wheels, feet

        path = new FalconPathPlanner(waypoints);
    	path.calculate(totalTime, timeStep, robotTrackWidth);
    	
    	final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
	    executorService.scheduleAtFixedRate(new Runnable() {
	        @Override
	        public void run() {
	//        	parseArray();
	        }
	    }, 0, 100, TimeUnit.MILLISECONDS);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	 
	     
    	SmartDashboard.putNumber("P", P);
    	SmartDashboard.putNumber("time", time);
    	SmartDashboard.putNumber("I", I);
    	SmartDashboard.putNumber("D", D);
    	SmartDashboard.putNumber("F", F);
    	SmartDashboard.putNumber("PL", PL);
    	SmartDashboard.putNumber("IL", IL);
    	SmartDashboard.putNumber("DL", DL);
    	SmartDashboard.putNumber("FL", FL);
    	
    	SmartDashboard.putNumber("L", path.smoothRightVelocity.length);
    	SmartDashboard.putNumber("LL", path.smoothRightVelocity[1].length);
    	
    	if(counter < path.smoothRightVelocity.length){
    		SmartDashboard.putNumber("actual setpoint", ((path.smoothRightVelocity[counter][1]/1.04667)/10)*8192);
    		SmartDashboard.putNumber("actual setpointL", ((path.smoothLeftVelocity[counter][1]/1.04667)/10)*8192);
    		RobotMap.driveTrainTalonSRX1.set(ControlMode.Velocity, -(((path.smoothRightVelocity[counter][1])/1.04667)/10)*8192);
    		RobotMap.driveTrainTalonSRX2.set(ControlMode.Velocity, (((path.smoothLeftVelocity[counter][1])/1.04667)/10)*8192);
    		
    	} else {
    		finish = true; 
    	}

    	counter++; 
    	try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    public void parseArray(){
    	if(counter < path.smoothRightVelocity.length){
    		SmartDashboard.putNumber("actual setpoint", ((path.smoothRightVelocity[counter][1]/1.04667)/10)*8192);
    		RobotMap.driveTrainTalonSRX1.set(ControlMode.Velocity, -((path.smoothRightVelocity[counter][1]/1.04667)/10)*8192);
    		RobotMap.driveTrainTalonSRX2.set(ControlMode.Velocity, ((path.smoothLeftVelocity[counter][1]/1.04667)/10)*8192);
    		
    	} else {
    		finish = true; 
    	}

    	counter++;
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return finish;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	Robot.driveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
