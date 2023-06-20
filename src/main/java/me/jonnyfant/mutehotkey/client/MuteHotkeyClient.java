package me.jonnyfant.mutehotkey.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class MuteHotkeyClient implements ClientModInitializer {
    // volume icon unicode copy paste zone
    // 🔇 strikethrough
    // 🔈 speaker
    // 🔉 speaker middle volume
    // 🔊 speaker full volume
    // 🔇 🔈 🔉 🔊
    //https://www.compart.com/en/unicode/block/U+1F300
    //https://www.compart.com/en/unicode/U+1F507
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    /**
     * Runs the mod initializer on the client environment.
     */
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
                    SimpleOption volume = mc.options.getSoundVolumeOption(SoundCategory.MASTER);
                    if ((double) volume.getValue() > 0d) {
                        volume.setValue(0d);
                        client.player.sendMessage(Text.of("\uD83D\uDD07 Volume off"));
                    } else {
                        volume.setValue((double) finalI / 100);
                        String icon;
                        if (finalI == 100)
                        {
                            icon = "\uD83D\uDD0A";
                        }
                        else
                        {
                            icon = "\uD83D\uDD09";
                        }
                        client.player.sendMessage(Text.of(icon + " Volume " + finalI + "%"));
                    }
                }
            });
        }
    }
}

