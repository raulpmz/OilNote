package com.example.raul.oilnote.Utils;

import android.view.View;
import android.widget.RadioButton;

/**
 * Clase para crear un grupo de RadioButton.
 */

public class CustomRadioGroups {

    // Global variables
    private RadioButton rb [];
    private int firstCheckedItemId;
    public static int countRadioButtons;

    /**
     *     Para inicializar un RadioGroup:
     *
     *         RadioGroup2 rg = new RadioGroup2;
     *         RadioButton rb [] = new RadioButton[3];
     *         rb[0] = (RadioButton)findViewById(R.id.radio1);
     *         rb[1] = (RadioButton)findViewById(R.id.radio2);
     *         rb[2] = (RadioButton)findViewById(R.id.radio3);
     *         rg.createRadioGroup(rb);
     *
     */
    public void createRadioGroup(RadioButton [] radioButtons){
        rb = radioButtons;
        firstCheckedItemId = getCheckedItemId();
        countRadioButtons = rb.length;
        onCheck();
    }

    private void onCheck(){
        for (RadioButton aRb : rb) {
            aRb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View radio) {
                    final RadioButton myRb = (RadioButton) radio;
                    for (RadioButton aRb : rb) {
                        if (aRb.getId() == myRb.getId()) {
                            aRb.setChecked(true);
                        } else aRb.setChecked(false);
                    }
                }
            });
        }
    }

    public int getRadioButtonsCount(){
        return countRadioButtons;
    }

    public int getCheckedItemId (){
        int id = -1;
        for (RadioButton aRb : rb) {
            if(aRb.isChecked())
                id = aRb.getId();
        }
        return id;
    }

    public RadioButton getCheckedItem (){
        RadioButton RB = null;
        for (RadioButton aRb : rb) {
            if(aRb.isChecked())
                RB = aRb;
        }
        return RB;
    }

    public void resetButtons(){
        RadioButton iRb = getCheckedItem();
        if(iRb != null)
            iRb.setChecked(false);
        if(firstCheckedItemId != -1)
            getChildById(firstCheckedItemId).setChecked(true);
    }

    private RadioButton getChildById(int id){
        RadioButton iRb = null;
        for (RadioButton aRb : rb ) {
            if(aRb.getId() == id) {
                iRb = aRb;
                break;
            }
        }
        return iRb;
    }


    public RadioButton getChildAt (int position){
        return rb[position];
    }
}