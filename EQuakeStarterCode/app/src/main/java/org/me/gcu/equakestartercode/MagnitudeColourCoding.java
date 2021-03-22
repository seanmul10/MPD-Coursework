package org.me.gcu.equakestartercode;

import android.util.Log;

import java.util.Dictionary;

public class MagnitudeColourCoding
{
    public static float minRange = 0f;
    public static float maxRange = 4f;
/*
    public static String[] colours = new String[] {

    }
*/
    public static String getColour(float magnitude) {
        return "#000000";
    }

    public static String rgbToHex(int r, int g, int b) {
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            Log.e("Colours", "Invalid RGB colour. RGB values must be in the range 0-255.");
            return null;
        }
        return "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
    }

    public static int[] hexToRgb(String hex) {
        if (hex.substring(0, 1) != "#") {
            Log.e("Colours", "Invalid Hex colour. Hex code must start with a \"#\".");
        }
        else if (hex.length() != 7) {
            Log.e("Colours", "Invalid Hex colour. Hex code must be in the form #rrggbb.");
        }
        else {
            return new int[] {
                    Integer.valueOf(hex.substring(1, 3), 16),
                    Integer.valueOf(hex.substring(3, 5), 16),
                    Integer.valueOf(hex.substring(5, 7), 16)
            };
        }
        return null;
    }

    private static int[] LerpColour(int r1, int g1, int b1, int r2, int g2, int b2, float t) {
        return new int[] {
                Math.round(r1 + t * (r2 - r1)),
                Math.round(g1 + t * (g2 - g1)),
                Math.round(b1 + t * (b2 - b1))
        };
    }
}
