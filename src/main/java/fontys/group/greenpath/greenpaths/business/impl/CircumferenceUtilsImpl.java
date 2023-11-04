package fontys.group.greenpath.greenpaths.business.impl;

import com.google.maps.model.LatLng;
import fontys.group.greenpath.greenpaths.business.CircumferenceUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class CircumferenceUtilsImpl implements CircumferenceUtils{
    private final static double EARTH_RADIUS_IN_M = 6371e3; // radius of the earth in meters
    private final Random random = new Random();
    @Override
    public LatLng getCircumferencePointBasedOnWalkingSpeed(Double walkingSpeed, LatLng origin, int pathTime) {
        double originLat = Math.toRadians(origin.lat); // Convert latitude to radians.
        double originLon = Math.toRadians(origin.lng); // Convert longitude to radians.

        // Calculate the radius of the circle.
        double pathTimeInSeconds = pathTime * 60; // Time in seconds.
        double walkDistance = walkingSpeed * pathTimeInSeconds; // Walk distance in meters.

        // Generate a random angle.
        double theta = 2 * Math.PI * random.nextDouble();

        // Convert walk distance to a change in degrees.
        double deltaSigma = walkDistance / EARTH_RADIUS_IN_M;

        // Calculate the coordinates of the random point on the circumference.
        double randomPointLat = Math.asin(Math.sin(originLat) * Math.cos(deltaSigma) + Math.cos(originLat) * Math.sin(deltaSigma) * Math.cos(theta));
        double randomPointLng = originLon + Math.atan2(Math.sin(theta) * Math.sin(deltaSigma) * Math.cos(originLat), Math.cos(deltaSigma) - Math.sin(originLat) * Math.sin(randomPointLat));

        // Convert back to degrees.
        randomPointLat = Math.toDegrees(randomPointLat);
        randomPointLng = Math.toDegrees(randomPointLng);

        return new LatLng(randomPointLat, randomPointLng);
    }


}
