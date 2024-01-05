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

import java.util.concurrent.atomic.AtomicBoolean;

public class ComChatUtil implements ClientModInitializer {

    public static AtomicBoolean inComChat = new AtomicBoolean(false);
    public static final Logger LOGGER = LoggerFactory.getLogger("comchatutil");

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ComChatConfig.class, GsonConfigSerializer::new);
        AutoConfig.getConfigHolder(ComChatConfig.class).registerSaveListener(ComChatEventListener::onConfigChange);
        ClientSendMessageEvents.MODIFY_COMMAND.register(ComChatEventListener::applyAliases);
        ClientSendMessageEvents.ALLOW_CHAT.register(ComChatEventListener::handleComChatEscaping);
        ClientSendMessageEvents.COMMAND.register(ComChatEventListener::handleComChatToggle);
        ClientPlayConnectionEvents.JOIN.register(ComChatEventListener::handleJoin);
    }
}
