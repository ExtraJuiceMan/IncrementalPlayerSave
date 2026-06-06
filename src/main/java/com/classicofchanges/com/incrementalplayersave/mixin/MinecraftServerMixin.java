package com.classicofchanges.com.incrementalplayersave.mixin;

import com.classicofchanges.com.incrementalplayersave.duck.ServerSaveAccess;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements ServerSaveAccess {
    @Unique
    private boolean incrementalplayersave$isManualSave = false;

    @Override
    public boolean incrementalplayersave$isForcedSave() {
        return this.incrementalplayersave$isManualSave;
    }

    @Inject(method = "saveEverything", at = @At("HEAD"))
    private void incrementalplayersave$onSaveEverythingStart(boolean silent, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        this.incrementalplayersave$isManualSave = force || flush;
    }

    @Inject(method = "saveEverything", at = @At("RETURN"))
    private void incrementalplayersave$onSaveEverythingEnd(boolean silent, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        this.incrementalplayersave$isManualSave = false;
    }
}