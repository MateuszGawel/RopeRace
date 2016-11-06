package com.apptogo.roperace.tools;

import com.badlogic.gdx.math.Vector2;

public class UnitConverter {

    public static final float PPM = 64;

    /** @param graphicsUnit
     * units for graphics representation. Usually pixels
     * @return box2d world units. Usually meters */
    public static float toBox2dUnits(float graphicsUnit) {
        return graphicsUnit / PPM;
    }
    
    /** @param graphicsUnit
     * units for graphics representation. Usually pixels
     * @return box2d world units. Usually meters */
    public static Vector2 toBox2dUnits(Vector2 graphicsUnitVector) {
        return new Vector2(graphicsUnitVector.x / PPM, graphicsUnitVector.y / PPM);
    }

    /** @param box2dUnit
     * box2d world units. Usually meters
     * @return units for graphics representation. Usually pixels */
    public static float toGraphicsUnits(float box2dUnit) {
        return box2dUnit * PPM;
    }
    
    /** @param box2dUnit
     * box2d world units. Usually meters
     * @return units for graphics representation. Usually pixels */
    public static Vector2 toGraphicsUnits(Vector2 box2dUnitVector) {
        return new Vector2(box2dUnitVector.x * PPM, box2dUnitVector.y * PPM);
    }

}
