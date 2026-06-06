# IncrementalPlayerSave

A Fabric mod that implements Paper's incremental player saving to smooth out MSPT spikes from saving player data to disk. 


Inspired by Paper's incremental player saving optimization:

https://github.com/PaperMC/Paper/blob/76d2ac758cb3abe75aceefa88207443768f585c6/paper-server/patches/features/0020-Incremental-chunk-and-player-saving.patch

## Configuration Options
### ticksBetweenSaveAttempts
How often the mod should go through all of the players to check if they have been saved yet and then to save them
### ticksBetweenSavesPerPlayer
How many ticks between each save for each player. I.e. don't save the player more often than ``ticksBetweenSavesPerPlayer`` ticks.
### maxPlayersSavedPerAttempt 
How many players are saved per tick, max. Capping this prevents saving players from spiking the MSPT too high.
### enableDebugLog
If the mod should log its actions.
