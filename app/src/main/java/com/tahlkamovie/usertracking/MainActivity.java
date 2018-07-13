package com.tahlkamovie.usertracking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tv1, tv2, tv3;
    Button btn, btn2;
    LocationManager lm;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        btn = (Button) findViewById(R.id.button);

        btn2 = (Button) findViewById(R.id.button2);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;

        }

        database = FirebaseDatabase.getInstance();

        reference = database.getReference("Location_Updates");

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);


        btn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                try {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {

                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();


                            tv1.setText("latitude :-" + latitude);
                            tv2.setText("\n longitude:-" + longitude);

                            Geocoder geocoder = new Geocoder(MainActivity.this);
                            try {
                                List<Address> adr = geocoder.getFromLocation(latitude, longitude, 1);
                                String country = adr.get(0).getCountryName();
                                //String countryCode=adr.get(0).getCountryCode();
                                String locality = adr.get(0).getLocality();
                                String postalCode = adr.get(0).getPostalCode();
                                String address = adr.get(0).getAddressLine(0);

                                tv3.setText("\n" + country + "," + locality + "," + address + "," + postalCode);

                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                }catch (Exception e){

                }
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String latitude=tv1.getText().toString();
                String longitude=tv2.getText().toString();

                Score score1=new Score();
                score1.setLatitud(latitude);
                score1.setLongitud(longitude);

                reference.setValue(score1);

                //tv1.setText("");
                //tv2.setText("");

                Toast.makeText(MainActivity.this, "Location Send on Server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
