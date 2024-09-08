package me.rejomy.levelsystem.listener;

import me.rejomy.levelsystem.manager.DataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {
    private final DataManager dataManager;

    public ConnectionListener(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        dataManager.add(
                dataManager.create(player, player.getName()));
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        dataManager.remove(event.getPlayer());
    }
}
