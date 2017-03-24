package com.launchpad.dli.launchpad.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.launchpad.dli.launchpad.MainActivity;
import com.launchpad.dli.launchpad.R;

/**
 * Created by dli on 3/3/2017.
 */

public  class ConfigDialog extends DialogFragment {

    EditText twoFingerText;
    EditText threeFingerText;
    EditText fourFingerText;

    CheckBox twoFingerCheckbox;
    CheckBox threeFingerCheckbox;
    CheckBox fourFingerCheckbox;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Config");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.config_fragment,null);
        builder.setView(view);

        twoFingerText = (EditText) view.findViewById(R.id.two_finger_drag_action);
        threeFingerText = (EditText) view.findViewById(R.id.three_finger_drag_action);
        fourFingerText = (EditText) view.findViewById(R.id.four_finger_drag_action);

        twoFingerCheckbox = (CheckBox) view.findViewById(R.id.two_finger_drag_action_included);
        threeFingerCheckbox = (CheckBox) view.findViewById(R.id.three_finger_drag_action_included);
        fourFingerCheckbox = (CheckBox) view.findViewById(R.id.four_finger_drag_action_included);

        read();

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    private void save() {
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE).edit();

        editor.putString("2FingerDrag",twoFingerText.getText().toString());
        editor.putString("3FingerDrag",threeFingerText.getText().toString());
        editor.putString("4FingerDrag",fourFingerText.getText().toString());

        editor.putBoolean("2FingerIncluded", twoFingerCheckbox.isChecked());
        editor.putBoolean("3FingerIncluded", threeFingerCheckbox.isChecked());
        editor.putBoolean("4FingerIncluded", fourFingerCheckbox.isChecked());

        editor.commit();
    }

    private void read() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);

        twoFingerText.setText(sharedPreferences.getString("2FingerDrag","empty value"));
        threeFingerText.setText(sharedPreferences.getString("3FingerDrag","empty value"));
        fourFingerText.setText(sharedPreferences.getString("4FingerDrag","empty value"));

        twoFingerCheckbox.setChecked(sharedPreferences.getBoolean("2FingerIncluded", false));
        threeFingerCheckbox.setChecked(sharedPreferences.getBoolean("3FingerIncluded", false));
        fourFingerCheckbox.setChecked(sharedPreferences.getBoolean("4FingerIncluded", false));
    }
}

