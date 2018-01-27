package psamim.g2backlight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(p.getOutputStream());
            dos.writeBytes("exit\n");
            dos.flush();
            dos.close();
            p.waitFor();
        } catch (IOException e) {
            Log.e(TAG, "onCreate: Su not granted", e);
        } catch (InterruptedException e) {
            Log.e(TAG, "onCreate: ", e);
        }
    }
}
