package me.jonnyfant.mutehotkey.client;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class MuteHotkeyClient implements ClientModInitializer {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void onInitializeClient() {
        KeyBinding keyBinding;
        int i;
        for (i = 10; i <= 100; i += 10) {
            if (i < 100) {
                keyBinding = new KeyBinding("key.mute.mute" + i, InputUtil.Type.KEYSYM, GLFW.GLFW_DONT_CARE, "category.mute.mute");
            } else {
                keyBinding = new KeyBinding("key.mute.mute" + i, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "category.mute.mute");
            }

            KeyBindingHelper.registerKeyBinding(keyBinding);
            KeyBinding finalKeyBinding = keyBinding;
            int finalI = i;

            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (finalKeyBinding.wasPressed()) {
                    SimpleOption<Double> volume = mc.options.getSoundVolumeOption(SoundCategory.MASTER);
                    if (volume.getValue() > 0d) {
                        volume.setValue(0d);
                        client.player.sendMessage(Text.of("\uD83D\uDD07 Volume off"), false);
                    } else {
                        volume.setValue(finalI / 100.0);
                        String icon = finalI == 100 ? "\uD83D\uDD0A" : "\uD83D\uDD09";
                        client.player.sendMessage(Text.of(icon + " Volume " + finalI + "%"), false);
                    }
                }
            });
        }
    }
}
