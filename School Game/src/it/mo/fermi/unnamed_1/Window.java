package it.mo.fermi.unnamed_1;

public class Window {

    private final String title;
    private int width = -1;
    private int height = -1;
    private boolean fullScreened;

    /**
     * Initialize a new window.
     * @param height The window height.
     * @param width The window width.
     * @param title The window title.
     */
    public Window(int height, int width, String title) {
        this.height = height;
        this.width = width;
        this.title = title;
        fullScreened = false;
    }

    /**
     * Initialize a new full-screened window.
     * @param title The window title.
     */
    public Window(String title) {
        this.fullScreened = fullScreened;
        this.title = title;
    }

    /**
     * Get the window title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the window width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the window height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Is the window full-screened?
     */
    public boolean isFullScreened() {
        return fullScreened;
    }
}
