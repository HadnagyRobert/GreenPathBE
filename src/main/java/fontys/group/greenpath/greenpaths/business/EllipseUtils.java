package fontys.group.greenpath.greenpaths.business;

import fontys.group.greenpath.greenpaths.domain.Ellipse;
import com.google.maps.model.LatLng;
import fontys.group.greenpath.greenpaths.domain.Graph;


public interface EllipseUtils {
    Ellipse calculateEllipseFromTwoPoints(LatLng origin, LatLng destination);
    boolean isPointInsideEllipse(LatLng point, Ellipse ellipse);
    double calculateDistance(LatLng startPoint, LatLng endPoint);
    Graph divideEllipse(Ellipse ellipse, int sectors);
}
