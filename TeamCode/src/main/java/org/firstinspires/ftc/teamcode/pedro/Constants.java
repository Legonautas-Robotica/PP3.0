package org.firstinspires.ftc.teamcode.pedro;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.algorithm.Foresight;
import com.pedropathing.controllers.Controller;
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
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("DiE");
        c.frontRightName.set("DiD");
        c.backLeftName.set("TE");
        c.backRightName.set("TD");
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.manualBrakeMode.set(true);
    });
    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(0.2800390851779247);
        c.yPodOffset.set(-2.443714292030635);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.16888100533341427);
                Controller secondaryTranslationalForward = Controller.proportional(0.06239701888712463);
                Controller primaryTranslationalLateral = Controller.proportional(0.4028022787341922);
                Controller secondaryTranslationalLateral = Controller.proportional(0.14882467891716986);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.016086222116962378));
                c.brake.set(Controller.proportionalFeedforward(0.01367328879941802));

                c.headingFeedback.set(Controller.proportional(3.5950921036631107));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04676066718274712, 0.012605539008385987));

                c.linearBrakeCoefficients.set(Matrix.diag(0.0679740596932057, 0.04722555043111141));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0017882010975683738, 0.0020079581344149503));

                c.maxAchievableForwardVelocity.set(65.37558214160578);
                c.maxAchievableStrafeVelocity.set(54.041243065777756);
                c.naturalForwardDeceleration.set(36.918934662321554);
                c.naturalStrafeDeceleration.set(62.49911817362299);
            }
    );
}