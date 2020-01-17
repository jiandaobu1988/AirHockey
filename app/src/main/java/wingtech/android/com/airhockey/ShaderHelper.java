package wingtech.android.com.airhockey;
import android.util.Log;

import static android.opengl.GLES30.GL_LINK_STATUS;
import static android.opengl.GLES30.glAttachShader;
import static android.opengl.GLES30.glCreateProgram;
import static android.opengl.GLES30.glDeleteProgram;
import static android.opengl.GLES30.glDeleteShader;
import static android.opengl.GLES30.glGetProgramInfoLog;
import static android.opengl.GLES30.glGetProgramiv;
import static android.opengl.GLES30.glLinkProgram;
import static android.opengl.GLES30.GL_COMPILE_STATUS;
import static android.opengl.GLES30.glCompileShader;
import static android.opengl.GLES30.glGetShaderInfoLog;
import static android.opengl.GLES30.glGetShaderiv;
import static android.opengl.GLES30.glShaderSource;
import static android.opengl.GLES30.GL_FRAGMENT_SHADER;
import static android.opengl.GLES30.GL_VERTEX_SHADER;
import static android.opengl.GLES30.glCreateShader;

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";
    public static int compileVertextShader(String shaderCode){
        return compileShader(GL_VERTEX_SHADER,shaderCode);
    }

    public static int compileFragmentShader(String shaderCode){
        return compileShader(GL_FRAGMENT_SHADER,shaderCode);
    }

    public static int  compileShader(int type,String shaderCode){
        final int shaderObjectId = glCreateShader(type);
        if(shaderObjectId == 0){
            if(LoggerConfig.ON){
                Log.w(TAG,"Could not create new shader.");
            }
        }
        glShaderSource(shaderObjectId,shaderCode);
        glCompileShader(shaderObjectId);
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId,GL_COMPILE_STATUS,compileStatus,0);
        if(LoggerConfig.ON){
            Log.v(TAG,"Results of compile source:"+"\n"+shaderCode+"\n"+glGetShaderInfoLog(shaderObjectId));
        }
        if(compileStatus[0] == 0){
            glDeleteShader(shaderObjectId);
            if(LoggerConfig.ON){
                Log.w(TAG,"Compilation of shader failed.");
            }
            return 0;
        }
        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId,int fragmentShaderId){
        final int programObjectId = glCreateProgram();
        if(programObjectId == 0){
            if(LoggerConfig.ON){
                Log.w(TAG,"Could not create new program.");
            }
            return 0;
        }
        glAttachShader(programObjectId,vertexShaderId);
        glAttachShader(programObjectId,fragmentShaderId);
        glLinkProgram(programObjectId);//链接程序
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId,GL_LINK_STATUS,linkStatus,0);
        if(LoggerConfig.ON){
            Log.v(TAG,"Results of linking program :\n"+glGetProgramInfoLog(programObjectId));
        }
        if(linkStatus[0] == 0){
            glDeleteProgram(programObjectId);
            if(LoggerConfig.ON){
                Log.w(TAG,"Linking of program failed.");
            }
            return 0;
        }
        return programObjectId;
    }
}
