package com.myproject.remindme.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.myproject.remindme.R;
import com.myproject.remindme.database.data.AbstractData;
import com.myproject.remindme.database.data.Birthday;
import com.myproject.remindme.eventbus.BirthdaysChangeEvent;
import com.myproject.remindme.eventbus.BusProvider;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Алексей on 17/01/2016.
 */
@SuppressLint("ValidFragment")
public class BirthdayDialog extends DialogFragment {

    public final static String TAG = "BirthdayDialog";
    public final static int BIRTHDAY_ADD = 1;
    public final static int BIRTHDAY_REVISE = 2;

    private View view;
    private EditText editFirstName;
    private EditText editLargeName;
    private EditText editBirthday;
    private GregorianCalendar calendar;
    private AutoCompleteTextView editRelationship;
    private Birthday birthday;
    private int operation;

    public BirthdayDialog(int operation) {
        this(operation, null);
    }

    public BirthdayDialog(int operation, Birthday birthday) {
        this.operation = operation;
        this.birthday = birthday;
        if (operation != BIRTHDAY_ADD && operation != BIRTHDAY_REVISE) {
            throw new IllegalArgumentException("Operation not found");
        }
        if (operation == BIRTHDAY_REVISE && birthday == null) {
            throw new IllegalArgumentException("Element birthday is equal to zero");
        }
        calendar = new GregorianCalendar();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.dialog_birthday, null);

        initToolBar();
        initEditFirstName();
        initEditLargeName();
        initEditBirthday();
        initEditRelationship();

        builder.setView(view)
                .setNegativeButton(R.string.button_cancel, null);

        if (operation == BIRTHDAY_ADD) {
            builder.setPositiveButton(R.string.button_add, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onClickBirthdayAdd();
                }
            });
        } else if (operation == BIRTHDAY_REVISE) {
            builder.setPositiveButton(R.string.button_revise, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onClickBirthdayRevise();
                }
            });
        }

        return builder.create();
    }

    private void onClickBirthdayRevise() {
        String sFirstName = editFirstName.getText().toString();
        if (isEmpty(sFirstName)) {
            return;
        }
        String sLargeName = editLargeName.getText().toString();
        String sBirthday = editBirthday.getText().toString();
        if (isEmpty(sBirthday)) {
            return;
        }
        String sRelationship = editRelationship.getText().toString();
        if (isEmpty(sRelationship)) {
            return;
        }
        birthday.setFirstName(sFirstName);
        birthday.setLastName(sLargeName);
        birthday.setBirthday(calendar);
        birthday.setRelationship(sRelationship);
        BusProvider.getInstance().post(new BirthdaysChangeEvent(AbstractData.ELEMENT_REVISE, birthday));
    }

    private void onClickBirthdayAdd() {
        String sFirstName = editFirstName.getText().toString();
        if (isEmpty(sFirstName)) {
            return;
        }
        String sLargeName = editLargeName.getText().toString();
        String sBirthday = editBirthday.getText().toString();
        if (isEmpty(sBirthday)) {
            return;
        }
        String sRelationship = editRelationship.getText().toString();
        if (isEmpty(sRelationship)) {
            return;
        }
        birthday = new Birthday(sFirstName, sLargeName, calendar, sRelationship);
        BusProvider.getInstance().post(new BirthdaysChangeEvent(AbstractData.ELEMENT_ADD, birthday));
    }

    private boolean isEmpty(String s) {
        if (s.isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.exception_not_fill_fields), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void initEditFirstName() {
        editFirstName = (EditText) view.findViewById(R.id.editFirstNmae);
        if (operation == BIRTHDAY_REVISE) {
            editFirstName.setText(birthday.getFirstName());
        }
    }

    private void initEditLargeName() {
        editLargeName = (EditText) view.findViewById(R.id.editLargeNmae);
        if (operation == BIRTHDAY_REVISE) {
            editLargeName.setText(birthday.getLastName());
        }
    }

    private void initEditBirthday() {
        editBirthday = (EditText) view.findViewById(R.id.editBirthday);
        editBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            calendar.set(year, monthOfYear, dayOfMonth);
                            editBirthday.setText(DateFormat.getDateInstance().format(calendar.getTime()));
                            editBirthday.clearFocus();
                        }
                    };

                    DatePickerDialog dpd = new DatePickerDialog(getActivity(), dateSetListener,
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dpd.show();
                }
            }
        });
        if (operation == BIRTHDAY_REVISE) {
            editBirthday.setText(birthday.getBirthdayString());
            calendar = birthday.getBirthday();
        }
    }

    private void initEditRelationship() {
        editRelationship = (AutoCompleteTextView) view.findViewById(R.id.editRelationship);
        String[] strings = getResources().getStringArray(R.array.relationship_items);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, strings);
        editRelationship.setAdapter(adapter);
        if (operation == BIRTHDAY_REVISE) {
            editRelationship.setText(birthday.getRelationship());
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initToolBar() {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_birthdays);
        toolbar.setNavigationIcon(R.drawable.ic_birthday);
        if (operation == BIRTHDAY_ADD) {
            toolbar.setTitle(getString(R.string.birthdays_add));
        } else if (operation == BIRTHDAY_REVISE) {
            toolbar.setTitle(getString(R.string.birthdays_revise));
        }
    }
}
