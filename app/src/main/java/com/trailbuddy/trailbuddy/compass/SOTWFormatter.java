/*
MIT License

Copyright (c) 2017 Viacheslav Iutin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.trailbuddy.trailbuddy.compass;

import android.content.Context;

import com.trailbuddy.trailbuddy.R;

public class SOTWFormatter {
    private static final int[] sides = {0, 45, 90, 135, 180, 225, 270, 315, 360};
    private static String[] names = null;

    public SOTWFormatter(Context context) {
        initLocalizedNames(context);
    }

    public String format(float azimuth) {
        int iAzimuth = (int) azimuth;
        int index = findClosestIndex(iAzimuth);
        return iAzimuth + "° " + names[index];
    }

    private void initLocalizedNames(Context context) {
        // it will be localized version of
        // {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"}
        // yes, N is twice, for 0 and for 360

        if (names == null) {
            names = new String[]{
                    context.getString(R.string.sotw_north),
                    context.getString(R.string.sotw_northeast),
                    context.getString(R.string.sotw_east),
                    context.getString(R.string.sotw_southeast),
                    context.getString(R.string.sotw_south),
                    context.getString(R.string.sotw_southwest),
                    context.getString(R.string.sotw_west),
                    context.getString(R.string.sotw_northwest),
                    context.getString(R.string.sotw_north)
            };
        }
    }

    /**
     * Finds index of the closest element to identify Side Of The World label
     *
     * @param target
     * @return index of the closest element
     */
    private static int findClosestIndex(int target) {
        // in the original binary search https://www.geeksforgeeks.org/find-closest-number-array/
        // you will see more steps to reduce the time
        // in in this particular case the corner conditions are never true
        // e.g. azimuth is never negative, so there is no point to check
        // these conditions. Also we don't check if target is equal to element of array,
        // because most of the time it's not.

        // and the main difference is it finds the index, not the value

        // Doing binary search
        int i = 0, j = sides.length, mid = 0;
        while (i < j) {
            mid = (i + j) / 2;

            /* If target is less than array element,
               then search in left */
            if (target < sides[mid]) {

                // If target is greater than previous
                // to mid, return closest of two
                if (mid > 0 && target > sides[mid - 1]) {
                    return getClosest(mid - 1, mid, target);
                }

                /* Repeat for left half */
                j = mid;
            } else {
                if (mid < sides.length - 1 && target < sides[mid + 1]) {
                    return getClosest(mid, mid + 1, target);
                }
                i = mid + 1; // update i
            }
        }

        // Only single element left after search
        return mid;
    }

    // Method to compare which one is the more close
    // We find the closest by taking the difference
    // between the target and both values. It assumes
    // that val2 is greater than val1 and target lies
    // between these two.
    private static int getClosest(int index1, int index2, int target) {
        if (target - sides[index1] >= sides[index2] - target) {
            return index2;
        }
        return index1;
    }
}
