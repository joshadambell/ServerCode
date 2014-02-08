package com.localsense.map.api;

import java.awt.image.BufferedImage;
import java.util.Map;

import com.localsense.map.support.Tuple;

public interface CCL
{
  Map<Integer, Tuple<Integer, Integer>> Process(BufferedImage input);
}
