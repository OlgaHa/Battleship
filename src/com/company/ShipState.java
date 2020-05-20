package com.company;

public enum ShipState {
    EMPTY, MARKED, SHOT, DROWN, MISSED, CHECKED_MARKED, CHECKED_SHOT;

    public static ShipState fromString(String state) {
        for (ShipState shipState : values())
            if (shipState.name().equalsIgnoreCase(state))
                return shipState;
        return null;
    }
}
