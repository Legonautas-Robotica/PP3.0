package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
@TeleOp
public class LimeLightDistance extends OpMode {

    private Limelight3A limelight3A;
    private double CAMERA_HEIGHT_CM = 1.0;
    private double CAMERA_ANGLE = 0.0;
    private double GOAL_HEIGHT = 127.0;
    private double distance = 0;


    @Override
    public void init() {
        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(0);
    }

    @Override
    public void start() {
        limelight3A.start();
    }

    @Override
    public void loop() {
        LLResult llResult = limelight3A.getLatestResult();

        if (llResult != null && llResult.isValid()){
            distance = getDistance(llResult.getTy());
            telemetry.addData("distancia", distance);

        }else {
            telemetry.addData("não achou", "fould");
        }


    }

    public double getDistance(double ty){
        double angleToTarget = CAMERA_ANGLE + ty;
        double heightdifference = GOAL_HEIGHT - CAMERA_HEIGHT_CM;

        return heightdifference / Math.tan(Math.toRadians(angleToTarget));
    }
}