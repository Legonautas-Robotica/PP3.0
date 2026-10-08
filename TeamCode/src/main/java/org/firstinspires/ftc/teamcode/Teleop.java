package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@TeleOp
public class Teleop extends OpMode {
    DcMotorEx motorDID, motorDIE, motorTD, motorTE,lan;
    DcMotor intake;
    IMU imu;
    float multiplicadorVelocidade = 1.0f;
    int velocidadeideal = 1400;
    String statuslancador = "";
    @Override
    public void init() {
        intake = hardwareMap.get(DcMotor.class, "Intake");
        lan = hardwareMap.get(DcMotorEx.class, "lan");
        lan.setDirection(DcMotorEx.Direction.REVERSE);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorDID = hardwareMap.get(DcMotorEx.class, "DiD");
        motorDIE = hardwareMap.get(DcMotorEx.class, "DiE");
        motorTD = hardwareMap.get(DcMotorEx.class, "TD");
        motorTE = hardwareMap.get(DcMotorEx.class, "TE");

        motorDID.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorDIE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorTD.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorTE.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorDID.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorDIE.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorTD.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorTE.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        motorDID.setDirection(DcMotorSimple.Direction.REVERSE);
        motorTD.setDirection(DcMotorSimple.Direction.REVERSE);

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot orientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT);
        imu.initialize(new IMU.Parameters(orientation));
    }

    @Override
    public void loop() {

        double y = -gamepad1.left_stick_y; //Remember, Y stick value is reversed
        double x = gamepad1.left_stick_x; //Tá andando errado para os lados, tentei só multiplicar por -1 mas não resolveu
        double rx = (gamepad1.right_trigger - gamepad1.left_trigger);

        if (gamepad1.y) {
            imu.resetYaw();
        }


        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);


        // Counteract imperfect strafing
        rotX = rotX * 1.3;

        double denominator = Math.max(abs(rotY) + abs(rotX) + abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        motorTE.setPower(backLeftPower * multiplicadorVelocidade);
        motorDIE.setPower(frontLeftPower * multiplicadorVelocidade);
        motorTD.setPower(backRightPower * multiplicadorVelocidade);
        motorDID.setPower(frontRightPower * multiplicadorVelocidade);

       // lan.setPower(gamepad1.right_stick_y*0.7);
        lan.setVelocity(velocidadeideal);

        if (gamepad1.right_bumper){
            intake.setPower(1.0);
        } else if (gamepad1.left_bumper){
            intake.setPower(-1.0);
        }else {
            intake.setPower(0);
        }

        if (gamepad1.dpad_up){
            velocidadeideal = 1400;
        } else if (gamepad1.dpad_down) {
            velocidadeideal = 1200;
        } else {
            velocidadeideal = 0;
        }

        if (abs(lan.getVelocity()-velocidadeideal)<50){
            statuslancador = "pode lançar";
        }
        else {
            statuslancador= "não lança";
        }

        telemetry.addData("velocidadeideal", velocidadeideal);
        telemetry.addData("velocidade atual", lan.getVelocity());
        telemetry.update();
    }
}
