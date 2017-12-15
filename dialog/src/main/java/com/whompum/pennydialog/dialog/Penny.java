package com.whompum.pennydialog.dialog;

import android.support.annotation.NonNull;

/**
 * Created by bryan on 12/14/2017.
 */

public class Penny {

    private OnPennyChangedListener pListener;

    public Penny(@NonNull final OnPennyChangedListener pListener){
        this.pListener = pListener;
    }

    private String pennies = "0";

    private int backspaceIndex = 0;

    public void setPennies(final String pennies){
        this.pennies = pennies;

    setBackspaceIndex();
    pListener.onPennyChanged(this.pennies);
    }

    public void addPennies(final String pennies){
        this.pennies += pennies;

    setBackspaceIndex();
    pListener.onPennyChanged(this.pennies);
    }

    public void removePennies(){
        final String temp = pennies;

     if(pennies.length() <= 1) return;

     pennies = temp.substring(0, temp.length()-1);
    pListener.onPennyChanged(this.pennies);
    }

    public void resetPennies(){
        pennies = "0";
        setBackspaceIndex();
    pListener.onPennyChanged(this.pennies);
    }


    private void setBackspaceIndex(){
        this.backspaceIndex = pennies.length()-1;
    }



    interface OnPennyChangedListener{
        void onPennyChanged(String penny);
    }



}
