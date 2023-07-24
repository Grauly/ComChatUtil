package grauly.comchatutil.event;

import grauly.comchatutil.ComChatUtil;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class ComChatEventListener {

    private static ClientPlayNetworkHandler networkHandler;

    public static boolean handleComChatEscaping(String message) {
        if (ComChatUtil.inComChat && message.matches("[wW][bB].?|[wW]elcome.?")) {
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

    public static void handleComChatToggle(String command) {
        //no extra variants needed, as the server does not accept any other variants
        if (command.equalsIgnoreCase("cc") || command.equalsIgnoreCase("communitychat")) {
            ComChatUtil.inComChat = !ComChatUtil.inComChat;
        }
    }

    public static void handleJoin(ClientPlayNetworkHandler clientPlayNetworkHandler, PacketSender sender, MinecraftClient minecraftClient) {
        ComChatUtil.inComChat = false;
        networkHandler = clientPlayNetworkHandler;
    }
}
