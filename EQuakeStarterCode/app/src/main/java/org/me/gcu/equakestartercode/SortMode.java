package org.me.gcu.equakestartercode;

public enum SortMode {
    MAGNITUDE_ASCENDING, MAGNITUDE_DESCENDING, DATE_ASCENDING, DATE_DESCENDING, ALPHABETICAL_ASCENDING, ALPHABETICAL_DESCENDING, DEPTH_ASCENDING, DEPTH_DESCENDING;

    public static SortMode getSortMode(int position) {
        switch (position) {
            case 1:
                return SortMode.DATE_ASCENDING;
            case 2:
                return SortMode.MAGNITUDE_DESCENDING;
            case 3:
                return SortMode.MAGNITUDE_ASCENDING;
            case 4:
                return SortMode.ALPHABETICAL_DESCENDING;
            case 5:
                return SortMode.ALPHABETICAL_ASCENDING;
            case 6:
                return SortMode.DEPTH_DESCENDING;
            case 7:
                return SortMode.DEPTH_ASCENDING;
            case 0:
            default:
                return SortMode.DATE_DESCENDING;
        }
    }
}