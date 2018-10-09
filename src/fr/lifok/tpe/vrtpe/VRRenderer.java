package fr.lifok.tpe.vrtpe;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;
import fr.lifok.tpe.vrtpe.shapes.Square;
import fr.lifok.tpe.vrtpe.shapes.Triangle;

public class VRRenderer implements Renderer {


	private static String TAG;
	private final float[] mMVPMatrix = new float[16];
	private final float[] mProjectionMatrix = new float[16];
	private final float[] mViewMatrix = new float[16];
	private Triangle triangle1;
	private Square square1;
	private Square square2;
	private Triangle triangle2;
	private Square square3;
	private Square square4;


	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);

		float[] triangle1Coords = new float[]{   // in counterclockwise order:
				0.0f,  0.577350269f + 2f, 0.0f, // top
				-0.5f, -0.288675134f + 2f, 0.0f, // bottom left
				0.5f, -0.288675134f + 2f, 0.0f  // bottom right
		};
		float[] triangle1Color = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f};
		triangle1 = new Triangle(triangle1Coords, triangle1Color);

		float[] triangle2Coords = new float[]{   // in counterclockwise order:
				-0.5f,  -4f, 0.0f, // top
				0.5f, -3f, 0.0f, // bottom left
				-0.5f, -2f, 0.0f  // bottom right
		};
		float[] triangle2Color = {1f, 0.317647058f, 0.874509803f, 1.0f};


		triangle1 = new Triangle(triangle1Coords, triangle1Color);
		triangle2 = new Triangle(triangle2Coords, triangle2Color);

		float[] square1Coords  = { -0.5f, 0.5f, 0.0f,   // top left
				-0.5f, -0.5f, 0.0f,   // bottom left
				0.5f, -0.5f, 0.0f,   // bottom right
				0.5f,  0.5f, 0.0f }; // top right
		float[] square1Color  = { 1.0f, 0.5f, 0.22265625f, 1.0f};
		square1 = new Square(square1Coords, square1Color);

		float[] square2Coords  = { -0.5f, 4.5f, 0.0f,   // top left
				-0.5f, 3.5f, 0.0f,   // bottom left
				0.5f, 3.5f, 0.0f,   // bottom right
				0.5f,  4.5f, 0.0f }; // top right
		float[] square2Color  = { 0.275f, 0.75f, 1.0f, 1.0f};
		square2 = new Square(square2Coords, square2Color);
		
		float[] square3Coords  = { 1.5f, 0.5f, 0.0f,   // top left
				1.5f, -0.5f, 0.0f,   // bottom left
				2.5f, -0.5f, 0.0f,   // bottom right
				2.5f,  0.5f, 0.0f }; // top right
		float[] square3Color  = { 1f, 0.87f, 0.41f, 1.0f};
		square3 = new Square(square3Coords, square3Color);
		
		float[] square4Coords  = { -1.5f, 0.5f, 0.0f,   // top left
				-1.5f, -0.5f, 0.0f,   // bottom left
				-2.5f, -0.5f, 0.0f,   // bottom right
				-2.5f,  0.5f, 0.0f }; // top right
		float[] square4Color  = { 1.0f, 0.65f, 1.0f, 1.0f};
		square4 = new Square(square4Coords, square4Color);

	}

	public void onDrawFrame(GL10 unused) {
		//  float[] scratch = new float[16];

		// Draw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		// Set the camera position (View matrix)
		Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -5, MainActivity.rotationSensor.getAngleY() * 2 - 3f, (float)(10 * MainActivity.rotationSensor.getAngleZ() / Math.PI), 0f, 0f, 1.0f, 0.0f);

		// Calculate the projection and view transformation
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
			square1.draw(mMVPMatrix);
			triangle1.draw(mMVPMatrix);
			square2.draw(mMVPMatrix);
			triangle2.draw(mMVPMatrix);
			square3.draw(mMVPMatrix);
			square4.draw(mMVPMatrix);

			for(int x = 0; x < 3; x++){
				triangle1.triangleColor[x] /= 2;
				triangle2.triangleColor[x] /= 2;
				square1.color[x] /= 2;
				square2.color[x] /= 2;
				square3.color[x] /= 2;
				square4.color[x] /= 2;
			}
			Matrix.translateM(mMVPMatrix, 0, 0, 0f, 0.5f);
			square1.draw(mMVPMatrix);
			triangle1.draw(mMVPMatrix);
			square2.draw(mMVPMatrix);
			triangle2.draw(mMVPMatrix);
			square3.draw(mMVPMatrix);
			square4.draw(mMVPMatrix);

			for(int x = 0; x < 3; x++){
				triangle1.triangleColor[x] *= 2;
				triangle2.triangleColor[x] *= 2;
				square1.color[x] *= 2;
				square2.color[x] *= 2;
				square3.color[x] *= 2;
				square4.color[x] *= 2;
			}
		//	Matrix.translateM(mMVPMatrix, 0, 0, 0, -0.5f);
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;

		// this projection matrix is applied to object coordinates
		// in the onDrawFrame() method
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

	}

	/**
	 * Utility method for debugging OpenGL calls. Provide the name of the call
	 * just after making it:
	 *
	 * <pre>
	 * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
	 * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
	 *
	 * If the operation is not successful, the check throws an error.
	 *
	 * @param glOperation - Name of the OpenGL call to check.
	 */
	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}
}
