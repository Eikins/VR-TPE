package fr.lifok.tpe.vrtpe;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends Activity {

	private GLSurfaceView mGLView;
	public static RotationSensor rotationSensor;

	
	
	@Override
	protected void onCreate(Bundle bundle){
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		if (hasGLES20()) {
			mGLView = new GLSurfaceView(this);
			mGLView.setEGLContextClientVersion(2);
			mGLView.setRenderer(new VRRenderer());
		} else {
			// Time to get a new phone, OpenGL ES 2.0 not
			// supported.
		}
		rotationSensor = new RotationSensor((SensorManager) getSystemService(Context.SENSOR_SERVICE));

		
		setContentView(mGLView);
	}

	private boolean hasGLES20() {
		ActivityManager am = (ActivityManager)
				getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		return info.reqGlEsVersion >= 0x20000;
	}

	@Override
	protected void onResume() {
		super.onResume();
		/*
		 * The activity must call the GL surface view's
		 * onResume() on activity onResume().
		 */
		if (mGLView != null) {
			mGLView.onResume();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

		/*
		 * The activity must call the GL surface view's
		 * onPause() on activity onPause().
		 */
		if (mGLView != null) {
			mGLView.onPause();
		}
	}


}
