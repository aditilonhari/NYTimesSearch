package com.codepath.nytimessearch.adapters;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
 * Created by aditi on 10/20/2016.
 */

public class FilterDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    @BindView(R.id.etDate) EditText beginDate;
    @BindView(R.id.spinnerID) Spinner spinStatus;
    @BindView(R.id.checkbox_art) CheckBox art;
    @BindView(R.id.checkbox_fashionStyle) CheckBox fashionStyle;
    @BindView(R.id.checkbox_sport) CheckBox sports;
    @BindView(R.id.btn) Button saveAndSend;

    private String prioritySelected;
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePicker;
    Bundle bundle;

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    public interface EditFilterDialogListener {
        void onFinishEditDialog(Bundle b);
    }

    public FilterDialogFragment(){
        // Empty Constructor is required for the DialogFragment
    }

    public FilterDialogFragment newInstance(){
        FilterDialogFragment frag = new FilterDialogFragment();
        bundle = new Bundle();
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View inflate = inflater.inflate(R.layout.activity_filters, container);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch arguments from bundle and set title
//        String title = getArguments().getString("title", "Select Filters");
        getDialog().setTitle("Select Filters");

        beginDate.setOnClickListener(v -> {
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
            initDatePicker();
            datePicker.show();
        });


        spinStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prioritySelected = (String)parent.getItemAtPosition(position);
                bundle.putString("spinner_value", prioritySelected.toLowerCase());
                setSpinValue(prioritySelected);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        saveAndSend.setOnClickListener(v -> {
            EditFilterDialogListener listener = (EditFilterDialogListener)getActivity();

            StringBuilder sb = new StringBuilder();
            if(art.isChecked()) {
                sb.append("Art");
                sb.append(" ");
            }
            if(fashionStyle.isChecked()) {
                try {
                    sb.append(URLEncoder.encode("Fashion & Style", "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append(" ");
            }
            if(sports.isChecked()) {
                sb.append("Sports");
                sb.append(" ");
            }

            bundle.putString("news_values", sb.toString());
            listener.onFinishEditDialog(bundle);
            dismiss();
        });
    }

    private void setSpinValue(String selectedItem){
        int index = -1;
        if (selectedItem.equalsIgnoreCase("Oldest"))
            index = 1;
        else if (selectedItem.equalsIgnoreCase("Newest"))
            index = 0;

        spinStatus.setSelection(index);
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
        View v = getView();
        if(v != null) {
        }
    }

    private void initDatePicker(){
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        datePicker = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            Calendar calDueDate = Calendar.getInstance();
            calDueDate.set(year, monthOfYear, dayOfMonth);
            String strDate = simpleDateFormat.format(calDueDate.getTime());
            beginDate.setText(strDate);
            bundle.putString("date", formatDate(year, monthOfYear,dayOfMonth));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        //hideKeyboard();
    }



}
