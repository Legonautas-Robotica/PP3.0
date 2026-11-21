package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static MecanumConfig driveConfig = new MecanumConfig(
    c -> {
        c.frontLeftName.set("DiE");
        c.frontRightName.set("DiD");
        c.backLeftName.set("TE");
        c.backRightName.set("TD");
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.manualBrakeMode.set(true);
    }
    );

    static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(-6.29);
        c.yPodOffset.set(-3.1496);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, driveConfig),
                new Foresight(foresightConfig)
        );
    }

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.7606318787073847);
                Controller secondaryTranslationalForward = Controller.proportional(0.2810331547242587);
                Controller primaryTranslationalLateral = Controller.proportional(0.3809484832362842);
                Controller secondaryTranslationalLateral = Controller.proportional(0.1407502854248632);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.016950915761734794));
                c.brake.set(Controller.proportionalFeedforward(0.014408278397474574));

                c.headingFeedback.set(Controller.proportional(1.503701096353718)); //3.903701096353718
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.054657683583436846, 0.010861264872110032));

                c.linearBrakeCoefficients.set(Matrix.diag(0.07573756136236974, 0.02937664200602938));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0012895244203632228, 0.002951297728020307));

                c.maxAchievableForwardVelocity.set(61.43173680786057);
                c.maxAchievableStrafeVelocity.set(45.6383251644925);
                c.naturalForwardDeceleration.set(31.927422317486215);
                c.naturalStrafeDeceleration.set(58.98655786093622);
            }
    );
}