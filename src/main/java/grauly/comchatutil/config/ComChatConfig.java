package grauly.comchatutil.config;

import grauly.comchatutil.event.ComChatEventListener;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.Arrays;
import java.util.List;


@Config(name = "comchatutil")
public class ComChatConfig implements ConfigData {
    @ConfigEntry.ColorPicker
    public int color = 42752;

    @ConfigEntry.Gui.Tooltip
    public List<String> escapedPhrases = Arrays.asList("[wW][bB].?", "[wW]elcome.?");
}
