package buildMap;

import buildMap.processImage.CCL;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main 
{
	
	public static void main(String[] args) throws IOException 
	{
		String savePath = "/Users/joshbell/Desktop/Image stuff/";
		BufferedImage input = ImageIO.read(new File(savePath + "main.png"));
		
		CCL ccl = new CCL();
			
		if(input != null)
			ccl.Process(input);
	}
}
