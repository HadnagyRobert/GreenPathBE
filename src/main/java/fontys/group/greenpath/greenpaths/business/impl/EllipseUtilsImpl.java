package fontys.group.greenpath.greenpaths.business.impl;

import fontys.group.greenpath.greenpaths.business.EllipseUtils;
import fontys.group.greenpath.greenpaths.domain.Ellipse;
import com.google.maps.model.LatLng;
import fontys.group.greenpath.greenpaths.domain.Graph;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
@AllArgsConstructor
public class EllipseUtilsImpl implements EllipseUtils {
    private final static double SEMI_MINOR_TO_MAJOR_RATIO = 0.35;

    @Override
    public Ellipse calculateEllipseFromTwoPoints(LatLng origin, LatLng destination) {
        HashMap<String, Double> ellipseCenter = calculateEllipseCenter(origin.lat, origin.lng, destination.lat, destination.lng);
        double semiMajorAxis = calculateSemiMajorAxis(origin.lat, origin.lng, ellipseCenter.get("x"), ellipseCenter.get("y"));
        double rotationAngle = calculateRotationAngle(origin.lat, origin.lng, destination.lat, destination.lng);
        double semiMinorAxis = calculateSemiMinorAxis(semiMajorAxis);

        return Ellipse.builder()
                .centerX(ellipseCenter.get("x"))
                .centerY(ellipseCenter.get("y"))
                .semiMajorAxis(semiMajorAxis)
                .semiMinorAxis(semiMinorAxis)
                .rotationAngle(rotationAngle)
                .build();
    }

    @Override
    public boolean isPointInsideEllipse(LatLng point, Ellipse ellipse) {
        double xPrime = (point.lat - ellipse.getCenterX()) * Math.cos(ellipse.getRotationAngle()) + (point.lng - ellipse.getCenterY()) * Math.sin(ellipse.getRotationAngle());
        double yPrime = -(point.lat - ellipse.getCenterX()) * Math.sin(ellipse.getRotationAngle()) + (point.lng - ellipse.getCenterY()) * Math.cos(ellipse.getRotationAngle());

        return (Math.pow(xPrime, 2) / Math.pow(ellipse.getSemiMajorAxis(), 2)) + (Math.pow(yPrime, 2) / Math.pow(ellipse.getSemiMinorAxis(), 2)) <= 1;
    }
    @Override
    public double calculateDistance(LatLng startPoint, LatLng endPoint) {
        final int R = 6371; // Earth radius in km

        // Convert latitude and longitude from degrees to radians
        double startLatRad = degreeToRadian(startPoint.lat);
        double startLngRad = degreeToRadian(startPoint.lng);
        double endLatRad = degreeToRadian(endPoint.lat);
        double endLngRad = degreeToRadian(endPoint.lng);

        // Calculate the differences between latitudes and longitudes
        double deltaLat = endLatRad - startLatRad;
        double deltaLng = endLngRad - startLngRad;

        // Haversine formula
        double a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.cos(startLatRad) * Math.cos(endLatRad) * Math.pow(Math.sin(deltaLng / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }


    @Override
    public Graph divideEllipse(Ellipse ellipse, int sectors) {
        List<LatLng> points = new ArrayList<>();
        for (int i = 0; i < sectors; i++) {
            double angle = 2.0 * Math.PI * i / sectors + Math.toRadians(ellipse.getRotationAngle());
            double midPointLat = ellipse.getCenterX() + 0.5 * ellipse.getSemiMajorAxis() * Math.cos(angle);
            double midPointLng = ellipse.getCenterY() + 0.5 * ellipse.getSemiMinorAxis() * Math.sin(angle);
            points.add(new LatLng(midPointLat, midPointLng));
        }

        // Create layers with specified points
        List<LatLng> layer1 = Arrays.asList(points.get(1), points.get(0), points.get(7));
        List<LatLng> layer2 = Arrays.asList(points.get(2), new LatLng(ellipse.getCenterX(), ellipse.getCenterY()),points.get(6));
        List<LatLng> layer3 = Arrays.asList(points.get(3), points.get(4), points.get(5));

        // Create a graph and add layers
        Graph graph = new Graph();
        graph.addLayer(layer1);
        graph.addLayer(layer2);
        graph.addLayer(layer3);

        return graph;
    }

    private double degreeToRadian(double degree) {
        return degree * (Math.PI / 180);
    }

    private HashMap<String, Double> calculateEllipseCenter(double startLat, double startLng, double endLat, double endLng) {
        double x = (startLat + endLat) / 2;
        double y = (startLng + endLng) / 2;
        HashMap<String, Double> result = new HashMap<>();
        result.put("x", x);
        result.put("y", y);
        return result;
    }

    private double calculateSemiMajorAxis(double startLat, double startLng, double h, double k) {
        return Math.sqrt(Math.pow(startLat - h, 2) + Math.pow(startLng - k, 2));
    }

    private double calculateSemiMinorAxis(double semiMajorAxis)
    {
        return semiMajorAxis * SEMI_MINOR_TO_MAJOR_RATIO;
    }

    private double calculateRotationAngle(double startLat, double startLng, double endLat, double endLng) {
        return Math.atan2(endLat - startLat, endLng - startLng);
    }
}
