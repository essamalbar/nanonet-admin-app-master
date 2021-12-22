package com.crazyiter.nanonet;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Statics {

    public static boolean checkInput(TextInputLayout layout, String errorMessage) {
        if (layout.getEditText().getText().toString().trim().isEmpty()) {
            layout.setError(errorMessage);
            return false;
        }

        layout.setError("");
        return true;
    }

    public static boolean checkInput(TextInputLayout layout, String errorMessage, boolean email) {
        if (checkInput(layout, errorMessage)) {
            String s = layout.getEditText().getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                layout.setError(errorMessage);
                return false;
            }
            return true;
        }

        return false;
    }

    public static class LocalDate {

        public static final long SECOND = 1000;
        public static final long MINUTE = 60 * SECOND;
        public static final long HOUR = 60 * MINUTE;
        public static final long DAY = 24 * HOUR;

        @SuppressLint("SimpleDateFormat")
        public static String getCurrentDate() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            return format.format(new Date());
        }

        @SuppressLint("SimpleDateFormat")
        public static int getDifferenceDays(String startDate, String endDate) {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            try {

                long start = dateTimeFormat.parse(startDate).getTime();
                long end = dateTimeFormat.parse(endDate).getTime();

                if (start > end) {
                    return 0;
                }

                double dif = Math.abs(end - start);
                return (int) (dif / DAY);

            } catch (ParseException e) {
                return 0;
            }
        }

        @SuppressLint("SimpleDateFormat")
        public static int getDifferenceHours(String startDate, String endDate) {
            SimpleDateFormat dateTimeFormat;
            dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);

            try {
                long start = dateTimeFormat.parse(startDate).getTime();
                long end = dateTimeFormat.parse(endDate).getTime();

                if (start > end) {
                    return 0;
                }

                double dif = Math.abs(end - start);
                dif %= DAY;
                return (int) (dif / HOUR);

            } catch (ParseException e) {
                return 0;
            }
        }

        @SuppressLint("SimpleDateFormat")
        public static String getEndDate(String startDate, int type, int count) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
            Date date;

            try {
                date = format.parse(startDate);
            } catch (ParseException e) {
                return null;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 1);

            if (type == 0) { // day
                calendar.add(Calendar.DAY_OF_YEAR, count);
            }

            if (type == 1) { // month
                calendar.add(Calendar.MONTH, count);
            }

            if (type == 2) { // year
                calendar.add(Calendar.YEAR, count);
            }

            return getDate(calendar);
        }

        public static void getDate(Context context, DateSelection dateSelection) {
            Calendar calendar = Calendar.getInstance();
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
                month++;
                String y = String.valueOf(year);
                String m = String.valueOf(month);
                String d = String.valueOf(dayOfMonth);
                if (month < 10) {
                    m = "0" + month;
                }
                if (dayOfMonth < 10) {
                    d = "0" + dayOfMonth;
                }
                String selectedDate = y + "-" + m + "-" + d;
                int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                int mMinute = calendar.get(Calendar.MINUTE);

                new TimePickerDialog(context, (view1, hourOfDay, minute) -> {
                    String h = String.valueOf(hourOfDay);
                    String mm = String.valueOf(minute);
                    String a = "AM";

                    if (hourOfDay == 0) h = "12";
                    else if (hourOfDay < 10) h = "0" + hourOfDay;
                    else if (hourOfDay > 12) h = String.valueOf(Math.abs(12 - hourOfDay));
                    if (hourOfDay >= 12) a = "PM";

                    if (minute < 10) mm = "0" + minute;
                    String selectedTime = h + ":" + mm + " " + a;
                    dateSelection.onSelected(selectedDate + " " + selectedTime);
                }, mHour, mMinute, false).show();
            }, mYear, mMonth, mDay).show();
        }

        public static String getDate(Calendar calendar) {
            int mYear = calendar.get(Calendar.YEAR);
            int mMonth = calendar.get(Calendar.MONTH);
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            String y = String.valueOf(mYear);
            String m = String.valueOf(mMonth);
            String d = String.valueOf(mDay);
            if (mMonth < 10) {
                m = "0" + mMonth;
            }
            if (mDay < 10) {
                d = "0" + mDay;
            }
            String selectedDateTime = y + "-" + m + "-" + d;

            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            String h = String.valueOf(hourOfDay);
            String mm = String.valueOf(minute);
            String a = "AM";

            if (hourOfDay == 0) h = "12";
            else if (hourOfDay < 10) h = "0" + hourOfDay;
            else if (hourOfDay > 12) h = String.valueOf(Math.abs(12 - hourOfDay));
            if (hourOfDay >= 12) a = "PM";

            if (minute < 10) mm = "0" + minute;
            String selectedTime = h + ":" + mm + " " + a;
            selectedDateTime += (" " + selectedTime);

            return selectedDateTime;
        }

        public interface DateSelection {
            void onSelected(String date);
        }

    }

    public static void sendEmail(Context context, String email) {
        context.startActivity(
                Intent.createChooser(
                        new Intent(Intent.ACTION_VIEW)
                                .setData(
                                        Uri.parse("mailto:?to=" + email)
                                ),
                        context.getString(R.string.send_email)
                )
        );
    }

}
