package org.me.gcu.equakestartercode;

import android.util.Log;

import java.util.Dictionary;

public class MagnitudeColourCoding
{
    public static float minRange = 0f;
    public static float maxRange = 4f;

    private static String[] colours = new String[] {
            "#fef5ff", "#fffe97", "#e7aa40", "#c31c1c", "#78050d", "#56018a"
    };

    private static float[] times = new float[] {
            0f, 0.2f, 0.4f, 0.6f, 0.8f, 1f
    };

    public static String getColour(float magnitude) {
        Log.d("Colours", "Mag: " + magnitude);
        float magNormalised = (magnitude - minRange) / (maxRange - minRange);
        for (int i = times.length - 1; i >= 0; i--) {
            if (magNormalised >= times[i]) {
                int[] rgb1 = hexToRgb(colours[i]);
                Log.d("Colours", "rgb1: " + rgb1[0] + ", " + rgb1[1] + ", " + rgb1[2]);
                int[] rgb2 = hexToRgb(colours[i + 1]);
                Log.d("Colours", "rgb2: " + rgb2[0] + ", " + rgb2[1] + ", " + rgb2[2]);
                int[] lerpedColour = lerpColour(rgb1[0], rgb1[1], rgb1[2], rgb2[0], rgb2[1], rgb2[2], (magNormalised - times[i]) / (times[i + 1] - times[i]));
                Log.d("Colours", rgbToHex(lerpedColour[0], lerpedColour[1], lerpedColour[2]));
                return rgbToHex(lerpedColour[0], lerpedColour[1], lerpedColour[2]);
            }
        }
        Log.e("Colours", "Could not retrieve colour for the magnitude of " + magnitude);
        return "#ffffff";
    }

    public static String rgbToHex(int r, int g, int b) {
        Log.d("Colours", r + ", " + g + ", " + b);
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            Log.e("Colours", "Invalid RGB colour. RGB values must be in the range 0-255.");
            return null;
        }
        return "#" + Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
    }

    public static int[] hexToRgb(String hex) {
        if (hex.charAt(0) != '#') {
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

    private static int[] lerpColour(int r1, int g1, int b1, int r2, int g2, int b2, float t) {
        return new int[] {
                Math.round(r1 + t * (r2 - r1)),
                Math.round(g1 + t * (g2 - g1)),
                Math.round(b1 + t * (b2 - b1))
        };
    }
}
