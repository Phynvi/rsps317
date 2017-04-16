package com.bclaus.rsps.server;


import com.bclaus.rsps.server.vd.clan.ClanManager;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.omicron.jagex.runescape.CollisionMap;

//import com.rspserver.motivote.Motivote;

import com.bclaus.rsps.server.vd.World;
import com.bclaus.rsps.server.vd.content.DoubleExperience;
import com.bclaus.rsps.server.vd.content.ShopHandler;
import com.bclaus.rsps.server.vd.content.bountyhunter.BountyHunterTick;
import com.bclaus.rsps.server.vd.content.instanced.impl.InstanceFloorReset;
import com.bclaus.rsps.server.vd.content.minigames.FightCaves;
import com.bclaus.rsps.server.vd.content.minigames.PestControl;
import com.bclaus.rsps.server.vd.event.CycleEventHandler;
import com.bclaus.rsps.server.vd.npc.NPCHandler;
import com.bclaus.rsps.server.vd.npc.NPCSize;
import com.bclaus.rsps.server.vd.player.Player;
import com.bclaus.rsps.server.vd.player.impl.RestoreSpecialStats;
import com.bclaus.rsps.server.vd.player.impl.RestoreStats;
import com.bclaus.rsps.server.vd.player.impl.UpdateTime;
import com.bclaus.rsps.server.vd.world.ItemHandler;
import com.bclaus.rsps.server.vd.world.ObjectHandler;
//import MySQLController;
import com.bclaus.rsps.server.login.NettyChannelInitializer;
import com.bclaus.rsps.server.quarantine.QuarantineIO;
import com.bclaus.rsps.server.region.ObjectDef;
import com.bclaus.rsps.server.region.Region;
import com.bclaus.rsps.server.task.TaskScheduler;
import com.bclaus.rsps.server.task.impl.AltarRestore;
import com.bclaus.rsps.server.task.impl.NPCMovementTask;
import com.bclaus.rsps.server.task.impl.TempPlayerSave;
import com.bclaus.rsps.server.util.DisplayName;
import com.bclaus.rsps.server.util.Misc;
import com.bclaus.rsps.server.util.NPCPoisonLoader;
//import server.util.RewardHandler;
import com.bclaus.rsps.server.util.SimpleCalendar;
import com.bclaus.rsps.server.util.WeaponPoisonLoader;

/**
 * Server.java
 * 
 * @author Sanity
 * @author Graham
 * @author Blake
 * @author Ryan Lmtruck30
 */

public class Server implements Runnable {
	/**
	 * Set the state of the constructor to false to change the state of the
	 * frame to invisible.
	 */
	public static ItemHandler itemHandler = new ItemHandler();
	//static AccountModifier m = new AccountModifier(true);
	public static NPCHandler npcHandler = new NPCHandler();

	public static ObjectHandler objectHandler = new ObjectHandler();

	public static FightCaves fightCaves = new FightCaves();

	public static PestControl pestControl = new PestControl();

	public static ClanManager clanManager = new ClanManager();
	


	public static final SimpleCalendar CALENDAR = new SimpleCalendar(TimeUnit.MINUTES, new SimpleDateFormat("yyyy/MM/dd HH:mm"));
	
	public static boolean needsHighscoresReset = false;
	
	public static final ScheduledExecutorService GENERAL_EXECUTOR = Executors.newSingleThreadScheduledExecutor();
	

	public static ShopHandler shopHandler = new ShopHandler();
	/**
	 * Handles logged in players.
	 */
	public static Player player;
	/**
	 * The task scheduler.
	 */
	
	public static final TaskScheduler scheduler = new TaskScheduler();
	public static final String playerHandler = null;

	/**
	 * Gets the task scheduler.
	 * 
	 * @return The task scheduler.
	 */
	
	public static TaskScheduler getTaskScheduler() {
		return scheduler;
	}
	
	/**
	 *  Used to disable & enable certain features
	 */
	public static boolean disableMysql = false;
	public static boolean localHost = true;

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(java.lang.String args[]) throws Exception {
		System.out.println("Starting up DemonRsps..!");

		/* set-up our stream output formatting */
		//System.setOut(new ConsoleStream(System.out));
		//System.setErr(new ConsoleStream(System.err));
		//new Motivote(new RewardHandler(), "http://www.DemonRsps.com/vote/", "ebbb134e").start();
		//new Motivote(new RewardHandler(), "http://DemonRsps.com/vote/", "f28af9cc").start();
		//MySQLController.init();
		QuarantineIO.read();
		Misc.loadNpcDrops();
		CycleEventHandler.getSingleton().process();
		Misc.loadNpcAggression().load();
		new WeaponPoisonLoader().load();
		new NPCPoisonLoader().load();
		ObjectDef.loadConfig();
		Region.load();
		NPCSize.init();
		DisplayName.init();
		//*CharacterBackup.init();
		SocketAddress address = new InetSocketAddress(43594);
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.childHandler(new NettyChannelInitializer());
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.group(new NioEventLoopGroup());
		bootstrap.bind(address);
		Connection.initialize();
		//Runtime.getRuntime().addShutdownHook(ShutdownHook.getSingleton());
		CollisionMap.load("data/data/collisiondata.dat");
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Server(), 600, 600, TimeUnit.MILLISECONDS);
		scheduler.schedule(new RestoreStats());
		scheduler.schedule(new BountyHunterTick());
		scheduler.schedule(new NPCMovementTask());
		scheduler.schedule(new UpdateTime());
		scheduler.schedule(new AltarRestore());
		scheduler.schedule(new RestoreSpecialStats());
		scheduler.schedule(new InstanceFloorReset());
		scheduler.schedule(new TempPlayerSave());
		clanManager.create("DemonRsps", "demon");
		//scheduler.schedule(new Raid());
		System.out.println("Double XP: " + DoubleExperience.isDoubleExperience());
		System.out.println("DemonRsps started, current server port:" + 43594);
	}

	@Override
	public void run() {
		try {
			scheduler.run();
			World.process();
			itemHandler.process();
			npcHandler.process();
			ShopHandler.process();
			pestControl.process();
			} catch (Exception e) {
		e.printStackTrace();
		}
	}

	public static int getSleepTimer() {
		// TODO Auto-generated method stub
		return 0;
	}
}
