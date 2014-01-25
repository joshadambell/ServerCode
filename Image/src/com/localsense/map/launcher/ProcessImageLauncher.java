package com.localsense.map.launcher;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.localsense.map.support.CCLImpl;

public class ProcessImageLauncher 
{
	
	public static void main(String[] args) throws IOException 
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
    
		String path = opts.savePath;
		BufferedImage input = ImageIO.read(new File(path + opts.saveName));
		
		CCLImpl ccl = new CCLImpl();
			
		if(input != null)
			ccl.Process(input);
	}
	

	@Parameters(separators = "=", optionPrefixes = "--")
	public static class CommandLineOptions
	{
	  @Parameter(names = "--savePath", required = true)
    private String savePath;
    @Parameter(names = "--saveName", required = true)
    private String saveName;
	}
}
