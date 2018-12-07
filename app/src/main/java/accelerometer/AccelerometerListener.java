package accelerometer;

/**
 * Created by Richard.Ezama on 24/05/2016.
 */
public interface AccelerometerListener {
    public void onAccelerationChanged(float x, float y, float z);
    public void onShake(float force);
}
