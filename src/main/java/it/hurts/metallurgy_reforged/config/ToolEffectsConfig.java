package it.hurts.metallurgy_reforged.config;

import it.hurts.metallurgy_reforged.Metallurgy;
import net.minecraftforge.common.config.Config;

/*************************************************
 * Author: Davoleo
 * Date / Hour: 10/02/2019 / 17:07
 * Class: ToolEffectsConfig
 * Project: Metallurgy 4 Reforged
 * Copyright - � - Davoleo - 2019
 **************************************************/

@Config.LangKey("config.metallurgy.category.tool_effects")
@Config(modid = Metallurgy.MODID, name = "metallurgy_reforged/effects", category = "tools")
public class ToolEffectsConfig {

    //Pickaxes
    @Config.Name("Deep Iron Pickaxe Effect")
    @Config.Comment("Underwater mining is not slowed down")
    public static boolean deepIronPickaxeEffect = true;

    //Tools
    @Config.Name("Shadow Steel Tools Effect")
    @Config.Comment("The tool speed is proportional to the darkness")
    public static boolean shadowSteelToolSpeedEffect = true;

    //Swords
    @Config.Name("Desichalkos Sword Effect")
    @Config.Comment("Gives some random effect to the target")
    public static boolean desichalkosSwordEffect = true;
    @Config.Name("Ignatius Sword Effect")
    @Config.Comment("Fire aspect")
    public static boolean ignatiusSwordEffect = true;
    @Config.Name("Kalendrite Sword Effect")
    @Config.Comment("Chance to regenerate your life on hit")
    public static boolean kalendriteSwordEffect = true;
    @Config.Name("Shadow Iron Sword Effect")
    @Config.Comment("Chance to blind the target")
    public static boolean shadowIronSwordEffect = true;
    @Config.Name("Shadow Steel Sword Effect")
    @Config.Comment("Speed and Damage is proportional to the darkness")
    public static boolean shadowSteelSwordEffect = true;
    @Config.Name("Tartarite Sword Effect")
    @Config.Comment("Withers the target")
    public static boolean tartariteSwordEffect = true;
    @Config.Name("Vulcanite Sword Effect")
    @Config.Comment("Fire Aspect")
    public static boolean vulcaniteSwordEffect = true;
    @Config.Name("Vyroxeres Sword Effect")
    @Config.Comment("Poisons the target")
    public static boolean vyroxeresSwordEffect = true;
}
