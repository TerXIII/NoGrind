package com.terxiii.nogrind;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;

public class NoGrind extends org.bukkit.plugin.java.JavaPlugin {

	public static Logger log = Logger.getLogger("Minecraft");;
	public DamageCauseListener dcl = new DamageCauseListener(this);
	@Override
	public void onDisable() {
		dcl.isEnabled = false;
		log.info("[VSP] Grinding is no longer managed.");
	}

	@Override
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		dcl.isEnabled = true;
		DamageCauseListener.scheduler = this.getServer().getScheduler();
		pm.registerEvents(dcl, this);
		configure();
		log.info("[NoGrind] Grinding is now managed.");
	}
	
	public void configure()
	{
		BufferedReader reader;
		try {
			File dir = getDataFolder();
			if (!dir.exists()) dir.mkdir();
			File config = new File(getDataFolder(),"config.cfg");
			if (!config.exists()) {
				config.createNewFile();
			}
			reader = new BufferedReader(new FileReader(config));
		
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
				log.info("[NoGrind] could not read it's config file. Have you just deleted it?");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
