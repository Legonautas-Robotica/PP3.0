package org.firstinspires.ftc.teamcode.pedro.procedures;
import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.pedropathing.follower.Follower;

public class Robo {
    DcMotorEx motorDID, motorDIE, motorTD, motorTE, lan;
    DcMotor intake, transferidor;
    IMU imu;
    float multiplicadorVelocidade = 1.0f;
    int velocidadeideal = 1100;
    String statuslancador = "";
    Servo SA81BHMW;
    long timervelocidade = System.currentTimeMillis();

    public Robo(HardwareMap h) {
        intake = h.get(DcMotor.class, "Intake");
        transferidor = h.get(DcMotor.class, "transfer");
        lan = h.get(DcMotorEx.class, "lan");
        lan.setDirection(DcMotorEx.Direction.REVERSE);

        SA81BHMW = h.get(Servo.class, "porta");
        SA81BHMW.setPosition(0.15);

        transferidor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        transferidor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void coletar(boolean girarIntake, boolean girarTransfer) {
        if (girarIntake) {
            intake.setPower(-1.0);
        } else {
            intake.setPower(0);
        }

        if (girarTransfer) {
            transferidor.setPower(-1.0);
        } else {
            transferidor.setPower(0);
        }
    }

    public void ligar_lanca() {
        lan.setVelocity(velocidadeideal);
    }

    public void abrir_servo() {
        SA81BHMW.setPosition(0.4);
    }

    public void fechar_servo() {
        SA81BHMW.setPosition(0.15);
    }

    public boolean pode_lancar() {
        if (Math.abs(lan.getVelocity() - velocidadeideal) <= 50) {
            return true;
        } else {
            return false;
        }
    }

    public void lancar() {
        long temporizadorLancamento = System.currentTimeMillis();
        while (System.currentTimeMillis() - temporizadorLancamento < 10000) {
            if (pode_lancar()) {
                abrir_servo();
                coletar(true, true);
            } else {
                fechar_servo();
                coletar(false, false);
            }

            /*if (sensorSentinela) {
                temporizadorLancamento = System.currentTimeMillis();

            }
             */
        }
    }

}
