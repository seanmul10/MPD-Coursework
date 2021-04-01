package org.me.gcu.equakestartercode;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

public class MagnitudeColourCoding
{
    public static float minRange = 0f;
    public static float maxRange = 4f;

    // Colour values for the colour gradient used to colour code earthquakes by magnitude
    private static String[] coloursLight = new String[] {
            "#ffffff", "#deffd1", "#feffba", "#ffc7b2", "#ff8282"
    };

    // Colour values for the colour gradient used to colour code earthquakes by magnitude in dark mode
    // Must be the same number of values as in the coloursLight array
    private static String[] coloursDark = new String[] {
            "#56a3b5", "#3ab60a", "#bcbf00", "#cb5009", "#c80000"
    };

    // Time values for the colour gradient used to colour code earthquakes by magnitude
    // First and last entries should always be 0f and 1f and there must be the same number of time values as colour values
    private static float[] times = new float[] {
            0f, .15f, .3f, .6f, 1f
    };

    // Returns a colour by passing in a magnitude and getting the corresponding colour from a colour gradient
    // Context is required for checking if the device is in night mode
    public static String getColour(float magnitude, Context context)
    {
        String[] colours = EarthquakeActivity.isDarkMode(context) ? coloursDark : coloursLight;

        float magNormalised = (magnitude - minRange) / (maxRange - minRange);
        if (magNormalised < 0f)
            magNormalised = 0f;
        else if (magNormalised > 1f)
            magNormalised = 1f;

        for (int i = times.length - 1; i >= 0; i--) {
            if (magNormalised > times[i]) {
                int[] rgb1 = hexToRgb(colours[i]);
                //Log.d("Colours", "rgb1: " + rgb1[0] + ", " + rgb1[1] + ", " + rgb1[2]);
                int[] rgb2 = hexToRgb(colours[i + 1]);
                //Log.d("Colours", "rgb2: " + rgb2[0] + ", " + rgb2[1] + ", " + rgb2[2]);
                int[] lerpedColour = lerpColour(rgb1[0], rgb1[1], rgb1[2], rgb2[0], rgb2[1], rgb2[2], (magNormalised - times[i]) / (times[i + 1] - times[i]));
                //Log.d("Colours", rgbToHex(lerpedColour[0], lerpedColour[1], lerpedColour[2]));
                return rgbToHex(lerpedColour[0], lerpedColour[1], lerpedColour[2]);
            }
        }
        //Log.e("Colours", "Could not retrieve colour for the magnitude of " + magnitude);
        return "#ffffff";
    }

    // Returns a hex colour code in the form #rrggbb
    public static String rgbToHex(int r, int g, int b) {
        //Log.d("Colours", r + ", " + g + ", " + b);
        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            Log.e("Colours", "Invalid RGB colour. RGB values must be in the range 0-255.");
            return null;
        }
        String red = (r > 15 ? "" : "0") + Integer.toHexString(r);
        String green = (g > 15 ? "" : "0") + Integer.toHexString(g);
        String blue = (b > 15 ? "" : "0") + Integer.toHexString(b);
        return "#" + red + green + blue;
    }

    // Returns an array of integers of with the length of 3 (r, g and b) by passing a hex colour code in the form #rrggbb
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

    // Interpolates two colours and returns the result
    private static int[] lerpColour(int r1, int g1, int b1, int r2, int g2, int b2, float t) {
        return new int[] {
                Math.round(r1 + t * (r2 - r1)),
                Math.round(g1 + t * (g2 - g1)),
                Math.round(b1 + t * (b2 - b1))
        };
    }
}
