package com.jonathanhoffmann.mute;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

public class Mute implements ClientModInitializer {

    private static KeyBinding keyBinding;

    private static final MinecraftClient mc = MinecraftClient.getInstance();



    @Override
    public void onInitializeClient() {
        keyBinding = new KeyBinding("key.mute.mute", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "category.mute.mute");


        KeyBindingHelper.registerKeyBinding(keyBinding);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {

                if(mc.options.getSoundVolume(SoundCategory.MASTER)>0) {
                    MinecraftClient.getInstance().options.setSoundVolume(SoundCategory.MASTER, 0);
                    client.player.sendMessage(new LiteralText("Volume off"), false);
                }

                else {
                    MinecraftClient.getInstance().options.setSoundVolume(SoundCategory.MASTER, 1);
                    client.player.sendMessage(new LiteralText("Volume on"), false);
                }
            }
        });
    }
}
