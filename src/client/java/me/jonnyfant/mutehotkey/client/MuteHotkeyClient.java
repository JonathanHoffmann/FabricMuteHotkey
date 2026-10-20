package me.jonnyfant.mutehotkey.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;

public class MuteHotkeyClient implements ClientModInitializer {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("mutehotkey", "mute")
    );

    @Override
    public void onInitializeClient() {
        for (int i = 10; i <= 100; i += 10) {
            int defaultKey = i < 100 ? InputConstants.UNKNOWN.getValue() : InputConstants.KEY_M;
            KeyMapping keyMapping = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                    "key.mute.mute" + i,
                    InputConstants.Type.KEYBOARD,
                    defaultKey,
                    CATEGORY
            ));
            int volumePercent = i;

            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (keyMapping.consumeClick()) {
                    OptionInstance<Double> volume = client.options.getSoundSourceOptionInstance(SoundSource.MASTER);
                    if (volume.get() > 0d) {
                        volume.set(0d);
                        if (client.player != null) {
                            client.player.sendSystemMessage(Component.literal("\uD83D\uDD07 Volume off"));
                        }
                    } else {
                        volume.set(volumePercent / 100.0);
                        String icon = volumePercent == 100 ? "\uD83D\uDD0A" : "\uD83D\uDD09";
                        if (client.player != null) {
                            client.player.sendSystemMessage(Component.literal(icon + " Volume " + volumePercent + "%"));
                        }
                    }
                }
            });
        }
    }
}
