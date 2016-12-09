package gr.uom.agelogeo.androidproject;

import android.app.DatePickerDialog;
import android.app.Dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    int adult_p = 1 , kid_p = 0 , baby_p = 0 , max_p = 9;
    EditText departureText;
    EditText returnText ;
    EditText fromText,destText,passengersText;
    TextView passengersNumber;
    ImageButton swapAirports,clearReturnDate;
    Switch directflightswitch;
    Button searchflightsbtn ;
    int year_x,month_x,day_x;
    static final int DEPARTURE_DATE_ID = 0;
    static final int ARRIVAL_DATE_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }


    public void Listeners(){
        fromText = (EditText) findViewById(R.id.fromText);
        fromText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SearchAirportActivity.class);
                startActivityForResult(i, 1);
            }
        });

        destText = (EditText) findViewById(R.id.destText);
        destText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SearchAirportActivity.class);
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
                    System.out.println(fromText.getText().subSequence(fromText.getText().length()-4,fromText.getText().length()-1));
                    System.out.println(destText.getText().subSequence(destText.getText().length()-4,destText.getText().length()-1));
                    i.putExtra("origin", fromText.getText().subSequence(fromText.getText().length()-4,fromText.getText().length()-1).toString());
                    i.putExtra("destination", destText.getText().subSequence(destText.getText().length()-4,destText.getText().length()-1).toString());
                    i.putExtra("departure_date", departureText.getText().toString());
                    i.putExtra("return_date", returnText.getText().toString());
                    i.putExtra("adults", adult_p);
                    i.putExtra("children", kid_p);
                    i.putExtra("infants", baby_p);
                    i.putExtra("nonstop", directflightswitch.isChecked());
                    i.putExtra("travel_class", "ECONOMY");
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
                    Date departureD = sdf.parse(s.toString());
                    if(returnText.getText().length()!=0) {
                        Date returnD = sdf.parse(returnText.getText().toString());
                        if(departureD.after(returnD))
                            returnText.setText("");
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
                    Date returnD = sdf.parse(s.toString());
                    if(returnText.getText().length()!=0) {
                        Date departureD = sdf.parse(departureText.getText().toString());
                        if(returnD.before(departureD))
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
                showDialog(ARRIVAL_DATE_ID);
            }
        });
    }

    protected Dialog onCreateDialog(int id){
        if ( id == DEPARTURE_DATE_ID ) {
            DatePickerDialog dialog = new DatePickerDialog(this, dpickerListner, year_x, month_x, day_x);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            return dialog;
        }else if ( id == ARRIVAL_DATE_ID ) {
            DatePickerDialog dialog = new DatePickerDialog(this, dpickerListner2, year_x, month_x, day_x);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            return dialog;
        }return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner
            = new DatePickerDialog.OnDateSetListener() {

        @Override

        public void onDateSet(DatePicker datePicker, int year, int month, int day ) {
            Calendar cal = Calendar.getInstance();
            cal.set(year,month,day);
            Date date = cal.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            departureText.setText(formatter.format(date));
        }
    };

    private DatePickerDialog.OnDateSetListener dpickerListner2
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.set(year,month,day);
            Date date = cal.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            returnText.setText(formatter.format(date));
        }
    };
}
