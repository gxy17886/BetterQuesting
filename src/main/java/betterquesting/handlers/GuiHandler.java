package betterquesting.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import betterquesting.blocks.TileSubmitStation;
import betterquesting.client.gui.help.GuiQuestingHelp;
import betterquesting.client.gui.inventory.ContainerSubmitStation;
import betterquesting.client.gui.inventory.GuiSubmitStation;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		
		if(ID == 0 && tile instanceof TileSubmitStation)
		{
			return new ContainerSubmitStation(player.inventory, (TileSubmitStation)tile);
		}
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		
		if(ID == 0 && tile instanceof TileSubmitStation)
		{
			return new GuiSubmitStation(null, player.inventory, (TileSubmitStation)tile);
		} else if(ID == 1)
		{
			return new GuiQuestingHelp(null);
		}
		
		return null;
	}
	
}
