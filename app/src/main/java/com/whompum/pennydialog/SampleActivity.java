package com.whompum.pennydialog;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.whompum.pennydialog.dialog.PennyDialog;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        final Bundle bundle = new Bundle();
        bundle.putInt(PennyDialog.STYLE_KEY, R.style.PennyDialog_Dark);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               PennyDialog dialog = PennyDialog.newInstance(new PennyDialog.CashChangeListener() {
                   @Override
                   public void onPenniesChange(long pennies) {
                       Log.i("DOUBLE_TAP_FIX", "I've been tapped :o");
                        Toast.makeText(SampleActivity.this, String.valueOf(pennies), Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onCashChange(String cashValue) {
                       //Toast.makeText(SampleActivity.this, cashValue, Toast.LENGTH_SHORT).show();
                   }
               }, bundle);
               dialog.show(getSupportFragmentManager(), PennyDialog.TAG);

               dialog.setTitle("NEWLY MADE TITLE");

            }
        });


        Configuration c = getResources().getConfiguration();

        Log.i("LAYOUT_FIX", "SCREEN WIDTH dp: " + c.screenWidthDp);


    }

}
