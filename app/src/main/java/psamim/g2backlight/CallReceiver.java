package psamim.g2backlight;

import android.content.Context;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by samim on 1/27/18.
 */

public class CallReceiver extends PhonecallReceiver {
    private static final String TAG = CallReceiver.class.getName();
    private volatile static boolean blink = false;

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        Log.d(TAG, "onIncomingCallStarted: ");
        blink = true;
        Thread thread = new Thread() {
            @Override
            public void run() {
                blink();
            }
        };
        thread.start();
    }

    private void blink()  {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(p.getOutputStream());
            while (blink) {
                for (int i = 0; i < 100; i++) {
                    dos.writeBytes(
                            "    echo " + i + " > /sys/class/leds/button-backlight2/brightness\n" +
                                    "    echo " + i + " > /sys/class/leds/button-backlight1/brightness\n");
                }
                Thread.sleep(10);
                for (int i = 100; i > 0; i--) {
                    dos.writeBytes(
                            "    echo " + i + " > /sys/class/leds/button-backlight2/brightness\n" +
                                    "    echo " + i + " > /sys/class/leds/button-backlight1/brightness\n");
                }
                Thread.sleep(10);
                dos.flush();
            }
            dos.writeBytes("exit\n");
            dos.flush();
            dos.close();
            p.waitFor();
        } catch (IOException e) {
            Log.e(TAG, "onIncomingCallStarted: ", e);
        } catch (InterruptedException e) {
            Log.e(TAG, "onIncomingCallStarted: ", e);
        }
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.d(TAG, "onIncomingCallEnded: ");
        blink = false;
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(p.getOutputStream());
            dos.writeBytes("echo 0 > /sys/class/leds/button-backlight2/brightness; echo 0 > /sys/class/leds/button-backlight1/brightness\n");
            dos.writeBytes("exit\n");
            dos.flush();
            dos.close();
            p.waitFor();
        } catch (IOException e) {
            Log.e(TAG, "onIncomingCallEnded: ", e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {
        Log.d(TAG, "onMissedCall: ");
        blink = false;
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(p.getOutputStream());
            dos.writeBytes("echo 0 > /sys/class/leds/button-backlight2/brightness; echo 0 > /sys/class/leds/button-backlight1/brightness\n");
            dos.writeBytes("exit\n");
            dos.flush();
            dos.close();
            p.waitFor();
        } catch (IOException e) {
            Log.e(TAG, "onIncomingCallEnded: ", e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
