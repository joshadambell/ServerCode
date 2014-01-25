package com.localsense.map.support;

import java.util.HashMap;
import java.util.Map;

import com.localsense.map.api.BuildMap;

/**
 * Created by joshbell on 1/6/14.
 */

// Whether I get the information the user entering it in the website or
// from processing an image, what is the info I want. Each of the dimensions
// an arrayList<Integer> of the dimensions
public class BuildMapImpl implements BuildMap
{
    // default 500 pixel width
    private Integer _percentageChange = null;
    private static final Mattress king = new Mattress(76, 80);
    private static final Mattress queen = new Mattress(60, 80);
    private static final Mattress full = new Mattress(53, 75);

    // Converts size of object in inches to pixels
    public Integer changeBedDimensions(Integer size)
    {
        return size * _percentageChange;
    }

    // height and width of available area
    @Override
    public void ConvertToPixels(Integer width, Integer height, Integer[] floorDimensions)
    {
        int margin = 10;
        Map<String, Integer> changedMattresses = new HashMap<String, Integer>();

        _percentageChange = width/floorDimensions[0];

        for (int i = 0; i < floorDimensions.length; i++)
        {
            floorDimensions[i] = _percentageChange * floorDimensions[i];
        }

        // add margin
        int newWidth = width - margin;
        int newHeight = height - margin;

      /*
        king.setLength(_percentageChange * king.getLength());
        king.setWidth(_percentageChange * king.getWidth());
        queen.setWidth(_percentageChange * queen.getWidth());
        queen.setLength(_percentageChange * queen.getLength());
        full.setLength(_percentageChange * full.getLength());
        full.setWidth(_percentageChange * full.getWidth());
*/


        // step through the mattresses
        for (MattressEnum m : MattressEnum.values())
        {
            if(m.name().equals("KING"))
            {
              changedMattresses.put("king", m.getLength() * _percentageChange);
            }
            else if(m.name().equals("QUEEN"))
            {
              changedMattresses.put("queen", m.getLength() * _percentageChange);
            }
            else if(m.name().equals("FULL"))
            {
              changedMattresses.put("full", m.getLength() * _percentageChange);
            }
        }

          //TODO: store values into the database

        return;
    }
}
