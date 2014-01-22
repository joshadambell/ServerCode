package buildMap.scaleMap;

/**
 * Created by joshbell on 1/6/14.
 */
public enum MattressEnum
{
    KING(76, 80),
    QUEEN(60, 80),
    FULL(53, 75);

    private final int width;
    private final int length;

    MattressEnum(int width, int length)
    {
        this.width = width;
        this.length = length;
    }

    public int getWidth() { return width; }
    public int getLength() { return length; }
}
