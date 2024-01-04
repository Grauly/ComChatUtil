package grauly.comchatutil.mixin;

import grauly.comchatutil.ComChatUtil;
import grauly.comchatutil.config.ComChatConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow public abstract void sendChatCommand(String command);

    @Shadow public abstract void sendChatMessage(String content);

    @Inject(at = @At("HEAD"), method = "sendChatCommand")
    public void changeStateOnCommand(String command, CallbackInfo ci) {
        var config = AutoConfig.getConfigHolder(ComChatConfig.class).getConfig();
        var lowerCase = command.toLowerCase();
        if(config.togglePhrases.contains(lowerCase)) {
            ComChatUtil.inComChat.set(!ComChatUtil.inComChat.get());
        }
    }

    @ModifyVariable(method = "sendChatCommand", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public String replaceAliases(String command) {
        var config = AutoConfig.getConfigHolder(ComChatConfig.class).getConfig();
        if(config.aliases.contains(command)) {
            return config.togglePhrases.get(0);
        }
        return command;
    }

    @Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
    public void escapeGreetings(String content, CallbackInfo ci) {
        var config = AutoConfig.getConfigHolder(ComChatConfig.class).getConfig();
        AtomicBoolean shouldEscape = new AtomicBoolean(false);
        config.escapedPhrases.forEach(p -> {
            shouldEscape.set(shouldEscape.get() || content.matches(p));
        });
        if(ComChatUtil.inComChat.get() && shouldEscape.get()) {
            sendChatCommand(config.togglePhrases.get(0));
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
            sendChatCommand(config.togglePhrases.get(0));
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onGameJoin")
    public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        ComChatUtil.inComChat.set(false);
    }
}
