package grauly.comchatutil;

import grauly.comchatutil.config.ComChatConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class ComChatUtil implements ClientModInitializer {

    public static boolean inComChat = false;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ComChatConfig.class, GsonConfigSerializer::new);
    }
}
