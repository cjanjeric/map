package ph.gardenia.com.maptest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.internal.client.ThinAdSizeParcel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

//    https://github.com/googlemaps/android-samples/tree/master/ApiDemos

    private GoogleMap mMap;
    private Marker storeMarker;

    public CameraPosition GARDENIA;
    private static final double DEFAULT_RADIUS = 230;
    private Circle circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.`
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);


        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(final Location location) {

                GARDENIA = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude()))
                        .zoom(17.5f)
                        .bearing(0)
                        .tilt(25)
                        .build();

                if (mMap != null) {

                    changeCamera(CameraUpdateFactory.newCameraPosition(GARDENIA), new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {

                            LatLng store = new LatLng(location.getLatitude(), location.getLongitude());
                            if (storeMarker != null) {
                                storeMarker.remove();
                            }
                            storeMarker = mMap.addMarker(new MarkerOptions().position(store).title("New Store"));

                            if (circle != null){
                                circle.remove();
                            }

                            circle = mMap.addCircle(new CircleOptions()
                                    .center(store)
                                    .radius(DEFAULT_RADIUS)
                                    .strokeColor(0xffff0000)
                                    .fillColor(0x44ff0000));


                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (marker.equals(storeMarker)){
                    final AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
                    LinearLayout layout = new LinearLayout(MapsActivity.this);
                    LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setLayoutParams(parms);

                    final EditText txtCustomerDescription = new EditText(MapsActivity.this);
                    final EditText txtCustomerOwner = new EditText(MapsActivity.this);
                    final TextView txtCustomerAddress = new TextView(MapsActivity.this);

                    layout.setGravity(Gravity.CLIP_VERTICAL);
                    layout.setPadding(50, 2, 50, 2);

                    txtCustomerDescription.setHint("Description");
                    txtCustomerDescription.setTextColor(Color.BLACK);

                    txtCustomerOwner.setHint("Owner");
                    txtCustomerOwner.setTextColor(Color.BLACK);

                    txtCustomerOwner.setHint("Address");
                    txtCustomerOwner.setTextColor(Color.BLACK);

                    alert.setMessage("This mema store will add in our server");
                    alert.setTitle("Add Mema Store");

                    layout.addView(txtCustomerDescription);
                    layout.addView(txtCustomerOwner);
                    layout.addView(txtCustomerAddress);

                    alert.setView(layout);

                    alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {



                        }
                    });

                    alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.show();
                }

                return false;
            }
        });

    }

    private void changeCamera(CameraUpdate update, GoogleMap.CancelableCallback callback) {

        mMap.animateCamera(update, callback);

    }

}
