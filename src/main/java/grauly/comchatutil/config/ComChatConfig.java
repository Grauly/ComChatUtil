package grauly.comchatutil.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;


@Config(name = "comchatutil")
public class ComChatConfig implements ConfigData {
    @ConfigEntry.ColorPicker
    public int color = 42752;
}
