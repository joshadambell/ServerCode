package com.localsense.map.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

import com.localsense.map.api.*;

public class CCLImpl implements CCL
{
  private int[][] _board;
  private BufferedImage _input;
  private Integer _width;
  private Integer _height;

  public Map<Integer, Tuple<Integer, Integer>> Process(BufferedImage input)
  {
    _input = input;
    _width = input.getWidth();
    _height = input.getHeight();
    _board = new int[_width][_height];

    // Object number to array of all pixel in object
    Map<Integer, ArrayList<Pixel>> patterns = Find();

    // Same integer as above mapped its height and width
    Map<Integer, Tuple<Integer, Integer>> dimensions = new HashMap<Integer, Tuple<Integer, Integer>>();

    // object number mapped to x y coordinates against the position of the largest image, hopefully
    // being the floor layout
    Map<Integer, Tuple<Integer, Integer>> locations = new HashMap<Integer, Tuple<Integer, Integer>>();
    Integer objectWithMaxArea = null;
    int maxArea = 0;
    int minXFloor = 0;
    int minYFloor = 0;

    // I know to find out where each of the objects are from a specific point. If there is a floor
    // outline then from the smallest x and smallest y of the largest object or
    // if there is no floor then use what????

    // Push this info to the database. The width, height, x y position relative to the floor
    // What account associated with, label what is the floor and what is a bed: 0 floor, 1 bed
    //
    for (Map.Entry<Integer, ArrayList<Pixel>> pattern : patterns.entrySet())
    {
      // find the width and height of each object
      // Tuple<Integer, Integer> objectDimensions = FindWidthAndHeight(pattern.getValue());
      ArrayList<Pixel> patternArrayList = pattern.getValue();
      int maxY = patternArrayList.get(0).Position.y;
      int minY = patternArrayList.get(0).Position.y;

      int minX = patternArrayList.get(0).Position.x;
      int maxX = patternArrayList.get(0).Position.x;

      for (int i = 0; i < patternArrayList.size(); i++)
      {
        if (patternArrayList.get(i).Position.x > maxX)
          maxX = patternArrayList.get(i).Position.x;
        if (patternArrayList.get(i).Position.x < minX)
          minX = patternArrayList.get(i).Position.x;
        if (patternArrayList.get(i).Position.y > maxY)
          maxY = patternArrayList.get(i).Position.y;
        if (patternArrayList.get(i).Position.x < minY)
          minY = patternArrayList.get(i).Position.y;
      }

      int width = maxX + 1 - minX;
      int height = maxY + 1 - minY;

      dimensions.put(pattern.getKey(), new Tuple<Integer, Integer>(width, height));

      // find the biggest object
      // the largest object will always be the first one
      int area = width * height;
      if (area > maxArea)
      {
        objectWithMaxArea = pattern.getKey();
        maxArea = area;
        minXFloor = minX;
        minYFloor = minY;
      }

      // find the top min point of each object
      // object min and max minus the largest objects min and max
      int xLocation = minX - minXFloor;
      int yLocation = minY - minYFloor;
      Tuple<Integer, Integer> objectLocation = new Tuple<Integer, Integer>(xLocation, yLocation);
      locations.put(pattern.getKey(), objectLocation);
    }

    // TODO: add info from locations and dimensions into the database.

    return locations;
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
          // currentLabel = neighboringLabels.Min(n -> allLabels.get(n).GetRoot().Name);
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

            // patterns.get(patternNumber) = new ArrayList<Pixel>();
            patterns.put(patternNumber, new ArrayList<Pixel>());
          }

          patterns.get(patternNumber).add(new Pixel(new Point(j, i), Color.BLACK));
        }
      }
    }

    return patterns;
  }

  private Tuple<Integer, Integer> FindWidthAndHeight(ArrayList<Pixel> pattern)
  {
    int maxY = pattern.get(0).Position.y;
    int minY = pattern.get(0).Position.y;

    int minX = pattern.get(0).Position.x;
    int maxX = pattern.get(0).Position.x;

    for (int i = 0; i < pattern.size(); i++)
    {
      if (pattern.get(i).Position.x > maxX)
        maxX = pattern.get(i).Position.x;
      if (pattern.get(i).Position.x < minX)
        minX = pattern.get(i).Position.x;
      if (pattern.get(i).Position.y > maxY)
        maxY = pattern.get(i).Position.y;
      if (pattern.get(i).Position.x < minY)
        minY = pattern.get(i).Position.y;
    }

    int width = maxX + 1 - minX;
    int height = maxY + 1 - minY;

    Tuple<Integer, Integer> tuple = new Tuple<Integer, Integer>(width, height);

    return tuple;
  }
}
