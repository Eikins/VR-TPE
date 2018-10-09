package fr.lifok.tpe.vrtpe;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class VRGLSurfaceView extends GLSurfaceView {

	public VRGLSurfaceView(Context context){
		super(context);
		setEGLContextClientVersion(2);
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		setRenderer(new VRRenderer());
	}

}
