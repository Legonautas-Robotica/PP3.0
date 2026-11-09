package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class LimeLightDistanceDetector extends OpMode {

    private Limelight3A limelight;

    private double cam_altura = 0; // = vai receber a altura da camera no robô em relação ao chão, em CM

    private double angulo_camera = 0; // = vai receber angulação da câmera

    private double altura_hive = 135.90; //No manual diz que a parte inferior da comeia até o chão de se da por este valor

    private double distance = 0;

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);

    }

    @Override
    public void start() {
        limelight.start();

    }

    @Override
    public void loop() {
        LLResult resultadoll = limelight.getLatestResult();


        if (resultadoll != null && resultadoll.isValid()) {
            distance = getDistance(resultadoll.getTy());
            telemetry.addData("distancia", distance);
        }
        else {
            telemetry.addData("Alvo invalido", "achei");
        }
    }

    public double getDistance(double ty){
        double anguloAlvo = angulo_camera + ty;
        double diferençaHive = altura_hive - cam_altura;

        return diferençaHive / Math.tan(Math.toRadians(anguloAlvo));

    }
}
