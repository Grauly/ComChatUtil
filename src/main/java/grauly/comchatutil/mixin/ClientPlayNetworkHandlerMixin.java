package grauly.comchatutil.mixin;

import grauly.comchatutil.ComChatUtil;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow public abstract void sendChatCommand(String command);

    @Shadow public abstract void sendChatMessage(String content);

    @Inject(at = @At("HEAD"), method = "sendChatCommand")
    public void changeStateOnCommand(String command, CallbackInfo ci) {
        if(command.equalsIgnoreCase("cc") || command.equalsIgnoreCase("communitychat")) {
            ComChatUtil.inComChat = !ComChatUtil.inComChat;
        }
    }

    @Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
    public void escapeGreetings(String content, CallbackInfo ci) {
        if(ComChatUtil.inComChat && content.matches("[wW][bB].?|[wW]elcome.?")) {
            sendChatCommand("cc");
            try {
                Thread.sleep(1000/20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sendChatMessage(content);
            try {
                Thread.sleep(1000/20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sendChatCommand("cc");
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onGameJoin")
    public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        ComChatUtil.inComChat = false;
    }
}
