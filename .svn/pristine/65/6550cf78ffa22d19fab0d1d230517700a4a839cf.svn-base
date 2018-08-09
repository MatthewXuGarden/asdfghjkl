package com.carel.supervisor.plugin.algorithmpro.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import com.carel.supervisor.plugin.algorithmpro.alias.AliasMgr;

public class AlgoProLoader 
{
	public static String JAR_NAME = "useralgopro.jar";
	public static String PROP_NAME = "algopro.conf";
	
	private AlgoProJarLoader jLoader = null;
	private boolean useMe = false;
	private String jarPath = "";

	public AlgoProLoader() {
	}

	public void loadJar() throws Exception 
	{
		jarPath = System.getenv("PVPRO_HOME") + File.separator + AliasMgr.ALGO_FOLDER + File.separator + JAR_NAME;
		URL uJarFilr = (new File(jarPath)).toURI().toURL();
		URLClassLoader loader = (URLClassLoader)this.getClass().getClassLoader();
		jLoader = new AlgoProJarLoader(loader.getURLs());
		jLoader.addURL(uJarFilr);
		useMe = true;
	}
	
	public boolean useThisLoader() {
		return this.useMe;
	}
	
	public AlgoProJarLoader getJarLoader() {
		return jLoader;
	}

	/**
	 * Load user properties file.
	 * If does not exists It throws an exception
	 */
	public Properties getPropConfiguration() 
		throws Exception 
	{
		Properties prop = new Properties();
		URL url = null;
		
		if(jLoader != null)
			url = jLoader.getResource(PROP_NAME);
		else
			url = this.getClass().getClassLoader().getResource(PROP_NAME);
			
		prop.load(url.openStream());
		return prop;
	}
}
