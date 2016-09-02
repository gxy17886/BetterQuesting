package betterquesting.client.toolbox.tools;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import betterquesting.api.client.gui.premade.screens.GuiScreenThemed;
import betterquesting.api.enums.EnumPacketAction;
import betterquesting.api.enums.EnumSaveType;
import betterquesting.api.network.PacketTypeNative;
import betterquesting.api.network.PreparedPayload;
import betterquesting.api.quests.IQuestContainer;
import betterquesting.api.quests.properties.QuestProperties;
import betterquesting.api.utils.BigItemStack;
import betterquesting.api.utils.JsonHelper;
import betterquesting.api.utils.NBTConverter;
import betterquesting.client.gui.editors.json.GuiJsonItemSelection;
import betterquesting.network.PacketSender;
import betterquesting.quests.QuestDatabase;
import com.google.gson.JsonObject;

public class GuiToolIconProxy extends GuiScreenThemed
{
	private final IQuestContainer quest;
	private BigItemStack stack;
	private final JsonObject jIcon;
	boolean flag = false;
	
	public GuiToolIconProxy(GuiScreen parent, IQuestContainer quest)
	{
		super(parent, "");
		this.quest = quest;
		stack = quest.getInfo().getProperty(QuestProperties.ICON);
		stack = stack != null? stack : new BigItemStack(Items.nether_star);
		jIcon = JsonHelper.ItemStackToJson(stack, new JsonObject());
	}
	
	@Override
	public void initGui()
	{
		if(flag)
		{
			stack = JsonHelper.JsonToItemStack(jIcon);
			stack = stack != null? stack : new BigItemStack(Items.nether_star);
			quest.getInfo().setProperty(QuestProperties.ICON, stack);
			SendChanges();
			this.mc.displayGuiScreen(parent);
			return;
		} else
		{
			flag = true;
			mc.displayGuiScreen(new GuiJsonItemSelection(this, jIcon));
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
		
		PacketSender.INSTANCE.sendToServer(new PreparedPayload(PacketTypeNative.QUEST_EDIT.GetLocation(), tags));
	}
}
