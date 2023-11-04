package fontys.group.greenpath.greenpaths.business;

import com.google.maps.model.LatLng;

public interface CircumferenceUtils {
    LatLng getCircumferencePointBasedOnWalkingSpeed(Double walkingSpeed, LatLng origin, int pathTime);
}
