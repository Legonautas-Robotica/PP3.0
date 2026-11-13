package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.follower.Follower;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static MecanumConfig drivetrainConfig;
    public static PinpointConfig localizerConfig;

    public static Follower create(HardwareMap h) {
        MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
            c.frontLeftName.set("DiE");
            c.frontRightName.set("DiD");
            c.backLeftName.set("TE");
            c.backRightName.set("TD");
            c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
            c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
            c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
            c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
        });
        return null;
    }
    }