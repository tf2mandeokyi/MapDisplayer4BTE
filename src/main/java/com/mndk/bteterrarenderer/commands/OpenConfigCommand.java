package com.mndk.bteterrarenderer.commands;

import com.mndk.bteterrarenderer.gui.MapRenderingOptionsUI;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.IClientCommand;

@Deprecated
public class OpenConfigCommand extends CommandBase implements IClientCommand {

	@Override
	public void execute(MinecraftServer var1, ICommandSender var2, String[] var3) throws CommandException {
		MapRenderingOptionsUI.open(50); // TODO make a rendering scheduler or something to make this work
	}

	@Override
	public String getName() {
		return "openbtrcfg";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "openbtrcfg";
	}

	@Override
	public boolean allowUsageWithoutPrefix(ICommandSender var1, String var2) {
		return false;
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

}
