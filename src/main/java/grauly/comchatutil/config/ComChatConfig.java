package grauly.comchatutil.config;

import grauly.comchatutil.ComChatUtil;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.List;

@Config(name = "comchatutil")
public class ComChatConfig implements ConfigData {
    @ConfigEntry.ColorPicker
    public int color = ComChatDefaultConfig.COLOR;
    @ConfigEntry.Gui.Tooltip
    public List<String> togglePhrases = ComChatDefaultConfig.togglePhrases;
    @ConfigEntry.Gui.Tooltip
    public List<String> escapedPhrases = ComChatDefaultConfig.escapedPhrases;
    @ConfigEntry.Gui.Tooltip
    public List<String> inlinePhrases = ComChatDefaultConfig.inlinePhrases;
    @ConfigEntry.Gui.Tooltip
    public List<String> aliases = ComChatDefaultConfig.aliases;
    @ConfigEntry.Gui.Tooltip
    public int escapeDelayTicks = ComChatDefaultConfig.ESCAPEDELAYTICKS;

    public String getTogglePhrase() {
        if (!togglePhrases.isEmpty())
            return togglePhrases.get(0);
        ComChatUtil.LOGGER.warn("Could not load toggle phrases, using internal defaults.");
        return ComChatDefaultConfig.togglePhrases.get(0);
    }

    public String getInlinePhrase() {
        if (!togglePhrases.isEmpty())
            return inlinePhrases.get(0);
        ComChatUtil.LOGGER.warn("Could not load inline phrases, using internal defaults.");
        return ComChatDefaultConfig.inlinePhrases.get(0);
    }

    @Override
    public void validatePostLoad() throws ValidationException {
        if (escapeDelayTicks <= 0) {
            escapeDelayTicks = ComChatDefaultConfig.ESCAPEDELAYTICKS;
        }
    }
}
