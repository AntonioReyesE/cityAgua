package com.aguayciudad.aguas.app;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.kinvey.android.AsyncAppData;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyListCallback;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;

public class MapsActivity extends FragmentActivity implements LocationListener {

    public final static String EXTRA_MESSAGE = "com.example.antonioreyes.aguas.message";
    public static LatLng center;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private Client mKinveyClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMap = supportMapFragment.getMap();
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }
        //locationManager.requestLocationUpdates(bestProvider, 10000, 0,this); //Returns marker to current position in a defined time
        setUpMapIfNeeded();

        getReports();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //TextView locationTv = (TextView) findViewById(R.id.latlongLocation);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
       // mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //Toast.makeText(this, latLng+" ",Toast.LENGTH_LONG).show();
        center = latLng;
       // locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
       // mMap.addMarker(new MarkerOptions().position(new LatLng(20.673792, -103.3354131)).title("Soy un marcador bien chido!!"));
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(20.673792, -103.3354131),12));

       // mMap.setOnMyLocationChangeListener(myLocationChangeListener);

         //center = mMap.getCameraPosition().target;

        //Toast.makeText(this,center+" ",Toast.LENGTH_SHORT).show();
    }


    public void make_report(View view){
        Intent intent = new Intent(this, MainActivity.class);
       // mMap.setOnMyLocationChangeListener(myLocationChangeListener);
        center = mMap.getCameraPosition().target;
       //center = mMap.getCameraPosition().target;

        Globals.latitude = center.latitude;
        Globals.longitude = center.longitude;

       // this.onLocationChanged(mMap.getMyLocation());

        startActivity(intent);
    }

    public void emergencias(View view){
        Intent intent = new Intent(this, Emergencias.class);
        startActivity(intent);
    }

    public void showInformacion(View view){
        Intent intent = new Intent(this, Informacion.class);
        startActivity(intent);
    }

    public void setMarkers(Report[] list){

        for (Report temp : list) {

            //Log.d("OBJECT", temp.getString("Tipo_Reporte"));
            //Log.d("OBJECT", String.valueOf(temp.getDouble("Latitud")) );
            //Log.d("OBJECT", String.valueOf(temp.getDouble("Longitud")) );

            String tipo = temp.getTipo_Reporte();
            double lat = temp.getLatitud();
            double lon = temp.getLongitud();
            String comentario = temp.getComentario();
            LatLng coordenadas = new LatLng(lat, lon);
            int img = 0;
            switch (tipo){
                case "Falta de Agua":
                    img = R.drawable.marcadores_03;
                    break;
                case "Agua contaminada":
                    img = R.drawable.marcadores_02;
                    break;
                case "Inundaciones":
                    img = R.drawable.marcadores_01;
                    break;
                case "Encharcamientos":
                    img = R.drawable.marcadores_04;
                    break;
                case "Fugas de agua":
                    img = R.drawable.marcadores_05;
                    break;
                case "Deslaves":
                    img = R.drawable.marcadores_06;
                    break;
                case "Socavamientos":
                    img = R.drawable.marcadores_07;
                    break;
                case "Infraestructura":
                    img = R.drawable.marcadores_08;
                    break;
                default:
                    img = R.drawable.marcadores_01;
            }
            MarkerOptions mo = new MarkerOptions();
            mo.position(coordenadas);
            mo.title(tipo);
            if(comentario != null){
                mo.snippet(comentario);
            }
            mo.icon(BitmapDescriptorFactory.fromResource(img));
            if(mo != null){
                mMap.addMarker(mo);
            }
        }
    }

    public void getReports(){
        mKinveyClient = new Client.Builder("kid_SktNtZOX", "65031a1aa45c4363a57c4369304843a9"
                , this.getApplicationContext()).build();

        mKinveyClient.user().login(new KinveyUserCallback() {
            @Override
            public void onFailure(Throwable error) {

            }
            @Override
            public void onSuccess(User result) {
                
            }
        });
        AsyncAppData<Report> reports = mKinveyClient.appData("Report", Report.class);
        reports.get(new KinveyListCallback<Report>() {
            @Override
            public void onSuccess(Report[] result) {
                setMarkers(result);
            }
            @Override
            public void onFailure(Throwable error)  {
                Log.e("TAG", "failed to fetch all", error);
            }
        });

    }


}
