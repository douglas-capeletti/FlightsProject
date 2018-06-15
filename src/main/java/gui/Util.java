package gui;

import modelo.objetos.Geo;

public class Util {

    private static final double RAIO_TERRA = 6372800;

    public  double haversine(Geo p1, Geo p2) {
        return haversine(p1.getLongitude(), p1.getLatitude(), p2.getLongitude(), p2.getLatitude());
    }

    private double haversine(double lon1, double lat1, double lon2, double lat2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return RAIO_TERRA * c;
    }
}
