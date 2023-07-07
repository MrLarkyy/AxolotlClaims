package net.crashcraft.crashclaim.wand;

import net.crashcraft.crashclaim.CrashClaim;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ClaimWandHandler {

    private static final NamespacedKey DATA_KEY = new NamespacedKey(CrashClaim.getPlugin(),"CLAIM_WAND_IDENTIFIER");
    private final Config config = new Config(CrashClaim.getPlugin(),"claimwand.yml");

    private final ItemStack claimWand;

    public ClaimWandHandler() {
        config.load();
        //CrashClaim.getPlugin().getServer().getPluginManager().registerEvents(new ClaimWandListener(this),CrashClaim.getPlugin());
        this.claimWand = setupWand();
    }

    public boolean isClaimWand(ItemStack itemStack) {
        if (itemStack == null) return false;
        var im =itemStack.getItemMeta();
        if (im == null) return false;
        var pdc= im.getPersistentDataContainer();
        return pdc.has(DATA_KEY, PersistentDataType.INTEGER);
    }

    private ItemStack setupWand() {
        var cfg = config.getConfiguration();

        Material m = Material.valueOf(cfg.getString("material").toUpperCase());
        ItemStack is = new ItemStack(m);
        ItemMeta im = is.getItemMeta();
        if (cfg.contains("display-name")) {
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&',cfg.getString("display-name")));
        }
        if (cfg.contains("lore")) {
            List<String> newLore = new ArrayList<>();
            var list = cfg.getStringList("lore");
            for (String s : list) {
                newLore.add(ChatColor.translateAlternateColorCodes('&',s));
            }
        }
        if (cfg.contains("model-data")) {
            im.setCustomModelData(cfg.getInt("model-data"));
        }

        im.getPersistentDataContainer().set(DATA_KEY,PersistentDataType.INTEGER,1);

        is.setItemMeta(im);
        return is;
    }

    public void interact(PlayerInteractEvent e) {
        CrashClaim.getPlugin().getCommandManager().getClaimCommand().onClick(e);
    }

    public ItemStack getClaimWand() {
        return claimWand;
    }
}
