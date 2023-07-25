package grauly.comchatutil;

import grauly.comchatutil.config.ComChatConfig;
import grauly.comchatutil.event.ComChatEventListener;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComChatUtil implements ClientModInitializer {

    public static boolean inComChat = false;
    public static final Logger LOGGER = LoggerFactory.getLogger("comchatutil");

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ComChatConfig.class, GsonConfigSerializer::new);
        AutoConfig.getConfigHolder(ComChatConfig.class).registerSaveListener(ComChatEventListener::onConfigChange);
        //ALLOW_CHAT instead of CHAT to allow for canceling the message
        ClientSendMessageEvents.ALLOW_CHAT.register(ComChatEventListener::handleComChatEscaping);
        ClientSendMessageEvents.COMMAND.register(ComChatEventListener::handleComChatToggle);
        ClientPlayConnectionEvents.JOIN.register(ComChatEventListener::handleJoin);
    }
}
