package com.myproject.remindme.adapter.data;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.myproject.remindme.R;
import com.myproject.remindme.database.data.AbstractData;
import com.myproject.remindme.database.data.Birthday;
import com.myproject.remindme.dialog.BirthdayDialog;
import com.myproject.remindme.eventbus.BirthdaysChangeEvent;
import com.myproject.remindme.eventbus.BusProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Алексей on 15/01/2016.
 */
public class BirthdaysAdapter extends AbstractAdapter<Birthday> {

    public BirthdaysAdapter(FragmentActivity activity, View view, List<Birthday> birthdayList) {
        this.dataList = birthdayList;
        this.drawableList = birthdayList;
        this.activity = activity;
        this.layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.textNotFound = (TextView) view.findViewById(R.id.textNotFound);
        this.textNotFound.setText(R.string.exception_not_found_birthday);
        this.setLayout(R.layout.listview_item_birthdays);
    }

    @Override
    public void filter(String search) {
        if (search.isEmpty()) {
            drawableList = dataList;
            this.textNotFound.setText(R.string.exception_not_found_birthday);
        } else {
            drawableList = new ArrayList<>();
            for (Birthday birthday : dataList) {
                if (birthday.getFirstName().contains(search)) {
                    drawableList.add(birthday);
                } else if (birthday.getLastName().contains(search)) {
                    drawableList.add(birthday);
                } else if (birthday.getBirthdayString().contains(search)) {
                    drawableList.add(birthday);
                } else if (birthday.getRelationship().contains(search)) {
                    drawableList.add(birthday);
                }
            }
        }
    }

    @Override
    protected void showInformation(int position, View view) {
        Birthday birthday = (Birthday) getItem(position);
        TextView textName = (TextView) view.findViewById(R.id.textHeader);
        TextView textDate = (TextView) view.findViewById(R.id.textBirthdaysDate);
        TextView textRelationship = (TextView) view.findViewById(R.id.textRelationship);
        textName.setText(birthday.getFirstName() + " " + birthday.getLastName());
        textDate.setText(birthday.getBirthdayString());
        textRelationship.setText(birthday.getRelationship());
    }

    @Override
    protected void showPopupMenu(final View view) {
        PopupMenu popupMenu = new PopupMenu(this.getActivity(), view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_listview, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Birthday birthday = (Birthday) getItem(view.getId());
                if (item.getItemId() == R.id.menu_item_listview_delete) {
                    BusProvider.getInstance().post(new BirthdaysChangeEvent(AbstractData.ELEMENT_DELETE, birthday));
                } else if (item.getItemId() == R.id.menu_item_listview_revise) {
                    BirthdayDialog dialog = new BirthdayDialog(BirthdayDialog.BIRTHDAY_REVISE, birthday);
                    dialog.show(getActivity().getSupportFragmentManager(), BirthdayDialog.TAG);
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
