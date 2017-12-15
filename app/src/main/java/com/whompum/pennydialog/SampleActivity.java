package com.whompum.pennydialog;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.whompum.pennydialog.dialog.PennyDialog;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               PennyDialog dialog = PennyDialog.newInstance(new PennyDialog.CashChangeListener() {
                   @Override
                   public void onPenniesChange(long pennies) {
                        Toast.makeText(SampleActivity.this, String.valueOf(pennies), Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onCashChange(String cashValue) {
                       //Toast.makeText(SampleActivity.this, cashValue, Toast.LENGTH_SHORT).show();
                   }
               }, null);
               dialog.show(getSupportFragmentManager(), PennyDialog.TAG);
            }
        });
    }

}
