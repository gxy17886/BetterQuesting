package bq_standard.core;

import org.apache.logging.log4j.Logger;
import betterquesting.quests.rewards.RewardRegistry;
import betterquesting.quests.tasks.TaskRegistry;
import bq_standard.core.proxies.CommonProxy;
import bq_standard.rewards.RewardChoice;
import bq_standard.rewards.RewardItem;
import bq_standard.rewards.RewardScoreboard;
import bq_standard.tasks.TaskCrafting;
import bq_standard.tasks.TaskFluid;
import bq_standard.tasks.TaskHunt;
import bq_standard.tasks.TaskLocation;
import bq_standard.tasks.TaskRetrieval;
import bq_standard.tasks.TaskScoreboard;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = BQ_Standard.MODID, version = BQ_Standard.VERSION, name = BQ_Standard.NAME)
public class BQ_Standard
{
    public static final String MODID = "bq_standard";
    public static final String VERSION = "BQ_VER_KEY";
    public static final String NAME = "Standard Questing Pack";
    public static final String PROXY = "bq_standard.core.proxies";
    public static final String CHANNEL = "BQ_STANDARD";
	
	@Instance(MODID)
	public static BQ_Standard instance;
	
	@SidedProxy(clientSide = PROXY + ".ClientProxy", serverSide = PROXY + ".CommonProxy")
	public static CommonProxy proxy;
	public SimpleNetworkWrapper network;
	public static Logger logger;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	logger = event.getModLog();
    	network = NetworkRegistry.INSTANCE.newSimpleChannel(CHANNEL);
    	
    	proxy.registerHandlers();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	TaskRegistry.RegisterTask(TaskRetrieval.class, "retrieval");
    	TaskRegistry.RegisterTask(TaskHunt.class, "hunt");
    	TaskRegistry.RegisterTask(TaskLocation.class, "location");
    	TaskRegistry.RegisterTask(TaskCrafting.class, "crafting");
    	TaskRegistry.RegisterTask(TaskScoreboard.class, "scoreboard");
    	TaskRegistry.RegisterTask(TaskFluid.class, "fluid");
    	
    	RewardRegistry.RegisterReward(RewardItem.class, "item");
    	RewardRegistry.RegisterReward(RewardChoice.class, "choice");
    	RewardRegistry.RegisterReward(RewardScoreboard.class, "scoreboard");
    	
    	proxy.registerThemes();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
}
