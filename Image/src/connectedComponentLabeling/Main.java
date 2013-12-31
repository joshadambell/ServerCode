package connectedComponentLabeling;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

public class Main 
{
	
	public static void main(String[] args) throws IOException 
	{
		String savePath = "/Users/joshbell/Desktop/Image stuff/";
		BufferedImage input = ImageIO.read(new File(savePath + "square.png"));
		Map<Integer, BufferedImage> finalMap = new HashMap<Integer, BufferedImage>();
		
		
		CCL ccl = new CCL();
			
		if(input != null)
			finalMap = ccl.Process(input);
			
		Iterator<Entry<Integer, BufferedImage>> it = finalMap.entrySet().iterator();
		
		while(it.hasNext())
		{
			Entry<Integer, BufferedImage> pairs = it.next();
			BufferedImage buf = (BufferedImage)pairs.getValue();
			File file = new File(savePath + pairs.getKey() + ".jpg");
			ImageIO.write(buf, "jpg", file);
			
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
			buf.getRaster().getDataBuffer();
		}
	}
}
