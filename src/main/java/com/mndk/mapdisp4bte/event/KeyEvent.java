package com.mndk.mapdisp4bte.event;

import com.mndk.mapdisp4bte.config.ConfigHandler;
import com.mndk.mapdisp4bte.gui.MapRenderingOptionsUi;
import com.mndk.mapdisp4bte.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KeyEvent {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onKeyEvent(InputEvent.KeyInputEvent event) {
        if(ClientProxy.mapOptionsKey.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new MapRenderingOptionsUi());
        } else if(ClientProxy.mapToggleKey.isPressed()) {
            ConfigHandler.getModConfig().toggleDrawTiles();
        }
    }

}
