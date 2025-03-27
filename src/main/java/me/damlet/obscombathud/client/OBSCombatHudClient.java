package me.damlet.obscombathud.client;

import me.damlet.obscombathud.config.CombatHudConfig;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OBSCombatHudClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("obs-armor-hud");
    public static final CombatHudConfig CONFIG = new CombatHudConfig();

    @Override
    public void onInitializeClient() {
        LOGGER.info("OBS Combat Hud Loaded!");
        CONFIG.load();
    }

}
