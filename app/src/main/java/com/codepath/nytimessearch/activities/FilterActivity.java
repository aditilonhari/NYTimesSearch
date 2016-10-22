package com.codepath.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codepath.nytimessearch.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by aditi on 10/19/2016.
 */

public class FilterActivity extends AppCompatActivity {

    @BindView(R.id.tvSortOrder) TextView tvSortOrder;
    @BindView(R.id.etDate) EditText etDate;
    @BindView(R.id.spinnerID) Spinner spinStatus;
    @BindView(R.id.checkbox_art) CheckBox art;
    @BindView(R.id.checkbox_fashionStyle) CheckBox fashionStyle;
    @BindView(R.id.checkbox_sport) CheckBox sports;
    @BindView(R.id.btn) Button btn;

    private String prioritySelected;
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePicker;
    private Intent applyFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        setInitialPageLoadView();

        etDate.setOnClickListener(v -> {
            initDatePicker();
            datePicker.show();
        });

        spinStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prioritySelected = (String)parent.getItemAtPosition(position);
                applyFilters.putExtra("spinner_value", prioritySelected.toLowerCase());
                setSpinValue(prioritySelected);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btn.setOnClickListener(v -> {
            try {
                sendDataToSearch();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }

    private void setInitialPageLoadView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        applyFilters = new Intent();
        ButterKnife.bind(this);
    }

    private void initDatePicker(){
        Calendar calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        datePicker = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar calDueDate = Calendar.getInstance();
            calDueDate.set(year, monthOfYear, dayOfMonth);
            String strDate = simpleDateFormat.format(calDueDate.getTime());
            etDate.setText(strDate);
            applyFilters.putExtra("date", formatDate(year, monthOfYear,dayOfMonth));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        hideKeyboard();
    }

    private void setSpinValue(String selectedItem){
        int index = -1;
        if (selectedItem.equalsIgnoreCase("Oldest"))
                index = 1;
        else if (selectedItem.equalsIgnoreCase("Newest"))
                index = 0;

        spinStatus.setSelection(index);
    }

    private void sendDataToSearch() throws UnsupportedEncodingException {
        StringBuilder news_desk_values = new StringBuilder();
        if(art.isChecked()){
            news_desk_values.append(art.getText());
            news_desk_values.append(" ");
        }
        if(fashionStyle.isChecked()) {
            news_desk_values.append(URLEncoder.encode(String.valueOf(fashionStyle.getText()),"UTF-8"));
            news_desk_values.append(" ");
        }
        if(sports.isChecked())
            news_desk_values.append(sports.getText());

        applyFilters.putExtra("news_values", news_desk_values.toString());
        setResult(RESULT_OK, applyFilters);
        finish();
    }

    private String formatDate(int year, int monthOfYear, int dayOfMonth){
        String month;
        String day;

        monthOfYear ++;
        month = (monthOfYear < 10)? ("0" + Integer.toString(monthOfYear)) : Integer.toString(monthOfYear);
        day = (dayOfMonth < 10) ? ("0" + Integer.toString(dayOfMonth)): Integer.toString(dayOfMonth);

        return Integer.toString(year) + month + day;
    }

    private void hideKeyboard(){
        View v = this.getCurrentFocus();
        if(v != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}

