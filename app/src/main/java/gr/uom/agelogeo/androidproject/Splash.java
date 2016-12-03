package gr.uom.agelogeo.androidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static android.os.SystemClock.sleep;


public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2500);
                    Intent i = new Intent(Splash.this,MainActivity.class);
                    startActivity(i);
                    Splash.this.finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        Thread myThread = new Thread(r);
        myThread.start();
           /* sleep(2000);
            Intent intent = new Intent(Splash.this,MainActivity.class);
            startActivity(intent);
            Splash.this.finish();*/

    }
}
