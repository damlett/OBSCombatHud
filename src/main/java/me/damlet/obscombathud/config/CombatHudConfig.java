package me.damlet.obscombathud.config;

import me.damlet.obscombathud.client.OBSCombatHudClient;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.zziger.obsoverlay.registry.OverlayComponent;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class CombatHudConfig {

    public static boolean SATURATION_HUD = true;
    public static boolean ARMOR_HUD = true;

    public static float SPACING = 18f;
    public static float OFFSET_X = 45f;
    public static float OFFSET_Y = 3f;
    public static int COLOR = 16777215;
    public static int MEDIUM_COLOR = 16777045;
    public static int LOW_COLOR = 16733525;
    public static boolean SHADOW = true;

    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("obscombathud.properties");
    public static OverlayComponent armorOverlay = null;
    public static OverlayComponent saturationOverlay = null;

    public Screen getScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("config.title"));

        builder.setSavingRunnable(CombatHudConfig::save);

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("config.title"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.saturationvisible"), CombatHudConfig.SATURATION_HUD)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> CombatHudConfig.ARMOR_HUD = newValue)
                .build());
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.armorvisible"), CombatHudConfig.ARMOR_HUD)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> CombatHudConfig.ARMOR_HUD = newValue)
                .build());
        general.addEntry(entryBuilder.startFloatField(Text.translatable("config.spacing"), CombatHudConfig.SPACING)
                .setDefaultValue(18f)
                .setSaveConsumer(newValue -> CombatHudConfig.SPACING = newValue)
                .build());
        general.addEntry(entryBuilder.startFloatField(Text.translatable("config.offset_x"), CombatHudConfig.OFFSET_X)
                .setDefaultValue(45f)
                .setSaveConsumer(newValue -> CombatHudConfig.OFFSET_X = newValue)
                .build());
        general.addEntry(entryBuilder.startFloatField(Text.translatable("config.offset_y"), CombatHudConfig.OFFSET_Y)
                .setDefaultValue(3f)
                .setSaveConsumer(newValue -> CombatHudConfig.OFFSET_Y = newValue)
                .build());
        general.addEntry(entryBuilder.startColorField(Text.translatable("config.color"), CombatHudConfig.COLOR)
                .setDefaultValue(16777215)
                .setSaveConsumer(newValue -> CombatHudConfig.COLOR = newValue)
                .build());
        general.addEntry(entryBuilder.startColorField(Text.translatable("config.medium_color"), CombatHudConfig.MEDIUM_COLOR)
                .setDefaultValue(16777045)
                .setSaveConsumer(newValue -> CombatHudConfig.MEDIUM_COLOR = newValue)
                .build());
        general.addEntry(entryBuilder.startColorField(Text.translatable("config.low_color"), CombatHudConfig.LOW_COLOR)
                .setDefaultValue(16733525)
                .setSaveConsumer(newValue -> CombatHudConfig.LOW_COLOR = newValue)
                .build());
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("config.shadow"), CombatHudConfig.SHADOW)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> CombatHudConfig.SHADOW = newValue)
                .build());

        return builder.build();
    }

    public static void write(Properties properties) {
        properties.setProperty("armor_hud", Boolean.toString(ARMOR_HUD));
        properties.setProperty("spacing", Float.toString(SPACING));
        properties.setProperty("offset_x", Float.toString(OFFSET_X));
        properties.setProperty("offset_y", Float.toString(OFFSET_Y));
        properties.setProperty("color", Integer.toString(COLOR));
        properties.setProperty("medium_color", Integer.toString(MEDIUM_COLOR));
        properties.setProperty("low_color", Integer.toString(LOW_COLOR));
        properties.setProperty("shadow", Boolean.toString(SHADOW));
    }

    public void read(Properties properties) {
        ARMOR_HUD = Boolean.parseBoolean(properties.getProperty("armor_hud"));
        SPACING = Float.parseFloat(properties.getProperty("spacing"));
        OFFSET_X = Float.parseFloat(properties.getProperty("offset_x"));
        OFFSET_Y = Float.parseFloat(properties.getProperty("offset_y"));
        COLOR = Integer.parseInt(properties.getProperty("color"));
        MEDIUM_COLOR = Integer.parseInt(properties.getProperty("medium_color"));
        LOW_COLOR = Integer.parseInt(properties.getProperty("low_color"));
        SHADOW = Boolean.parseBoolean(properties.getProperty("shadow"));
    }

    public static void save() {
        Properties properties = new Properties();
        write(properties);

        if (!Files.exists(CONFIG_PATH)) {
            try {
                Files.createFile(CONFIG_PATH);
            } catch (IOException e) {
                OBSCombatHudClient.LOGGER.error("Failed to create config file");
            }
        }

        try {
            properties.store(Files.newOutputStream(CONFIG_PATH), "OBS Combat Hud config file");
        } catch (IOException e) {
            OBSCombatHudClient.LOGGER.error("Failed to write config");
        }
    }

    public void load() {
        Properties properties = new Properties();

        if (!Files.exists(CONFIG_PATH)) {
            try {
                Files.createFile(CONFIG_PATH);
                save();
            } catch (IOException e) {
                OBSCombatHudClient.LOGGER.error("Failed to create config file");
            }
        }

        try {
            properties.load(Files.newInputStream(CONFIG_PATH));
        } catch (IOException e) {
            OBSCombatHudClient.LOGGER.error("Failed to read config");
        }

        read(properties);
    }

}
