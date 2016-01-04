package com.mobilecomputing.alarmanlage2015.alarmanlageapp;


import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Jan Urbansky on 19.12.2015.
 * <p/>
 * Übernommen aus dem RFCOMM-Server Projekt und unserem Projekt angepasst.
 */
public class ConnectedThread extends Thread {

    public final static String TAG = "fhflConnectedThread";
    private Controller mController;
    private final BluetoothSocket mSocket;
    private final InputStream mInStream;
    private final OutputStream mOutStream;

    /**
     * @param socket
     * @param controller
     */
    public ConnectedThread(BluetoothSocket socket, Controller controller) {
        mSocket = socket;
        mController = controller;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        debugOut("Konstruktor");

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            debugOut(e.getMessage());
            debugOut("ConnectedThread(): Error: IOException during get streams !!!");

        }

        mInStream = tmpIn;
        mOutStream = tmpOut;


        try {
            mInStream.available();
        } catch (IOException e) {
            debugOut("mInStream not available");
            debugOut("Error: " + e.getMessage());
        }
    }


    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        debugOut("run()");


        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                bytes = mInStream.read(buffer);
                mController.obtainMessage(Controller.SmMessage.CT_RECEIVED.ordinal(), bytes, -1, buffer)
                        .sendToTarget(); // obtain...(): delivers 'empty' message-object from a pool
            } catch (IOException e) {
                debugOut("run(): IOException during read stream");
                e.printStackTrace();
                debugOut("Error: " + e.getMessage());

                mController.obtainMessage(Controller.SmMessage.CT_CONNECTION_CLOSED.ordinal(), -1, -1, null).sendToTarget();
                break;
            }
        }
        debugOut("run(): thread terminates");
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            String sendStr = new String(bytes);
            debugOut("write(" + sendStr + ")");

            mOutStream.write(bytes);
        } catch (IOException e) {
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        debugOut("cancel()");
        try {
            mSocket.close();
        } catch (IOException e) {
        }
    }

    private void debugOut(String str) {
        //C-ID steht für Connecton ID und ist ist die Thread ID.
        str = "C-ID "+this.getId()+ ": "+str;
        mController.obtainMessage(Controller.SmMessage.CT_DEBUG.ordinal(), -1, -1, str).sendToTarget();
    }

    ;
}
