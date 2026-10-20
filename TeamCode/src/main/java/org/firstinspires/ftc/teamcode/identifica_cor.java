package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class identifica_cor extends OpMode {
    Teste_colorsensor sensor = new Teste_colorsensor();
    Teste_colorsensor.DetectedColor detectedColor;

    @Override
    public void init() {
        sensor.init(hardwareMap);
    }

    @Override
    public void loop() {
       detectedColor =  sensor.getDetectedColor(telemetry);
       telemetry.addData("cor detectada", detectedColor);
    }

}
