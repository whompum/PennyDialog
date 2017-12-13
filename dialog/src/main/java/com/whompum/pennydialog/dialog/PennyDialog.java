package com.whompum.pennydialog.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by bryan on 12/13/2017.
 */

public class PennyDialog extends AppCompatDialogFragment {

    private static final String TAG = "PennyDialog";

    private static final int LAYOUT_RES = R.layout.penny_dialog_layout;

    private static final String TOTAL_KEY = "total"; //Used for reconstruction during onSaveInstanceState

    private long pennies = 0L; //Total in pennies

    private String cash = ""; //Total in cash (raw String version)

    private CashChangeListener cashChangeListener = null; // Client impl to be invoked when the user enters a new total


    private FloatingActionButton addTotalFab = null;


    public static PennyDialog newInstance(final Bundle args){
        final PennyDialog pennyDialog = new PennyDialog();
        pennyDialog.setArguments(args);
    return pennyDialog;
    }


    public static PennyDialog newInstance(final CashChangeListener cashChangeListener){
        final PennyDialog pennyDialog = new PennyDialog();
        pennyDialog.setCashChangeListener(cashChangeListener);
    return pennyDialog;
    }


    public static PennyDialog newInstance(final CashChangeListener cashChangeListener, final Bundle args){
        final PennyDialog pennyDialog = new PennyDialog();
        pennyDialog.setArguments(args);
        pennyDialog.setCashChangeListener(cashChangeListener);

    return pennyDialog;
    }

    @NonNull
    @Override //Creates the dialog
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog theD = new Dialog(this.getContext());
        theD.requestWindowFeature(Window.FEATURE_NO_TITLE);

    return theD;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Attach to root should be set to true maybe?
        final View content = inflater.inflate(LAYOUT_RES, container, false);




    return content;
    }

    //Registers a listener
    public void setCashChangeListener(final CashChangeListener cashChangeListener){
        this.cashChangeListener = cashChangeListener;
    }


    //Callback client for when the total has changed
    public interface CashChangeListener{
        void onPenniesChange(final long pennies);
        void onCashChange(final String cashValue);
    }

}






