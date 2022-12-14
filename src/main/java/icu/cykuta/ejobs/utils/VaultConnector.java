package icu.cykuta.ejobs.utils;

import icu.cykuta.ejobs.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultConnector {
    public static Economy setupEconomy() {
        Server server = Main.getPlugin().getServer();
        if (server.getPluginManager().getPlugin("Vault") == null) {
            return null;
        }

        RegisteredServiceProvider<Economy> rsp = server.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return null;
        }

        return rsp.getProvider();
    }
}
