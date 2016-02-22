package it.mo.fermi.unnamed_1;

import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class OpenGLHandler {

    private static final float[] VERTICES = {
            -1.0f, 1.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 1.0f, 0.0f,
            -1.0f, -1.0f, 0.0f, 1.0f,
            1.0f, -1.0f, 1.0f, 1.0f
    };

    private static final int[] ELEMENTS = {
            0, 1, 2,
            1, 2, 3
    };

    private static String getShaderSource(File file) {
        String out = "";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null)
                out += line + "\n";
            reader.close();
        } catch (IOException ignored) {
            return out;
        }
        return out;
    }

    private final int vaoId, vboId, eboId;
    private final int vsShaderId, fsShaderId, program;

    /**
     * Initialize a new OpenGL Handler, create buffers and shader programs.
     */
    public OpenGLHandler() {
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(VERTICES.length);
        verticesBuffer.put(VERTICES);
        verticesBuffer.flip();

        IntBuffer elementsBuffer = BufferUtils.createIntBuffer(ELEMENTS.length);
        elementsBuffer.put(ELEMENTS);
        elementsBuffer.flip();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementsBuffer, GL_STATIC_DRAW);

        vsShaderId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vsShaderId, getShaderSource(new File("resources/shader/vertex.glsl")));
        glCompileShader(vsShaderId);

        fsShaderId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fsShaderId, getShaderSource(new File("resources/shader/fragment.glsl")));
        glCompileShader(fsShaderId);

        program = glCreateProgram();

        glAttachShader(program, vsShaderId);
        glAttachShader(program, fsShaderId);

        glLinkProgram(program);
        glUseProgram(program);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
    }


    public enum Uniform {
        MODEL,
        COLOR,
        TEXTURE_DATA,
        SPRITE_SIDES,
        SPRITE_COORD;

        public String getName() {
            return name().toLowerCase();
        }
    }

    /**
     * Destroy all OpenGL created buffers and shader programs.
     */
    public void destroy() {
        glDetachShader(program, fsShaderId);
        glDetachShader(program, vsShaderId);
        glUseProgram(0);

        glDeleteShader(fsShaderId);
        glDeleteShader(vsShaderId);
        glDeleteProgram(program);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glEnableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDeleteBuffers(eboId);
        glDeleteBuffers(vboId);
        glDeleteVertexArrays(vaoId);
    }
}
