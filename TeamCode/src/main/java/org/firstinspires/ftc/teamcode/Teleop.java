package org.firstinspires.ftc.teamcode;

import com.pedropathing.ivy.Scheduler;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static java.lang.Math.abs;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.ArrayList;
import java.util.List;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class Teleop extends OpMode {
    DcMotorEx motorDID, motorDIE, motorTD, motorTE, lan;
    DcMotor intake, transferidor;
    IMU imu;
    float multiplicadorVelocidade = 1.0f;
    int velocidadeideal = 1100;
    String statuslancador = "";
    Servo SA81BHMW;
    private Follower follower;
    String mirar = "";

    double xRobo, yRobo, dist, distParaAlvo, headingRobo = 0;

    long timervelocidade = System.currentTimeMillis();

    List<Double> gol1 = new ArrayList<>((java.util.Arrays.asList(57.0, 54.5)));
    List<Double> gol2 = new ArrayList<>((java.util.Arrays.asList(57.0, 87.0)));

    double xGol1 = gol1.get(0);
    double yGol1 = gol1.get(1);
    double xgol2 = gol2.get(0);
    double ygol2 = gol2.get(1);
    double xA;
    double yA;
    double distanciaIdeal = 30.0;

    private final PoseFactory p = PoseFactory.degrees(); // add this
    private final Pose startPose = p.of(70.75, 70.75, 90);
    private Pose poseLan;
    boolean seguindoTrajetoria = false;

    public Path cT(Pose posicaoInicial, Pose posicaoFinal){return Paths.line(posicaoInicial, posicaoFinal).linear(posicaoFinal, posicaoInicial);}

    @Override
    public void init() {
        Scheduler.reset();
        intake = hardwareMap.get(DcMotor.class, "Intake");
        transferidor = hardwareMap.get(DcMotor.class, "transfer");
        lan = hardwareMap.get(DcMotorEx.class, "lan");
        lan.setDirection(DcMotorEx.Direction.FORWARD);

        SA81BHMW = hardwareMap.get(Servo.class, "porta");
        SA81BHMW.setPosition(0.15);

        transferidor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        transferidor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
    }

    @Override
    public void loop() {

        follower.update();

        xRobo = follower.pose().x();
        yRobo = follower.pose().y();

        telemetry.addData("velocidadeideal", velocidadeideal);
        telemetry.addData("velocidade atual", lan.getVelocity());
        telemetry.addData("status", statuslancador);
        telemetry.addData("X", "%.2f", xRobo);
        telemetry.addData("Y", "%.2f", yRobo);
        telemetry.addData("Heading atual", "%.2f", follower.pose().heading());
        telemetry.addData("Heading alvo", "%.2f", headingRobo);
        telemetry.addData("Seguindo trajetória", seguindoTrajetoria);
        telemetry.update();   // um único update no final

        if (!seguindoTrajetoria) {
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
        }

        // lan.setPower(gamepad1.right_stick_y*0.7);
        lan.setVelocity(velocidadeideal);

        if (gamepad2.right_trigger_pressed && abs(lan.getVelocity() - velocidadeideal) < 25) {
            transferidor.setPower(1.0);
        } else if (gamepad2.left_trigger_pressed) {
            transferidor.setPower(-1.0);
        } else {
            transferidor.setPower(0);
        }

        if (gamepad2.right_bumper) {
            intake.setPower(-0.7);
        } else if (gamepad2.left_bumper) {
            intake.setPower(0.7);
        } else {
            intake.setPower(0);
        }

        if (gamepad1.a) {
            SA81BHMW.setPosition(0.4);
        } else if (gamepad1.b) {
            SA81BHMW.setPosition(0.2);
        }

        if (gamepad1.dpad_up && System.currentTimeMillis() - timervelocidade > 500) {
            velocidadeideal += 25;
            timervelocidade = System.currentTimeMillis();
        } else if (gamepad1.dpad_down && System.currentTimeMillis() - timervelocidade > 500) {
            velocidadeideal -= 25;
            timervelocidade = System.currentTimeMillis();
        }

        if (abs(lan.getVelocity() - velocidadeideal) < 25) {
            statuslancador = "pode lançar";
        } else {
            statuslancador = "não lança";
        }

        if (gamepad1.left_bumper){
            if (yRobo < 45 || yRobo > 95) {

                seguindoTrajetoria = true;
                headingRobo = Math.toDegrees(follower.pose().heading());

                if (yRobo < 45) {
                    dist = Math.sqrt(Math.pow(gol1.get(0) - xRobo, 2) + (Math.pow(gol1.get(1) - yRobo, 2)));
                    xA = xRobo - ((dist - distanciaIdeal) * (xRobo - xGol1)) / dist;
                    yA = yRobo - ((dist - distanciaIdeal) * (yRobo - yGol1)) / dist;
                } else {
                    dist = Math.sqrt(Math.pow(gol2.get(0) - xRobo, 2) + (Math.pow(gol2.get(1) - yRobo, 2)));
                    xA = xRobo - ((dist - distanciaIdeal) * (xRobo - xgol2)) / dist;
                    yA = yRobo - ((dist - distanciaIdeal) * (yRobo - ygol2)) / dist;
                }

                poseLan = p.of(xA, yA, headingRobo);
                schedule(follow(follower, cT(follower.pose(), poseLan)));
            }
        }

        if (seguindoTrajetoria) {
            distParaAlvo = Math.sqrt(Math.pow(xA - xRobo, 2) + Math.pow(yA - yRobo, 2));

            if (abs(gamepad1.left_stick_y) > 0.2 || abs(gamepad1.left_stick_x) > 0.2 || distParaAlvo < 2) {
                seguindoTrajetoria = false;
                Scheduler.cancel(follow(follower, cT(follower.pose(), poseLan)));
                Scheduler.reset();
            }
        }

        Scheduler.execute();

    }
}
