package com.localsense.map.launcher;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import lombok.SneakyThrows;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.localsense.map.api.DAO;
import com.localsense.map.support.CCLImpl;
import com.localsense.map.support.LoadDbImpl;
import com.localsense.map.support.Tuple;

public class ProcessImageLauncher 
{
	@SneakyThrows
	public static void main(String[] args) 
	{
	  CommandLineOptions opts = new CommandLineOptions();	  
	  JCommander parser = new JCommander(opts);
    try
    {
      parser.parse(args);
    }
    catch (Exception e)
    {
      parser.usage();
      return;
    }
    
		String path = opts.path;
		BufferedImage input = ImageIO.read(new File(path));
		Map<Integer, Tuple<Integer, Integer>> locations = null;
		
		CCLImpl ccl = new CCLImpl();
			
		if(input != null)
			locations = ccl.Process(input);
		
		LoadDbImpl loadDbImpl = new LoadDbImpl();
		loadDbImpl.insertIntoDb(locations);	
	}
	

	@Parameters(separators = "=", optionPrefixes = "--")
	public static class CommandLineOptions
	{
	  @Parameter(names = "--path", required = true)
    private String path;
	}
}
