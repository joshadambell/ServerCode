package com.localsense.map.support;

import java.awt.Color;
import java.awt.Point;

public class Pixel 
{
    //#region Public Properties
    public Point Position = null; // { get; set; }
    public Color color = null; //{ get; set; }

    //#endregion

    //#region Constructor

    public Pixel(Point Position, Color color)
    {
        this.Position = Position;
        this.color = color;
    }
    
    

    //#endregion
}
