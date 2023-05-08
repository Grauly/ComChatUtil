package grauly.comchatutils.mixin;

import grauly.comchatutils.ComChatUtil;
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
            chatField.setEditableColor(ComChatUtil.COM_CHAT_COLOR.getRGB());
        } else {
            chatField.setEditableColor(TextFieldWidget.DEFAULT_EDITABLE_COLOR);
        }
    }
}
