package grauly.comchatutil.event;

import grauly.comchatutil.ComChatUtil;
import grauly.comchatutil.config.ComChatConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ComChatEventListener {
    private ComChatEventListener() {
        throw new IllegalStateException("Utility class");
    }

    private static ClientPlayNetworkHandler networkHandler;
    public static boolean needsRegexRecompile = true;
    private static String fullEscapingRegex;

    public static String applyAliases(String command) {
        var config = AutoConfig.getConfigHolder(ComChatConfig.class).getConfig();
        // toggle aliases
        if (config.aliases.contains(command)) {
            return config.getTogglePhrase();
        }
        // inline aliases
        for (String alias : config.aliases) {
            if (command.startsWith(alias)) {
                return command.replaceFirst(alias, config.getInlinePhrase());
            }
        }
        return command;
    }

    public static void handleComChatToggle(String command) {
        var config = AutoConfig.getConfigHolder(ComChatConfig.class).getConfig();
        var lowerCase = command.toLowerCase();
        if (config.togglePhrases.contains(lowerCase)) {
            ComChatUtil.inComChat.set(!ComChatUtil.inComChat.get());
        }
    }

    public static boolean handleComChatEscaping(String message) {
        var config = AutoConfig.getConfigHolder(ComChatConfig.class).getConfig();
        if (needsRegexRecompile) {
            createRegexFromUserConfig();
            needsRegexRecompile = false;
        }
        if (ComChatUtil.inComChat.get() && message.matches(fullEscapingRegex)) {
            networkHandler.sendChatCommand(config.getInlinePhrase());
            try {
                Thread.sleep(50L * config.escapeDelayTicks);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            networkHandler.sendChatMessage(message);
            try {
                Thread.sleep(50L * config.escapeDelayTicks);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            networkHandler.sendChatCommand(config.getInlinePhrase());
            return false;
        }
        return true;
    }

    public static void handleJoin(ClientPlayNetworkHandler clientPlayNetworkHandler, PacketSender sender, MinecraftClient minecraftClient) {
        ComChatUtil.inComChat.set(false);
        networkHandler = clientPlayNetworkHandler;
    }

    public static void createRegexFromUserConfig() {
        StringBuilder builder = new StringBuilder();
        ArrayList<String> errorStrings = new ArrayList<>();
        ComChatConfig config = AutoConfig.getConfigHolder(ComChatConfig.class).getConfig();
        config.escapedPhrases.forEach(phrase -> {
            try {
                Pattern.compile(phrase);
                builder.append(phrase);
                builder.append("|");
            } catch (PatternSyntaxException e) {
                errorStrings.add(phrase);
            }
        });
        builder.deleteCharAt(builder.length() - 1);
        fullEscapingRegex = builder.toString();
        createErrorMessage(errorStrings);
    }

    public static void createErrorMessage(List<String> errorStrings) {
        if (errorStrings.isEmpty()) {
            return;
        }
        MutableText errorText = MutableText.of(Text.translatable("text.comchatutil.regex.error").getContent());
        errorStrings.forEach(erroringString -> {
            errorText.append(Text.literal("\n"));
            errorText.append(Text.literal(erroringString));
        });
        ComChatUtil.LOGGER.warn((errorText.toString()));
        MinecraftClient.getInstance().player.sendMessage(errorText);
    }

    public static ActionResult onConfigChange(ConfigHolder<ComChatConfig> comChatConfigConfigHolder, ComChatConfig comChatConfig) {
        ComChatUtil.LOGGER.info("Config save detected, recompiling regex");
        createRegexFromUserConfig();
        needsRegexRecompile = false;
        return ActionResult.PASS;
    }
}
