package connectedComponentLabeling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;


public class CCL implements IConnectedComponentLabeling
{
        private int[][] _board;
        private BufferedImage _input;
        private Integer _width;
        private Integer _height;

        public Map<Integer, BufferedImage> Process(BufferedImage input)
        {
            _input = input;
            _width = input.getWidth();
            _height = input.getHeight();
            _board = new int[_width][_height];

            Map<Integer, ArrayList<Pixel>> patterns = Find();
            Map<Integer, BufferedImage> images = new HashMap<Integer, BufferedImage>();

            for (Map.Entry<Integer, ArrayList<Pixel>> pattern : patterns.entrySet())
            {
            	BufferedImage bmp = CreateBitmap(pattern.getValue());
                images.put(pattern.getKey(), bmp);
            }

            return images;
        }

        public Boolean CheckIsNotBackGround(Pixel currentPixel)
        {
            return currentPixel.color.getRed() == 0 && currentPixel.color.getGreen() == 0 && currentPixel.color.getBlue() == 0;
        }

        private Map<Integer, ArrayList<Pixel>> Find()
        {
            int labelCount = 1;
            Map<Integer, Label> allLabels = new HashMap<Integer, Label>();

            for (int i = 0; i < _height; i++)
            {
                for (int j = 0; j < _width; j++)
                {    
                	Integer rgb = _input.getRGB(j, i);

                	Color c = new Color(rgb);
                	
                    Pixel currentPixel = new Pixel(new Point(j, i), c);

                    if (!CheckIsNotBackGround(currentPixel))
                    {
                        continue;
                    }

                    ArrayList<Integer> neighboringLabels = GetNeighboringLabels(currentPixel);
                    Integer currentLabel = null;

                    if (neighboringLabels.isEmpty())
                    {
                        currentLabel = labelCount;
                        allLabels.put(currentLabel, new Label(currentLabel));
                        labelCount++;
                    }
                    else
                    {
                        //currentLabel = neighboringLabels.Min(n -> allLabels.get(n).GetRoot().Name);
                        currentLabel = Collections.min(neighboringLabels);
                    	Label root = allLabels.get(currentLabel).GetRoot();

                        for (Integer neighbor : neighboringLabels)
                        {
                            if (root.Name != allLabels.get(neighbor).GetRoot().Name)
                            {
                                allLabels.get(neighbor).Join(allLabels.get(currentLabel));
                            }
                        }
                    }

                    _board[j][i] = currentLabel;
                }
            }


            Map<Integer, ArrayList<Pixel>> patterns = AggregatePatterns(allLabels);

            return patterns;
        }

        private ArrayList<Integer> GetNeighboringLabels(Pixel pix)
        {
            ArrayList<Integer> neighboringLabels = new ArrayList<Integer>();

            for (int i = pix.Position.y - 1; i <= pix.Position.y + 2 && i < _height - 1; i++)
            {
                for (int j = pix.Position.x - 1; j <= pix.Position.x + 2 && j < _width - 1; j++)
                {
                    if (i > -1 && j > -1 && _board[j][i] != 0)
                    {
                        neighboringLabels.add(_board[j][i]);
                    }
                }
            }

            return neighboringLabels;
        }

        private Map<Integer, ArrayList<Pixel>> AggregatePatterns(Map<Integer, Label> allLabels)
        {
            Map<Integer, ArrayList<Pixel>> patterns = new HashMap<Integer, ArrayList<Pixel>>();

            for (int i = 0; i < _height; i++)
            {
                for (int j = 0; j < _width; j++)
                {
                    int patternNumber = _board[j][i];

                    if (patternNumber != 0)
                    {
                        patternNumber = allLabels.get(patternNumber).GetRoot().Name;

                        if (!patterns.containsKey(patternNumber))
                        {
                        	
                        	//patterns.get(patternNumber) = new ArrayList<Pixel>();
                        	patterns.put(patternNumber, new ArrayList<Pixel>());
                        }

                        patterns.get(patternNumber).add(new Pixel(new Point(j, i), Color.BLACK));
                    }
                }
            }

            return patterns;
        }

        private BufferedImage CreateBitmap(ArrayList<Pixel> pattern)
        {	
        	int maxY = pattern.get(0).Position.y;
        	int minY = pattern.get(0).Position.y;
        	
        	int minX = pattern.get(0).Position.x;
        	int maxX = pattern.get(0).Position.x;
        	
        	for(int i = 0; i<pattern.size(); i++)
        	{
        		if(pattern.get(i).Position.x > maxX)
        			maxX = pattern.get(i).Position.x;
        		if(pattern.get(i).Position.x < minX)
        			minX = pattern.get(i).Position.x;
        		if(pattern.get(i).Position.y > maxY)
        			maxY = pattern.get(i).Position.y;
        		if(pattern.get(i).Position.x < minY)
        			minY = pattern.get(i).Position.y;
        	}

            int width = maxX + 1 - minX;
            int height = maxY + 1 - minY;
            
            // Figure out type of buffered image
            BufferedImage bmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (Pixel pix : pattern)
            {
                bmp.setRGB(pix.Position.x - minX, pix.Position.y - minY, pix.color.getRGB());//shift position by minX and minY
            }

            return bmp;
        }
}