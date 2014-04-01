package com.example.locationsensing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * 
 * @author Prafulla
 *
 */
public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {
	private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private File file;
    private FileWriter filewriter;

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    // Define an object that holds accuracy and frequency parameters
    private LocationRequest mLocationRequest;
    int count = 0;
    final String DOWNLOAD_FILE = "http://web.mit.edu/21w.789/www/papers/griswold2004.pdf ";
    private long enqueue;
    private DownloadManager dm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationClient = new LocationClient(this, this, this);
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        count +=1;
        file = new File(Environment.getExternalStoragePublicDirectory(DOWNLOAD_SERVICE), "logfiles"+count+".txt");
        try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
        mLocationClient.disconnect();
        try {
        	filewriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        super.onStop();
    }

    @SuppressLint("NewApi")
	public void onDownloadClick(View v){
    	dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Request request = new Request(
                Uri.parse(DOWNLOAD_FILE));
        enqueue = dm.enqueue(request);
    }
    
    @SuppressLint("InlinedApi")
	public void downloadStatus(View v){
    	Toast.makeText(this, "Status: "+DownloadManager.COLUMN_STATUS+" Downloaded: "+DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR, Toast.LENGTH_SHORT).show();
    }
    @SuppressLint("InlinedApi")
	public void showDownload(View view) {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }

    public void sensenNetworkProvider(View v){
    	Toast.makeText(this, "Using Network Provider", Toast.LENGTH_SHORT).show();
    	mLocationRequest.setPriority(
                LocationRequest.PRIORITY_NO_POWER);
    	mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    public void senseLeastAccuracy(View v){
    	Toast.makeText(this, "Hmm", Toast.LENGTH_SHORT).show();
    	mLocationRequest.setPriority(
                LocationRequest.PRIORITY_NO_POWER);
    	mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }
    
    public void senseLowerAccuracy(View v){
    	Toast.makeText(this, "Hmm", Toast.LENGTH_SHORT).show();
    	mLocationRequest.setPriority(
                LocationRequest.PRIORITY_LOW_POWER);
    	mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }
    
    public void senseLowAccuracy(View v){
    	Toast.makeText(this, "Hmm", Toast.LENGTH_SHORT).show();
    	mLocationRequest.setPriority(
                LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    	mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }
    
    public void senseHighAccuracy(View v){
    	Toast.makeText(this, "Hmm", Toast.LENGTH_SHORT).show();
    	mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
    	mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }
    
    public void senseLastLocation(View v){
    	mCurrentLocation = mLocationClient.getLastLocation();
    	Toast.makeText(this,"Location is:" + mCurrentLocation.getLatitude()+" "+mCurrentLocation.getLongitude()+" and accuracy is "+mCurrentLocation.getAccuracy(), Toast.LENGTH_SHORT).show();
    	System.out.println(mCurrentLocation.getLatitude());
    	try {
			filewriter.write("Location is:" + mCurrentLocation.getLatitude()+" "+mCurrentLocation.getLongitude()+" and accuracy is "+mCurrentLocation.getAccuracy()+"\n");
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

	@Override
	public void onLocationChanged(Location location) {
		// Report to the UI that the location was updated
		if(location!=null){
			String msg = "Updated Location: " +
						Double.toString(location.getLatitude()) + "," +
						Double.toString(location.getLongitude())+ " and accuracy is "+location.getAccuracy();
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			try {
				filewriter.write("Updated Location: " +
						Double.toString(location.getLatitude()) + "," +
						Double.toString(location.getLongitude())+ " and accuracy is "+location.getAccuracy()+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			Toast.makeText(this, "Trying", Toast.LENGTH_SHORT).show();
		}
	}
}
