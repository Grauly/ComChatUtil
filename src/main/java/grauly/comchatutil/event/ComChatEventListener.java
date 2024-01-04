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
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ComChatEventListener {

    private static ClientPlayNetworkHandler networkHandler;
    public static boolean needsRegexRecompile = true;
    private static String fullEscapingRegex;

    public static boolean handleComChatEscaping(String message) {
        if(needsRegexRecompile) {
            createRegexFromUserConfig();
            needsRegexRecompile = false;
        }
        if (ComChatUtil.inComChat.get() && message.matches(fullEscapingRegex)) {
            networkHandler.sendChatCommand("cc");
            try {
                Thread.sleep(1000 / 20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            networkHandler.sendChatMessage(message);
            try {
                Thread.sleep(1000 / 20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            networkHandler.sendChatCommand("cc");
            return false;
        }
        return true;
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

    public static void createErrorMessage(ArrayList<String> errorStrings) {
        if(errorStrings.size() == 0) {
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

    public static void handleComChatToggle(String command) {
        //no extra variants needed, as the server does not accept any other variants
        if (command.equalsIgnoreCase("cc") || command.equalsIgnoreCase("communitychat")) {
            ComChatUtil.inComChat.set(!ComChatUtil.inComChat.get());
        }
    }

    public static void handleJoin(ClientPlayNetworkHandler clientPlayNetworkHandler, PacketSender sender, MinecraftClient minecraftClient) {
        ComChatUtil.inComChat.set(false);
        networkHandler = clientPlayNetworkHandler;
    }

    public static ActionResult onConfigChange(ConfigHolder<ComChatConfig> comChatConfigConfigHolder, ComChatConfig comChatConfig) {
        ComChatUtil.LOGGER.info("Config save detected, recompiling regex");
        createRegexFromUserConfig();
        needsRegexRecompile = false;
        return ActionResult.PASS;
    }
}
