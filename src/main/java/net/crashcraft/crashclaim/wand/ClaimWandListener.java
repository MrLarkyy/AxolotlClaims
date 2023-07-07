package net.crashcraft.crashclaim.wand;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClaimWandListener implements Listener {

    private final ClaimWandHandler claimWandHandler;

    public ClaimWandListener(ClaimWandHandler claimWandHandler) {
        this.claimWandHandler = claimWandHandler;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        var p = e.getPlayer();
        var item = e.getItem();

        if (!claimWandHandler.isClaimWand(item)) {
            return;
        }

        e.setCancelled(true);
        claimWandHandler.interact(e);
    }

}
