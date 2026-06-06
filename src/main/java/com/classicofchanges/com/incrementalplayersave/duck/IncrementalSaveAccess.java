package com.classicofchanges.com.incrementalplayersave.duck;

public interface IncrementalSaveAccess {
    boolean incrementalplayersave$getFullySavedLastRound();
    void incrementalplayersave$saveAllIncremental(int interval, int maxPlayersPerTick);
}
