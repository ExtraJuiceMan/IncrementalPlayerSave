package com.classicofchanges.com.incrementalplayersave.mixin;

import com.classicofchanges.com.incrementalplayersave.IncrementalPlayerSave;
import com.classicofchanges.com.incrementalplayersave.ModConfig;
import com.classicofchanges.com.incrementalplayersave.duck.IncrementalSaveAccess;
import com.classicofchanges.com.incrementalplayersave.duck.LastSavedAccess;
import com.classicofchanges.com.incrementalplayersave.duck.ServerSaveAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin implements IncrementalSaveAccess {
    @Final
    @Shadow
    private List<ServerPlayer> players;

    @Final
    @Shadow
    private MinecraftServer server;

    @Shadow
    protected abstract void save(ServerPlayer player);

    @Unique
    private boolean incrementalplayersave$fullySavedLastRound = false;

    @Unique
    public boolean incrementalplayersave$getFullySavedLastRound() {
        return this.incrementalplayersave$fullySavedLastRound;
    }

    @Unique
    public void incrementalplayersave$saveAllIncremental(int interval, int maxPlayersPerTick) {
        long now = server.getTickCount();
        int saved = 0;

        for (ServerPlayer player : this.players) {
            LastSavedAccess access = (LastSavedAccess)player;
            if (now - access.incrementalplayersave$getTickLastSaved() >= interval) {
                this.save(player);
                if (++saved >= maxPlayersPerTick) {
                    incrementalplayersave$fullySavedLastRound = false;
                    return;
                }
            }
        }

        incrementalplayersave$fullySavedLastRound = true;
    }

    @Inject(method = "save", at = @At("HEAD"))
    private void incrementalplayersave$onSave(ServerPlayer player, CallbackInfo ci) {
        ((LastSavedAccess) player).incrementalplayersave$setTickLastSaved(this.server.getTickCount());
        if (ModConfig.getInstance().isEnableDebugLog()) {
            IncrementalPlayerSave.LOGGER.info("Saving player {} ({}) on tick {}",
                    player.getPlainTextName(), player.getStringUUID(), this.server.getTickCount());
        }
    }

    @Inject(
            method = "saveAll",
            at = @At("HEAD"),
            cancellable = true
    )
    private void incrementalplayersave$cancelVanillaSaveAll(CallbackInfo ci) {
        boolean isForcedSave = ((ServerSaveAccess) this.server).incrementalplayersave$isForcedSave();

        // Force save on server stop or /save-all
        if (isForcedSave) {
            if (ModConfig.getInstance().isEnableDebugLog()) {
                IncrementalPlayerSave.LOGGER.info("Saving all players due to a forced save");
            }

            return;
        }

        if (ModConfig.getInstance().isEnableDebugLog()) {
            IncrementalPlayerSave.LOGGER.info("Minecraft tried to auto-save player data, but incremental player save will handle player data saving instead");
        }

        ci.cancel();
    }
}