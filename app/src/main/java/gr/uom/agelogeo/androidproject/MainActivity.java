package gr.uom.agelogeo.androidproject;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener , LocationListener , NavigationView.OnNavigationItemSelectedListener{
    int adult_p = 1 , kid_p = 0 , baby_p = 0 , max_p = 9;
    EditText departureText;
    EditText returnText ;
    String lat = "!";
    String lng = "!";
    GoogleApiClient mGoogleApiClient = null;
    EditText fromText,destText,passengersText;
    TextView passengersNumber;
    ImageButton swapAirports,clearReturnDate;
    Switch directflightswitch;
    DatePickerDialog returndialog = null;
    Button searchflightsbtn ;
    int year_x,month_x,day_x,year_d=-1,month_d=-1,day_d=-1;
    static final int DEPARTURE_DATE_ID = 0;
    static final int ARRIVAL_DATE_ID = 1;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.content_main);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        GPSLocationPermissionRequest();

        returnText = (EditText) findViewById(R.id.returnDate);
        departureText = (EditText) findViewById(R.id.departureDate);
        passengersText = (EditText) findViewById(R.id.passengersText);
        passengersNumber = (TextView) findViewById(R.id.passengersNumber);
        directflightswitch = (Switch) findViewById(R.id.directflightswitch);
        searchflightsbtn = (Button)findViewById(R.id.search_flights_btn);
        final Calendar cal = Calendar.getInstance();
        year_x=cal.get(Calendar.YEAR);
        month_x=cal.get(Calendar.MONTH);
        day_x=cal.get(Calendar.DAY_OF_MONTH);

        showDialogOnClick();
        Listeners();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }




    public void Listeners(){
        fromText = (EditText) findViewById(R.id.fromText);
        fromText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SearchAirportActivity.class);
                i.putExtra("lat",lat);
                i.putExtra("lng",lng);
                startActivityForResult(i, 1);
            }
        });

        destText = (EditText) findViewById(R.id.destText);
        destText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SearchAirportActivity.class);
                i.putExtra("lat",lat);
                i.putExtra("lng",lng);
                startActivityForResult(i, 2);
             }
        });

        swapAirports = (ImageButton) findViewById(R.id.swapAirports);
        swapAirports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = fromText.getText().toString();
                fromText.setText(destText.getText().toString());
                destText.setText(temp);
            }
        });

        clearReturnDate = (ImageButton) findViewById(R.id.clearReturnDate);
        clearReturnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(returnText.getText().length()!=0)
                    returnText.getText().clear();
            }
        });


        passengersText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle(R.string.passenger_selection_title);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.show();
                DialogListeners(dialog);
            }
        });

        searchflightsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fromText.getText().length()==0 || destText.getText().length()==0  || departureText.getText().length()==0 )
                    Toast.makeText(MainActivity.this,getString(R.string.search_toast_empty), Toast.LENGTH_SHORT).show();
                else if(fromText.getText().toString().equals(destText.getText().toString()))
                    Toast.makeText(MainActivity.this, R.string.search_toast_same, Toast.LENGTH_SHORT).show();
                else {
                    Intent i = new Intent(MainActivity.this, SearchFlights.class);
                    i.putExtra("origin", fromText.getText().subSequence(fromText.getText().length()-4,fromText.getText().length()-1).toString());
                    i.putExtra("destination", destText.getText().subSequence(destText.getText().length()-4,destText.getText().length()-1).toString());
                    i.putExtra("departure_date", departureText.getText().toString());
                    i.putExtra("return_date", returnText.getText().toString());
                    i.putExtra("adults", adult_p);
                    i.putExtra("children", kid_p);
                    i.putExtra("infants", baby_p);
                    i.putExtra("nonstop", directflightswitch.isChecked());
                    Spinner ticket_plan = (Spinner) findViewById(R.id.ticket_plan);
                    switch(ticket_plan.getSelectedItemPosition()){
                        case 0:
                            i.putExtra("travel_class", "ECONOMY");
                            break;
                        case 1:
                            i.putExtra("travel_class", "PREMIUM_ECONOMY");
                            break;
                        case 2:
                            i.putExtra("travel_class", "BUSINESS");
                            break;
                        case 3:
                            i.putExtra("travel_class", "FIRST");
                            break;
                        default:
                            i.putExtra("travel_class", "economy");
                    }
                    startActivity(i);
                }
            }
        });

    }


    public void DialogListeners(final Dialog dialog){
        final ImageButton adult_minus = (ImageButton) dialog.findViewById(R.id.adult_minusBtn);
        final ImageButton kid_minus = (ImageButton) dialog.findViewById(R.id.kid_minus);
        final ImageButton baby_minus = (ImageButton) dialog.findViewById(R.id.baby_minus);

        final ImageButton adult_plus = (ImageButton) dialog.findViewById(R.id.adult_plusBtn);
        final ImageButton kid_plus = (ImageButton) dialog.findViewById(R.id.kid_plus);
        final ImageButton baby_plus = (ImageButton) dialog.findViewById(R.id.baby_plus);

        final TextView adult_text = (TextView) dialog.findViewById(R.id.adult_text);
        final TextView kid_text = (TextView) dialog.findViewById(R.id.kid_text);
        final TextView baby_text = (TextView) dialog.findViewById(R.id.baby_text);

        final TextView kidage = (TextView) dialog.findViewById(R.id.kid_age);
        final ImageView kidIcon = (ImageView) dialog.findViewById(R.id.kid_icon) ;
        final TextView babyage = (TextView) dialog.findViewById(R.id.baby_age);
        final ImageView babyIcon = (ImageView) dialog.findViewById(R.id.baby_icon) ;

        adult_text.setText(String.valueOf(adult_p));
        kid_text.setText(String.valueOf(kid_p));
        baby_text.setText(String.valueOf(baby_p));

        if(adult_p > 1)
            adult_minus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
        if(adult_p == 1)
            adult_minus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
        if(adult_p == max_p)
            adult_plus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
        if(kid_p > 0) {
            kid_minus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
            kidIcon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorBlack));
            kidage.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorBlack));
            kid_text.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorBlack));
        }if(kid_p + adult_p == max_p)
            kid_plus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
        if(baby_p>0) {
            babyIcon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorBlack));
            babyage.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorBlack));
            baby_text.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorBlack));
            baby_minus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
        }if(baby_p == adult_p)
            baby_plus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));




        adult_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidMove(adult_p+1,kid_p,baby_p)){
                    if(adult_p+1+kid_p>max_p) {
                        kid_p--;
                        kid_text.setText(String.valueOf(kid_p));
                        if(kid_p==0) {
                            kidIcon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                            kid_text.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                            kidage.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                            kid_minus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                        }
                    }
                    if (adult_p+1+kid_p==max_p)
                        kid_plus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                    adult_p++;
                    adult_text.setText(String.valueOf(adult_p));
                    if(adult_p>1)
                        adult_minus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                    if(adult_p==max_p)
                        adult_plus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                    if(adult_p>baby_p)
                        baby_plus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                }
            }
        });

        adult_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidMove(adult_p - 1, kid_p, baby_p) || isValidMove(adult_p - 1, kid_p, baby_p-1)) {
                        if(baby_p>adult_p-1) {
                            baby_p--;
                            baby_text.setText(String.valueOf(baby_p));
                            baby_plus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                        }
                        kid_plus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                        if(baby_p==adult_p-1)
                            baby_plus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                        adult_p--;
                        adult_text.setText(String.valueOf(adult_p));
                        if (adult_p == 1)
                            adult_minus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                        if (adult_p < max_p)
                            adult_plus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                }
            }
        });

        kid_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidMove(adult_p,kid_p+1,baby_p)){
                    if(kid_p+1+adult_p<=max_p) {
                        kid_p++;
                        kid_text.setText(String.valueOf(kid_p));
                        if (kid_p > 0) {
                            kidIcon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorBlack));
                            kidage.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorBlack));
                            kid_text.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorBlack));
                            kid_minus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                        }
                        if (kid_p + adult_p == max_p)
                            kid_plus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                    }
                }
            }
        });

        kid_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidMove(adult_p , kid_p - 1 , baby_p)){
                    kid_p--;
                    kid_text.setText(String.valueOf(kid_p));
                    if (kid_p == 0) {
                        kidIcon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                        kidage.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                        kid_text.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                        kid_minus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                    }
                    if (kid_p+adult_p < max_p)
                        kid_plus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                }
            }
        });

        baby_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidMove(adult_p,kid_p,baby_p+1)){
                    baby_p++;
                    baby_text.setText(String.valueOf(baby_p));
                    if (baby_p == adult_p)
                        baby_plus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                    if ( baby_p > 0) {
                        babyIcon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorBlack));
                        babyage.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorBlack));
                        baby_text.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorBlack));
                        baby_minus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                    }
                }
            }
        });

        baby_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidMove(adult_p,kid_p,baby_p-1)) {
                    baby_p--;
                    baby_text.setText(String.valueOf(baby_p));
                    if(baby_p<adult_p)
                        baby_plus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                    if(baby_p==0) {
                        babyIcon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                        babyage.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                        baby_text.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                        baby_minus.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colordarkgray));
                    }
                }
            }
        });

        Button confirmbtn = (Button) dialog.findViewById(R.id.confirmBtn);
        confirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((adult_p+kid_p+baby_p)!=1) {
                    passengersText.setText(getString(R.string.passengers_plurar));
                    passengersNumber.setText(String.valueOf(adult_p + kid_p + baby_p));
                }else {
                    passengersText.setText(getString(R.string.passengers_single));
                    passengersNumber.setText(String.valueOf(adult_p + kid_p + baby_p));
                }
                dialog.dismiss();
            }
        });
    }

    public boolean isValidMove(int adult, int kid, int baby){
        if(baby > adult)
            return false;
        if(adult>max_p)
            return false;
        if(kid>0 && adult==0)
            return false;
        if(kid>=max_p)
            return false;
        if(adult<=0 || kid<0 || baby<0)
            return false;

        return true;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String stredittext=data.getStringExtra("getSelectedItem");
                fromText.setText(stredittext);
            }
        }
        else if (requestCode == 2) {
            if(resultCode == RESULT_OK){
                String stredittext=data.getStringExtra("getSelectedItem");
                destText.setText(stredittext);
            }
        }
    }

    public void showDialogOnClick(){
        departureText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                try {
                    if(departureText.getText().length()!=0) {
                        Date departureD = sdf.parse(s.toString());
                        if(returnText.getText().length()!=0) {
                            Date returnD = sdf.parse(returnText.getText().toString());
                            if(departureD.after(returnD))
                                returnText.setText("");
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {            }
        });

        returnText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                try {
                    if(returnText.getText().length()!=0) {
                        Date returnD = sdf.parse(s.toString());
                        Date departureD = sdf.parse(departureText.getText().toString());
                            if (returnD.before(departureD))
                                departureText.setText("");
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {            }
        });

        departureText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(DEPARTURE_DATE_ID);

            }
        });
        returnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(departureText.getText().length()!=0) {
                    removeDialog(ARRIVAL_DATE_ID);
                    showDialog(ARRIVAL_DATE_ID);
                }else
                    Toast.makeText(MainActivity.this, R.string.chooseDepartDateFirst, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected Dialog onCreateDialog(int id){
        if ( id == DEPARTURE_DATE_ID ) {
            DatePickerDialog dialog = new DatePickerDialog(this, dpickerListner, year_x, month_x, day_x);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            return dialog;
        }else if ( id == ARRIVAL_DATE_ID ) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            Calendar calendar = Calendar.getInstance();
            try {
                Date d = formatter.parse(departureText.getText().toString());
                calendar.setTime(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            returndialog = new DatePickerDialog(this, dpickerListner2, year_d, month_d, day_d);
            returndialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

            return returndialog;
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener dpickerListner = new DatePickerDialog.OnDateSetListener() {

        @Override

        public void onDateSet(DatePicker datePicker, int year, int month, int day ) {
            Calendar cal = Calendar.getInstance();
            cal.set(year,month,day);
            Date date = cal.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            departureText.setText(formatter.format(date));

            year_d = year;
            month_d = month;
            day_d = day;
        }
    };

    private DatePickerDialog.OnDateSetListener dpickerListner2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(year,month,day);
            Date date = cal.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            returnText.setText(formatter.format(date));
    }
};

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(3000);
           /* FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;*/

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,locationRequest,MainActivity.this);
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                lat = String.valueOf(mLastLocation.getLatitude());
                lng = String.valueOf(mLastLocation.getLongitude());
                System.out.println("Lat : "+lat+" Long : "+lng);
            }
        }catch (SecurityException e){
            e.printStackTrace();
            lat="#";
            lng="#";
        }
        if(lat.equals("#") || lng.equals("#")){
            TextView nearby = (TextView) findViewById(R.id.nearbyTextView);
            nearby.setVisibility(View.GONE);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.activity_search_airport);
            rl.setFocusableInTouchMode(false);
            System.out.println("Error : #");
        }else if(lat.equals("!") || lng.equals("!"))
            System.out.println("Error : !");

    }

    public void onLocationChanged(Location location) {
        //Toast.makeText(MainActivity.this, "location :"+location.getLatitude()+" , "+location.getLongitude(), Toast.LENGTH_SHORT).show();
        lat=String.valueOf(location.getLatitude());
        lng=String.valueOf(location.getLongitude());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void GPSLocationPermissionRequest(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showMessageOKCancel(getString(R.string.acceotGPSPermission),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                                    REQUEST_CODE_ASK_PERMISSIONS);
                                    }

                                }
                            });
                    return;
                }
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                    .addConnectionCallbacks(MainActivity.this)
                    .addOnConnectionFailedListener(MainActivity.this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }


    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            // Handle the camera action
        } else if (id == R.id.nav_checkin) {

        } else if (id == R.id.nav_topdestinations) {

        } else if (id == R.id.nav_preferences) {

        } else if (id == R.id.nav_info) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
