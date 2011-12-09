package com.terxiii.nogrind;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

public class NoGrind extends org.bukkit.plugin.java.JavaPlugin {

	
	public static Logger log;
	public DamageCauseListener dcl;
	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		dcl.isEnabled = false;
		log.info("[VSP] Grinding is no longer managed.");
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		log = Logger.getLogger("Minecraft");
		PluginManager pm = this.getServer().getPluginManager();
		dcl = new DamageCauseListener(this);
		dcl.isEnabled = true;
		DamageCauseListener.scheduler = this.getServer().getScheduler();
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, dcl, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, dcl, Event.Priority.Normal, this);
		configure();
		log.info("[NoGrind] Grinding is now managed.");
	}
	
	public void configure()
	{
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(new File(getDataFolder(),"config.cfg")));
		
			String s;
			try {
				s = reader.readLine();
				
				while(s!=null&&s.length()>0)
				{
					log.info("Read line: "+s);
					String cfg = s.substring(0,s.indexOf("="));
					if(cfg.equals("combatTagSeconds"))
					{
						log.info("Found recognized tag");
						String len = s.substring(s.indexOf("=")+1);
						DamageCauseListener.setExpireTime(Integer.parseInt(len));
						s = reader.readLine();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
