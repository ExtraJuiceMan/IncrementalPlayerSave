package com.classicofchanges.com.incrementalplayersave.mixin;

import com.classicofchanges.com.incrementalplayersave.duck.LastSavedAccess;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements LastSavedAccess {
    @Unique
    private long incrementalplayersave$lastTickSaved = 0;

    @Override
    public long incrementalplayersave$getTickLastSaved() {
        return this.incrementalplayersave$lastTickSaved;
    }

    @Override
    public void incrementalplayersave$setTickLastSaved(long tick) {
        this.incrementalplayersave$lastTickSaved = tick;
    }
}