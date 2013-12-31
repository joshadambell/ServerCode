package connectedComponentLabeling;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface IConnectedComponentLabeling 
{
        Map<Integer, BufferedImage> Process(BufferedImage input);
}
