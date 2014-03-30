package com.example.locationsensing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;



public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
	private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private File file;
    private FileWriter filewriter;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationClient = new LocationClient(this, this, this);
        file = new File(Environment.getExternalStoragePublicDirectory(DOWNLOAD_SERVICE), "logfile1.txt");
        try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
        try {
			filewriter = new FileWriter(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        try {
        	filewriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        super.onStop();
    }



    
    public void senseGps(View v){
    	
    }
    
    public void senseWifi(View v){

    }
    
    public void senseBoth(View v){
    	mCurrentLocation = mLocationClient.getLastLocation();
    	Toast.makeText(this,"Location is:" + mCurrentLocation.getLatitude()+" "+mCurrentLocation.getLongitude()+" and accuracy is "+mCurrentLocation.getAccuracy(), Toast.LENGTH_SHORT).show();
    	System.out.println(mCurrentLocation.getLatitude());
    	try {
			filewriter.write("Location is:" + mCurrentLocation.getLatitude()+" "+mCurrentLocation.getLongitude()+" and accuracy is "+mCurrentLocation.getAccuracy());
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

    }

	@Override
	public void onConnected(Bundle arg0) {
		// Display the connection status
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
	}
}
