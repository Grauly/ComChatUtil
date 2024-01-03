package grauly.comchatutil.config;

import grauly.comchatutil.event.ComChatEventListener;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Config(name = "comchatutil")
public class ComChatConfig implements ConfigData {
    @ConfigEntry.ColorPicker
    public int color = 42752;
    @ConfigEntry.Gui.Tooltip
    public List<String> togglePhrases = Arrays.asList("gc toggle","groupchat toggle");
    @ConfigEntry.Gui.Tooltip
    public List<String> escapedPhrases = Arrays.asList("[wW][bB].?", "[wW]elcome.?");
    @ConfigEntry.Gui.Tooltip
    public List<String> aliases = List.of("cc");

}
