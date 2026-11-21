package org.firstinspires.ftc.teamcode.pedro.procedures;
import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

public class Robo {
    DcMotorEx motorDID, motorDIE, motorTD, motorTE, lan;
    DcMotor intake, transferidor;
    IMU imu;
    float multiplicadorVelocidade = 1.0f;
    int velocidadeideal = 1100;
    String statuslancador = "";
    Servo SA81BHMW;
    long timervelocidade = System.currentTimeMillis();
    public Robo(HardwareMap h){
        intake = h.get(DcMotor.class, "Intake");
        transferidor = h.get(DcMotor.class,"transfer");
        lan = h.get(DcMotorEx.class, "lan");
        lan.setDirection(DcMotorEx.Direction.REVERSE);

        SA81BHMW = h.get(Servo.class, "porta");
        SA81BHMW.setPosition(0.15);

        transferidor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        transferidor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void coletar(boolean True) {
        if (True) {
            intake.setPower(-0.7);
            transferidor.setPower(-0.7);
        } else {
            intake.setPower(0);
            transferidor.setPower(0);
        }
    }

    public void lançar() {
        if (abs(lan.getVelocity() - velocidadeideal) < 25) {
            SA81BHMW.setPosition(0.4);
            intake.setPower(-0.7);
            transferidor.setPower(-0.7);
        }
        else {
            SA81BHMW.setPosition(0.2);
            intake.setPower(0);
            transferidor.setPower(0);
        }
    }
}
