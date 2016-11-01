package com.apptogo.roperace.tools;

public class UnitConverter {

    public static final float PPM = 64;

    /** @param graphicsUnit
     * units for graphics representation. Usually pixels
     * @return box2d world units. Usually meters */
    public static float toBox2dUnits(float graphicsUnit) {
        return graphicsUnit / PPM;
    }

    /** @param box2dUnit
     * box2d world units. Usually meters
     * @return units for graphics representation. Usually pixels */
    public static float toGraphicsUnits(float box2dUnit) {
        return box2dUnit * PPM;
    }
}
