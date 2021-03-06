package betterquesting.client.toolbox;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import betterquesting.api.client.gui.GuiScreenThemed;
import betterquesting.api.enums.EnumPacketAction;
import betterquesting.api.enums.EnumSaveType;
import betterquesting.api.misc.ICallback;
import betterquesting.api.network.QuestingPacket;
import betterquesting.api.properties.NativeProps;
import betterquesting.api.questing.IQuest;
import betterquesting.api.utils.BigItemStack;
import betterquesting.api.utils.NBTConverter;
import betterquesting.client.gui.editors.json.GuiJsonItemSelection;
import betterquesting.network.PacketSender;
import betterquesting.network.PacketTypeNative;
import betterquesting.questing.QuestDatabase;
import com.google.gson.JsonObject;

public class GuiToolIconProxy extends GuiScreenThemed implements ICallback<BigItemStack>
{
	private final IQuest quest;
	boolean flag = false;
	
	public GuiToolIconProxy(GuiScreen parent, IQuest quest)
	{
		super(parent, "");
		this.quest = quest;
	}
	
	@Override
	public void initGui()
	{
		if(flag)
		{
			this.mc.displayGuiScreen(parent);
			return;
		} else
		{
			flag = true;
			mc.displayGuiScreen(new GuiJsonItemSelection(this, this, quest.getItemIcon()));
		}
	}
	
	// If the changes are approved by the server, it will be broadcast to all players including the editor
	public void SendChanges()
	{
		JsonObject base = new JsonObject();
		base.add("config", quest.writeToJson(new JsonObject(), EnumSaveType.CONFIG));
		base.add("progress", quest.writeToJson(new JsonObject(), EnumSaveType.PROGRESS));
		NBTTagCompound tags = new NBTTagCompound();
		tags.setTag("data", NBTConverter.JSONtoNBT_Object(base, new NBTTagCompound()));
		tags.setInteger("action", EnumPacketAction.EDIT.ordinal());
		tags.setInteger("questID", QuestDatabase.INSTANCE.getKey(quest));
		
		PacketSender.INSTANCE.sendToServer(new QuestingPacket(PacketTypeNative.QUEST_EDIT.GetLocation(), tags));
	}

	@Override
	public void setValue(BigItemStack value)
	{
		BigItemStack stack = value != null? value : new BigItemStack(Items.NETHER_STAR);
		quest.getProperties().setProperty(NativeProps.ICON, stack);
		SendChanges();
	}
}
