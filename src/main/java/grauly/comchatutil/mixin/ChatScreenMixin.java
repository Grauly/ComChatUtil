package grauly.comchatutil.mixin;

import grauly.comchatutil.ComChatUtil;
import grauly.comchatutil.config.ComChatConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    @Shadow protected TextFieldWidget chatField;

    @Inject(at = @At("TAIL"), method = "onChatFieldUpdate")
    private void onChatFieldUpdate(String chatText, CallbackInfo ci) {
        if(ComChatUtil.inComChat) {
            ComChatConfig config = AutoConfig.getConfigHolder(ComChatConfig.class).getConfig();
            chatField.setEditableColor(config.color);
        } else {
            chatField.setEditableColor(TextFieldWidget.DEFAULT_EDITABLE_COLOR);
        }
    }
}
