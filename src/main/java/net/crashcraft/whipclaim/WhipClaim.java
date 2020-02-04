package net.crashcraft.whipclaim;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.whip.crashutils.CrashUtils;
import dev.whip.crashutils.Payment.PaymentProcessor;
import dev.whip.crashutils.Payment.ProcessorManager;
import net.crashcraft.whipclaim.commands.*;
import net.crashcraft.whipclaim.commands.modes.ModeCommand;
import net.crashcraft.whipclaim.config.ValueConfig;
import net.crashcraft.whipclaim.data.ClaimDataManager;
import net.crashcraft.whipclaim.data.MaterialName;
import net.crashcraft.whipclaim.events.PlayerListener;
import net.crashcraft.whipclaim.events.WorldListener;
import net.crashcraft.whipclaim.permissions.BypassManager;
import net.crashcraft.whipclaim.permissions.PermissionHelper;
import net.crashcraft.whipclaim.visualize.VisualizationManager;
import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

public class WhipClaim extends JavaPlugin {
    private static WhipClaim plugin;

    private ClaimDataManager manager;
    private VisualizationManager visualizationManager;
    private ProtocolManager protocolManager;
    private CrashUtils crashUtils;

    private MaterialName materialName;

    private PaymentProcessor payment;

    /*
    crashclaim.admin.bypass

     */

    @Override
    public void onLoad() {
        plugin = this;
        protocolManager = ProtocolLibrary.getProtocolManager();

        if (getDataFolder().mkdirs()){
            getLogger().info("Created plugin directory");
        }

        saveResource("localization.yml", false);
        saveResource("lookup.yml", false);
        saveResource("config.yml", false);

        reloadConfig();
        ValueConfig.writeDefault(getConfig(), this);
        ValueConfig.loadConfig(getConfig(), this);
        saveConfig();

        saveResource("localization.yml", false);
        saveResource("lookup.yml", false);

        this.crashUtils = new CrashUtils(this);
    }

    @Override
    public void onEnable() {
        visualizationManager = new VisualizationManager(this, protocolManager);
        manager = new ClaimDataManager(this);
        materialName = new MaterialName();

        BypassManager bypassManager = new BypassManager();
        new PermissionHelper(manager, bypassManager);

        CommandManager commandManager = new CommandManager(this);

        commandManager.registerCommand(new ShowClaimsCommand(visualizationManager, manager));
        commandManager.registerCommand(new HideClaimsCommand(visualizationManager));
        commandManager.registerCommand(new ModeCommand(this, protocolManager));
        commandManager.registerCommand(new MenuCommand(manager));
        commandManager.registerCommand(new BypassCommand(bypassManager));

        Bukkit.getPluginManager().registerEvents(manager, this);

        crashUtils.setupMenuSubSystem();
        payment = new ProcessorManager(this, null).getProcessor();

        Bukkit.getPluginManager().registerEvents(new WorldListener(manager, visualizationManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(manager, visualizationManager), this);
    }

    @Override
    public void onDisable() {
        manager.saveClaimsSync();
    }

    public static WhipClaim getPlugin() {
        return plugin;
    }

    public ClaimDataManager getDataManager() {
        return manager;
    }

    public VisualizationManager getVisualizationManager() {
        return visualizationManager;
    }

    public CrashUtils getCrashUtils() {
        return crashUtils;
    }

    public PaymentProcessor getPayment() {
        return payment;
    }

    public MaterialName getMaterialName() {
        return materialName;
    }
}
