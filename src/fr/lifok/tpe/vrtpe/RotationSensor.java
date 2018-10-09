package fr.lifok.tpe.vrtpe;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class RotationSensor implements SensorEventListener{

	private float[] mValuesMagnet      = new float[3];
	private float[] mValuesAccel       = new float[3];
	private static float[] mValuesOrientation = new float[3];
	private float[] mRotationMatrix    = new float[9];
	private Sensor mGeomagnet;
	private Sensor mAccel;
	private SensorManager sensorManager;
	private static final float ALPHA = 0.15f;
	
	public RotationSensor(SensorManager manager) {
		sensorManager = manager;
	    mGeomagnet = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	    mAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    manager.registerListener(this, mGeomagnet, SensorManager.SENSOR_DELAY_FASTEST);
	    manager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_FASTEST);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		// Handle the events for which we registered
		switch (event.sensor.getType()) {           
		case Sensor.TYPE_ACCELEROMETER: 
			mValuesAccel = lowPass(event.values, mValuesAccel);
			break; 

		case Sensor.TYPE_MAGNETIC_FIELD: 
			mValuesMagnet = lowPass(event.values, mValuesMagnet);
			break; 
		}
		SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
		SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);  
	}
	
	protected float[] lowPass( float[] input, float[] output ) {
	    if ( output == null ) return input;

	    for ( int i=0; i<input.length; i++ ) {
	        output[i] = output[i] + ALPHA  * (input[i] - output[i]);
	    }
	    return output;
	}
	
	protected float[] highPass(float[] input, float[] output){
		
		output[0] = input[0] * ALPHA + output[0] * (1.0f - ALPHA);
		output[1] = input[1] * ALPHA + output[1] * (1.0f - ALPHA);
		output[2] = input[2] * ALPHA + output[2] * (1.0f - ALPHA);
	/*	output[0] = input[0] - accel[0];
		output[1] = input[1] - accel[1];
		output[2] = input[2] - accel[2];*/
		
		return output;
	}
	
	public float getAngleZ(){
		return mValuesOrientation[0];
	}
	
	public float getAngleX(){
		return mValuesOrientation[1];
	}
	
	public float getAngleY(){
		return mValuesOrientation[2];
	}
	
}
