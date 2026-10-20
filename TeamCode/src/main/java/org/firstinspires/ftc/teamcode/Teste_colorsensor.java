package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;
public class Teste_colorsensor {
    NormalizedColorSensor colorsensor;
    boolean detecteiazul = false;
    public enum DetectedColor {
        red,
        blue,
        yellow,
        BLUE, UNKNOWN
    }
    public void init(HardwareMap hardwareMap){
        colorsensor = hardwareMap.get(NormalizedColorSensor.class, "sensordecor");
        colorsensor.setGain(8);
    }
    public DetectedColor getDetectedColor(Telemetry telemetry){
        NormalizedRGBA colors = colorsensor.getNormalizedColors();


        float normRed, normGreen, normBlue;
        normRed = colors.red / colors.alpha;
        normGreen = colors.green / colors.alpha;
        normBlue = colors.blue / colors.alpha;

        telemetry.addData("vermelho", normRed);
        telemetry.addData("verde", normGreen);
        telemetry.addData("azul", normBlue);

        if (normRed < 0.04 && normGreen <0.08 && normBlue > 0.1){
            return DetectedColor.blue;

        } else if (normRed > 0.075 && normGreen > 0.09 && normBlue < 0.9) {
            return DetectedColor.yellow;
        }else {
            return DetectedColor.UNKNOWN;
        }
        }

    }

// vermelho, verde, azul
// AZUL = <0.04, <0.08,>0.1
// AMARELO = >0.06,>0.1,<0.04