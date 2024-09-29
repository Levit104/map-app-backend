package org.study.grabli_application.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtils {
    private static final GeometryFactory FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

    public static Point createPoint(double lat, double lng) {
        return FACTORY.createPoint(new Coordinate(lat, lng));
    }
}
