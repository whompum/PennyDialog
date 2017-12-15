package com.whompum.pennydialog.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.GridLayout;

import currencyedittext.whompum.com.currencyedittext.CurrencyEditText;

/**
 * Created by bryan on 12/13/2017.
 *
 * HOW THIS CLASS WORKS:
 *
 * The pennyDialog uses a class called Penny which handles all logic related to manipulating cash values.
 * For example, if the user pressed the backspace, Penny will handle that logic, and we just tell it to do so.
 *
 * Then anytime the penny object is updated, it invokes its client listener, I.E. PennyDialogs IMPL of OnPennyChangedListener
 * which then calls the public method setTotal that updates the CurrencyEditText. CurrencyEditText then calls its client
 * to notify that it has changed, as well as handing us the Penny value. We use that to further determine other things like fab animations,
 * what value to save, etc.
 *
 * So, BASIC FLOW:
 * User presses button
 * Update penny object accordingly
 * Recieve notification from penny object, and update CurrencyEditText
 * Recieve notification from CurrencyEditText, update fab, and total.
 *
 */

public class PennyDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private static final String TAG = "PennyDialog";

    private static final int LAYOUT_RES = R.layout.penny_dialog_layout;

    private static final String PENNY_KEY = "total"; //Bundles Penny value key
    private static final String CASH_KEY = "cash"; //Bundles Cash value key

    private static final int SPECIALTY_BTN_BACKSPACE = R.id.backspaceButton; //Id of the backspace
    private static final int SPECIALTY_BTN_DELETE = R.id.deleteButton; //Id of the delete button


    /**
     * Penny object we update; Given a callback IMPL at compile time
     * that will call setTotal anytime a change is made
     */
    private Penny penny = new Penny(new Penny.OnPennyChangedListener(){
        @Override
        public void onPennyChanged(String penny) {
            setTotal(penny);
        }
    });


    private long pennies = 0L; //Total in pennies; Used for clients, and to animate the FAB
    private String cash = "0"; //Total in cash (raw String version); Used for clients really


    private CashChangeListener cashChangeListener = null; // Client impl to be invoked when the user enters a new total


    private CurrencyEditText valueEditText;
    private FloatingActionButton addTotalFab = null;


    private Vibrator vibrator; //vibrates buttons when pressed


    private Interpolator fabInterpolator = new AccelerateDecelerateInterpolator();

    /**
     * Creates an Instance of this object, and returns it to the client
     *
     * @param args Arguments supplied to this guy. Could be a total, could be some styling
     * @return an instanc of PennyDialog.class
     */
    public static PennyDialog newInstance(@Nullable final Bundle args){
        final PennyDialog pennyDialog = new PennyDialog();
        pennyDialog.setArguments(args);
    return pennyDialog;
    }

    /**
     * Creates an instance of this object, with a callback implementation
     * @param cashChangeListener Callback
     * @return an instance of PennyDialog.class
     */
    public static PennyDialog newInstance(final CashChangeListener cashChangeListener){
        final PennyDialog pennyDialog = new PennyDialog();
        pennyDialog.setCashChangeListener(cashChangeListener);
    return pennyDialog;
    }

    /**
     * Creates an instance of this object with arguments AND a listener
     * @param cashChangeListener callback
     * @param args arguments
     * @return an instance
     */
    public static PennyDialog newInstance(final CashChangeListener cashChangeListener, final Bundle args){
        final PennyDialog pennyDialog = new PennyDialog();
        pennyDialog.setArguments(args);
        pennyDialog.setCashChangeListener(cashChangeListener);

    return pennyDialog;
    }


    @Override //Overriden to initialize the Vibrator object
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @NonNull
    @Override //Creates the dialog
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog theD = new Dialog(this.getContext());
        theD.requestWindowFeature(Window.FEATURE_NO_TITLE);

    return theD;
    }

    /*
      Will initialize views, and call initNumberListener()
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Attach to root should be set to true maybe?
        final View content = inflater.inflate(LAYOUT_RES, container, false);


        this.valueEditText = content.findViewById(R.id.valueEditText);
             valueEditText.setOnCurrencyChangeListener(valueChangeListener);

        this.addTotalFab = content.findViewById(R.id.doneFab);
             addTotalFab.setOnClickListener(this);

        initNumbersListener(content);


        if(savedInstanceState!=null) {
            cash = savedInstanceState.getString(CASH_KEY, "0");
            pennies = savedInstanceState.getLong(PENNY_KEY, 0L);
        }

        valueEditText.setText(cash);

    return content;
    }

    /**
     *
     * Iterates through the number grid, and sets an onclick listener on all the views
     *
     * @param content Base view containing the number grid
     */
    private void initNumbersListener(final View content){
        final GridLayout numbersGrid = content.findViewById(R.id.numbersGrid);

        for(int a = 0; a < numbersGrid.getChildCount(); a++)
            numbersGrid.getChildAt(a).setOnClickListener(this.numberClickListener);
    }


    /**
     * Set's the total for the EditText object
     * @param value
     */
    private void setTotal(final String value){
        valueEditText.setText(value);
    }


    //Registers a listener
    public void setCashChangeListener(final CashChangeListener cashChangeListener){
        this.cashChangeListener = cashChangeListener;
    }


    /**
     *
     * Only our fab will be linked to this callback since its will invoke
     * fragment wide interactions
     *
     * @param view Should always be a FloatingActionButton
     */
    @Override
    public void onClick(View view) {

    }

    /**
     * Is used for when a number button is clicked upon;
     * Segregated from when the fab is clicked for simplicity purposes
     */
    private final View.OnClickListener numberClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            final int id = view.getId();

            if(id == SPECIALTY_BTN_BACKSPACE)
                penny.removePennies();
            else if(id == SPECIALTY_BTN_DELETE)
                penny.resetPennies();

            else
                penny.addPennies(((Button)view).getText().toString());

        }
    };


    /**
     * Choreographer method that determines if we should hide/show the fab.
     * Show the fab ONLY if it's not shown, and penny values are greater than zero
     * Hide the fab ONLY if its showing, and penny value is less than zero
     */
    private void updateFab(){

        final boolean shouldShow = (pennies > 0);

        if(!shouldShow & addTotalFab.isShown()) //If pennies < 0, AND fab is showing currently
            hideFab();

        else if(shouldShow & !addTotalFab.isShown()) //If pennies >0, AND fab isn't showing currently
            showFab();
    }

    private void hideFab(){
        addTotalFab.animate()
                .scaleY(0F)
                .scaleX(0F)
                .setDuration(500L)
                .setInterpolator(fabInterpolator).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                addTotalFab.setVisibility(View.INVISIBLE);
            }
        }).start();
    }

    private void showFab(){
        addTotalFab.animate()
                .scaleY(1F)
                .scaleX(1F)
                .setDuration(500L)
                .setInterpolator(fabInterpolator).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationEnd(animation);
                addTotalFab.setVisibility(View.VISIBLE);
            }
        }).start();
    }



    /**
     * Callback induced whenever the CurrencyEditText object changes its text.
     */
    private final CurrencyEditText.OnCurrencyChange valueChangeListener = new CurrencyEditText.OnCurrencyChange() {
        /**
         *
         * NOTE, we'll use this method to track penny values
         * Fro this we can return the penny value to the client, we can
         * monitor its value to decide if we need to show/hide the fab or not, etc etc
         *
         * @param pennyValue Returns the pennie value of the total from CurrencyEditText
         */
        @Override
        public void onCurrencyChange(long pennyValue) {
                pennies = pennyValue;
                cash = valueEditText.getText().toString();
                updateFab();
        }
    };


    /**
     * Callback invoked when the add fab is clicked upon.
     */
    public interface CashChangeListener{
        void onPenniesChange(final long pennies);
        void onCashChange(final String cashValue);
    }

}





