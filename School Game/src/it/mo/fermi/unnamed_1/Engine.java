package it.mo.fermi.unnamed_1;

import it.mo.fermi.unnamed_1.enumeration.Action;
import it.mo.fermi.unnamed_1.enumeration.Color;
import it.mo.fermi.unnamed_1.enumeration.Key;
import it.mo.fermi.unnamed_1.enumeration.MouseButton;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.nio.DoubleBuffer;
import java.util.Optional;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Engine {

    public interface Listener {

        void onKeyDown(Key key, Action action);

        void onMouseDown(Point2D where, MouseButton button, Action action);

        boolean onUpdate();

        boolean onRender();
    }

    private final long windowId;
    private final GLFWKeyCallback keyDownCallback;
    private final GLFWMouseButtonCallback mouseDownCallback;
    private final OpenGLHandler openGLHandler;

    private Color backColor;
    private Listener listener;
    private Scene currentScene;

    private volatile long tmp_FPS;
    private volatile long FPS;
    private final Thread FPSCounter;

    public Engine(Window window) {
        if (glfwInit() == GLFW_FALSE)
            throw new IllegalStateException("Could not initialize GLfw.");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        windowId = glfwCreateWindow(
                window.getWidth(),
                window.getHeight(),
                window.getTitle(),
                window.isFullScreened() ? glfwGetPrimaryMonitor() : NULL,
                NULL
        );

        if (windowId == NULL)
            throw new IllegalStateException("Failed to initialize GLfw window.");

        glfwSetKeyCallback(windowId, keyDownCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window_id, int key_code, int scanCode, int action_code, int mods) {
                Optional<Key> key_ = Key.getKey(key_code);
                Optional<Action> action_ = Action.getAction(action_code);
                if (!key_.isPresent() || !action_.isPresent())
                    return;

                if (listener != null)
                    listener.onKeyDown(key_.get(), action_.get());
            }
        });

        glfwSetMouseButtonCallback(windowId, mouseDownCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window_id, int button_code, int action_code, int mods) {
                DoubleBuffer buf_x = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer buf_invY = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window_id, buf_x, buf_invY);

                float x = (float) buf_x.get();
                float y = (float) (buf_invY.get() * -1 + window.getHeight()); // invert y

                Optional<MouseButton> button_ = MouseButton.getMouseButton(button_code);
                Optional<Action> action_ = Action.getAction(action_code);
                if (!button_.isPresent() || !action_.isPresent())
                    return;

                if (listener != null)
                    listener.onMouseDown(new Point2D(x, y), button_.get(), action_.get());
            }
        });

        if (!window.isFullScreened()) {
            GLFWVidMode video_mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(
                    windowId,
                    (video_mode.width() - window.getWidth()) / 2,
                    (video_mode.height() - window.getHeight()) / 2
            );
        }

        glfwMakeContextCurrent(windowId);
        glfwSwapInterval(10);
        glfwShowWindow(windowId);

        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        openGLHandler = new OpenGLHandler();

        FPSCounter = new Thread(() -> {
            while (glfwWindowShouldClose(windowId) == GLFW_FALSE) {
                FPS = tmp_FPS;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                tmp_FPS = 0;
            }
        });
    }

    public void loop() {
        FPSCounter.start();
        while (glfwWindowShouldClose(windowId) == GLFW_FALSE) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor(
                    backColor.r,
                    backColor.g,
                    backColor.b,
                    1.0f
            );
            if (listener != null && (!listener.onUpdate() || !listener.onRender()))
                return;
            currentScene.ifPresent(Scene::draw);

            glfwSwapBuffers(windowId);
            glfwPollEvents();
            tmp_FPS++;
        }
    }

    public long getFPS() {
        return FPS;
    }
}
