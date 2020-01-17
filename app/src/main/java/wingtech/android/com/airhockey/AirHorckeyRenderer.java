package wingtech.android.com.airhockey;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES30.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES30.glClear;
import static android.opengl.GLES30.glClearColor;
import static android.opengl.GLES30.glVertexAttribIPointer;
import static android.opengl.GLES30.glViewport;
import android.opengl.Matrix;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AirHorckeyRenderer implements GLSurfaceView.Renderer {
    private static final int POSSITION_COMPONENT_COUNT = 4;
    private static final int BYTES_PER_FLOAT = 4;
    private  final FloatBuffer vertexData ;
    private  Context context = null;
    private int program;
    private static final String A_COLOR = "a_Color";
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT)*BYTES_PER_FLOAT;
    private int aColorLocation;
    private int uColorLocation;
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;
    private static final String U_MATRIX = "u_Matrix";
    private final float[] projectionMatrix = new float[16];
    private int uMatrixLocation;
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f,0.0f,0.0f,0.0f);//清除颜色设置屏幕为黑色
        String vertexShaderSource = TextResourceReader.readTextFromResource(context,R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFromResource(context,R.raw.simple_fragment_shader);
        int vertexShader = ShaderHelper.compileVertextShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        program = ShaderHelper.linkProgram(vertexShader,fragmentShader);
        glUseProgram(program);
        aColorLocation = glGetAttribLocation(program,A_COLOR);
        aPositionLocation = glGetAttribLocation(program,A_POSITION);
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation,POSSITION_COMPONENT_COUNT,GL_FLOAT,false,STRIDE,vertexData);
        glEnableVertexAttribArray(aPositionLocation);
        vertexData.position(POSSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GL_FLOAT,false,STRIDE,vertexData);
        glEnableVertexAttribArray(aColorLocation);
        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0,0,width,height);
        final float aspectRatio = width > height?(float)width/(float)height:(float)height/(float)width;
        if(width>height){
           Matrix.orthoM(projectionMatrix,0,-aspectRatio,aspectRatio,-1f,1f,-1f,1f);
        }else {
           Matrix.orthoM(projectionMatrix,0,-1f,1f,-aspectRatio,aspectRatio,-1f,1f);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);//实际完成了设置屏幕为黑色的任务
        //glUniform4f(uColorLocation,1.0f,1.0f,1.0f,1.0f);
        glDrawArrays(GL_TRIANGLE_FAN,0,6);
        //glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
        glDrawArrays(GL_LINES,6,2);
        //glUniform4f(uColorLocation,0.0f,0.0f,1.0f,1.0f);
        glDrawArrays(GL_POINTS,8,1);
        //glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f);
        glDrawArrays(GL_POINTS,9,1);
        glUniformMatrix4fv(uMatrixLocation,1,false,projectionMatrix,0);
    }

    public AirHorckeyRenderer(Context context){
        this.context = context;
        //构造函数中定义曲棍球桌子的四个顶点
        float tableVertices[] = {
                0f,0f,
                0f,14f,
                9f,14f,
                9f,0,
        };
        float tableVerticesWithTriangles[] = {
                //Triangle of Fam
                //Order of coordinanates :X,Y,R,G,B
                0f,0f,0f,1.5f,1f,1f,1f,
                -0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
                0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
                0.5f,0.8f,0f,2f,0.7f,0.7f,0.7f,
                -0.5f,0.8f,0f,2f,0.7f,0.7f,0.7f,
                -0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
                //Line 1
                -0.5f,0f,0f,1.5f,1f,0f,0f,
                0.5f,0f,0f,1.5f,1f,0f,0f,
                //Mallets
                0f,-0.4f,0f,1.25f,0f,0f,1f,
                0f,0.4f,0f,1.75f,1f,0f,0f,
        };
        float tableVerticesWithTriangles1[] = {
                //Triangle 1
                -0.5f,-0.5f,
                0.5f,0.5f,
                -0.5f,0.5f,
                //Triangle 2
                -0.5f,-0.5f,
                0.5f,-0.5f,
                0.5f,0.5f,
                //Line 1
                -0.5f,0f,
                0.5f,0f,
                //Mallets
                0f,-0.25f,
                0f,0.25f,
        };
        float tableVerticesWithTriangles2[] = {
                //Triangle 1
                0f,0f,
                9f,14f,
                0f,14f,
                //Triangle 2
                0f,0f,
                9f,0f,
                9f,14f,
                //Line 1
                0f,7f,
                9f,7f,
                //Mallets
                4.5f,2f,
                4.5f,12f,
        };
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangles.length*BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(tableVerticesWithTriangles);
    }
}
