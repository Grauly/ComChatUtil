package grauly.comchatutil.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.List;

public class ComChatDefaultConfig {
    @ConfigEntry.ColorPicker
    public static final int color = 42752;
    @ConfigEntry.Gui.Tooltip
    public static final List<String> togglePhrases = List.of("gc toggle","groupchat toggle");
    @ConfigEntry.Gui.Tooltip
    public static final List<String> escapedPhrases = List.of("[wW][bB].?", "[wW]elcome.?");
    @ConfigEntry.Gui.Tooltip
    public static final List<String> inlinePhrases = List.of("gc", "groupchat");
    @ConfigEntry.Gui.Tooltip
    public static final List<String> aliases = List.of("cc","gc","groupchat","communitychat");
    @ConfigEntry.Gui.Tooltip
    public static final int escapeDelayTicks = 1;
}
