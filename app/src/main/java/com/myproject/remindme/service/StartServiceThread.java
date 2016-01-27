package com.myproject.remindme.service;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.myproject.remindme.database.data.Birthday;
import com.myproject.remindme.database.handler.DatabaseHandler;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Alecey Ermolovich on 1/26/2016.
 */
public class StartServiceThread extends Thread {

    private int REQUEST_CODE = 123;

    private DatabaseHandler.BirthdaysTable birthdaysTable;
    private List<Birthday> birthdayList;
    private AlarmManager alarmManager;
    private Context context;

    public StartServiceThread(Context context) {
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void run() {
        birthdaysTable = DatabaseHandler.getInstance(context).getBirthdaysTable();
        birthdayList = birthdaysTable.getAll();
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Collections.sort(birthdayList, new BirthdayComparator());

        for (Birthday birthday : birthdayList) {
            Intent intent = new Intent(context, BirthdayAlarmService.class);
            intent.putExtra("name_of_the_birthday", birthday.getFirstName() + " " + birthday.getLastName());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);

            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, birthday.getBirthday().get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.MONTH, birthday.getBirthday().get(Calendar.MONTH));
            calendar.set(Calendar.HOUR, 8);
            calendar.set(Calendar.MINUTE, 53);
            Log.i(birthday.getLastName(), birthday.getBirthdayString());

            long timeInMillis = calendar.getTimeInMillis();
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent);

            break;
        }
    }

    private class BirthdayComparator implements Comparator<Birthday> {

        @Override
        public int compare(Birthday lhs, Birthday rhs) {
            GregorianCalendar lhsBirthdayday = lhs.getBirthday();
            GregorianCalendar rhsBirthday = rhs.getBirthday();
            int lhsm = lhsBirthdayday.get(Calendar.MONTH);
            int rhsm = rhsBirthday.get(Calendar.MONTH);
            if (lhsm < rhsm) {
                return -1;
            } else if (lhsm == rhsm) {
                int lhsd = lhsBirthdayday.get(Calendar.DAY_OF_MONTH);
                int rhsd = rhsBirthday.get(Calendar.DAY_OF_MONTH);
                if (lhsd < rhsd) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                return 1;
            }
        }

        @Override
        public boolean equals(Object object) {
            return false;
        }
    }
}
