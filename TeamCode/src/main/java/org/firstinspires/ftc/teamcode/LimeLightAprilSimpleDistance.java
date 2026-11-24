package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.pedro.procedures.Robo;

@TeleOp
public class LimeLightAprilSimpleDistance extends OpMode {
    private Limelight3A limelight3A;
    private double distance;

    @Override
    public void init() {
        //bench.init(hardwareMap);
        limelight3A = hardwareMap.get(Limelight3A.class,"limelight");
        limelight3A.pipelineSwitch(8);
    }
    @Override
    public void start() {
        limelight3A.start();
    }

    @Override
    public void loop() {

        LLResult llResult = limelight3A.getLatestResult();
        if (llResult != null && llResult.isValid()){
            Pose3D botpose = llResult.getBotpose_MT2();
            distance = getDistanceFromTage(llResult.getTa());
            telemetry.addData("distancia",distance);
            telemetry.addData("distance", distance);
            telemetry.addData("X", llResult.getTx());
            telemetry.addData("AREA", llResult.getTa());
        }

    }

    public double getDistanceFromTage (double ta){
        double scale = 30665.95;
        double distance = (scale/ta);
        return distance;
    }
}