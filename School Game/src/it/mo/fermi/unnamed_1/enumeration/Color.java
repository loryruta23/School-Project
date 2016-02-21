package it.mo.fermi.unnamed_1.enumeration;

public class Color {

    public static final Color RED = new Color(1.0f, 0.0f, 0.0f);
    public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f);
    public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f);
    public static final Color WHITE = new Color(1.0f, 1.0f, 1.0f);
    public static final Color BLACK = new Color(0.0f, 0.0f, 0.0f);

    public final float r, g, b;

    public Color(int r, int g, int b) {
        this(r / 255f, g / 255f, b / 255f);
    }

    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
