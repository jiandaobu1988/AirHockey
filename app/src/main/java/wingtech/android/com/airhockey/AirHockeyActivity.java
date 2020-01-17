package wingtech.android.com.airhockey;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.Toast;

public class AirHockeyActivity extends Activity {
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationinfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsES3 = configurationinfo.reqGlEsVersion >= 0x3000;//获取openGL版本
        if (supportsES3){
            glSurfaceView.setEGLContextClientVersion(3);
            glSurfaceView.setRenderer(new AirHorckeyRenderer(this));
            rendererSet = true;
        }else{
            Toast.makeText(this,"This device doesn't support OpenGL ES 3.0.",Toast.LENGTH_LONG).show();
            return;
        }
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(rendererSet){
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(rendererSet){
            glSurfaceView.onResume();
        }
    }
}
