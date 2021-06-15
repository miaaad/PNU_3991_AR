package com.neshan.task1.utils;

import org.neshan.common.model.LatLng;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

public interface LatLngInterpolator {
    public LatLng interpolate(float fraction, LatLng a, LatLng b);

    public class Linear implements LatLngInterpolator {
        @Override
        public LatLng interpolate(float fraction, LatLng a, LatLng b) {
            double lat = (b.getLatitude() - a.getLatitude()) * fraction + a.getLatitude();
            double lng = (b.getLongitude() - a.getLongitude()) * fraction + a.getLongitude();
            return new LatLng(lat, lng);
        }
    }

    public class LinearFixed implements LatLngInterpolator {
        @Override
        public LatLng interpolate(float fraction, LatLng a, LatLng b) {
            double lat = (b.getLatitude() - a.getLatitude()) * fraction + a.getLatitude();
            double lngDelta = b.getLongitude() - a.getLongitude();

            if (Math.abs(lngDelta) > 180) {
                lngDelta -= Math.signum(lngDelta) * 360;
            }
            double lng = lngDelta * fraction + a.getLongitude();
            return new LatLng(lat, lng);
        }
    }

    public class Spherical implements LatLngInterpolator {


        @Override
        public LatLng interpolate(float fraction, LatLng from, LatLng to) {

            double fromLat = toRadians(from.getLatitude());
            double fromLng = toRadians(from.getLongitude());
            double toLat = toRadians(to.getLatitude());
            double toLng = toRadians(to.getLongitude());
            double cosFromLat = cos(fromLat);
            double cosToLat = cos(toLat);

            double angle = computeAngleBetween(fromLat, fromLng, toLat, toLng);
            double sinAngle = sin(angle);
            if (sinAngle < 1E-6) {
                return from;
            }
            double a = sin((1 - fraction) * angle) / sinAngle;
            double b = sin(fraction * angle) / sinAngle;

            double x = a * cosFromLat * cos(fromLng) + b * cosToLat * cos(toLng);
            double y = a * cosFromLat * sin(fromLng) + b * cosToLat * sin(toLng);
            double z = a * sin(fromLat) + b * sin(toLat);

            double lat = atan2(z, sqrt(x * x + y * y));
            double lng = atan2(y, x);
            return new LatLng(toDegrees(lat), toDegrees(lng));
        }

        private double computeAngleBetween(double fromLat, double fromLng, double toLat, double toLng) {

            double dLat = fromLat - toLat;
            double dLng = fromLng - toLng;
            return 2 * asin(sqrt(pow(sin(dLat / 2), 2) +
                    cos(fromLat) * cos(toLat) * pow(sin(dLng / 2), 2)));
        }
    }
}