package com.mobilecomputing.alarmanlage2015.alarmanlageapp;

import BluetoothCommunication.BluetoothCommunicator;
import BluetoothCommunication.Controller;

/**
 * Created by Jan Urbansky on 22.02.2016.
 */
public class AppController {

    private BluetoothCommunicator mBluetoothCommunicator;


    public AppController(BluetoothCommunicator mBluetoothCommunicator) {
        this.mBluetoothCommunicator = mBluetoothCommunicator;
    }

    public void sendMessage(String deviceID){
        mBluetoothCommunicator.sendToDevice(deviceID);
    }


    public void bluetoothAdapterEnabled(){

    }
    public void discoverabilityEnabled(){

    }
}