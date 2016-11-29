package gr.uom.agelogeo.androidproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import java.util.Calendar;

import android.content.Intent;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText departureText;
    EditText arrivalText;
    EditText fromText,destText;
    ImageButton swapAirports,clearReturnDate;
    int year_x,month_x,day_x;
    static final int DEPARTURE_DATE_ID = 0;
    static final int ARRIVAL_DATE_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
                //System.out.println(intent.getStringExtra("item"));

            }
        });

        destText = (EditText) findViewById(R.id.destText);
        destText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SearchAirportActivity.class);
                startActivityForResult(i, 2);
                //System.out.println(intent.getStringExtra("item"));

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
                arrivalText = (EditText) findViewById(R.id.arrivalDate);
                if(arrivalText.getText().length()!=0)
                    arrivalText.getText().clear();
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String stredittext=data.getStringExtra("edittextvalue");
                fromText.setText(stredittext);
            }
        }
        else if (requestCode == 2) {
            if(resultCode == RESULT_OK){
                String stredittext=data.getStringExtra("edittextvalue");
                destText.setText(stredittext);
            }
        }
    }

    public void showDialogOnClick(){
        departureText = (EditText) findViewById(R.id.departureDate);
        departureText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DEPARTURE_DATE_ID);
            }
        });

        arrivalText = (EditText) findViewById(R.id.arrivalDate);
        arrivalText.setOnClickListener(new View.OnClickListener() {
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
            departureText.setText(day+"/"+(month+1)+"/"+year);
        }
    };

    private DatePickerDialog.OnDateSetListener dpickerListner2
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            arrivalText.setText(day+"/"+(month+1)+"/"+year);
        }
    };
}
