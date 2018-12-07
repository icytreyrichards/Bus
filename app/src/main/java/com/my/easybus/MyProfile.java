package com.my.easybus;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import BusinessLogic.LogicClass;
import utility.Utility;

public class MyProfile extends Fragment implements View.OnClickListener {
    private TextView login;
    private EditText fullname, email, phone, password;
    private ProgressBar progress;
    private String fname,em,tel,pwd;
    private Button register;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private FloatingActionButton fab;
    public MyProfile() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MyProfile newInstance(int sectionNumber) {
        MyProfile fragment = new MyProfile();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.myprofile, container, false);
        fab=(FloatingActionButton)rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new MyProfile(), "").commit();
            }
        });
        fullname = (EditText) rootView.findViewById(R.id.editTextfullname);
        email = (EditText) rootView.findViewById(R.id.editTextemail);
        phone = (EditText) rootView.findViewById(R.id.editTextphone);
        progress = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        register=(Button)rootView.findViewById(R.id.buttonregisteraccount);
        register.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        if (visible) {
            ((IndexActivity) getActivity()).toolbar.setTitle("Emergency Register");
        }
        super.setMenuVisibility(visible);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonregisteraccount) {
             fname = fullname.getText().toString();
             em = email.getText().toString();
            tel = phone.getText().toString();
             pwd = password.getText().toString();
            NetWork task=new NetWork();
            task.execute();
        }

    }
    public class NetWork extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // showDialog(progress_bar_type);
            progress.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Void... arg0) {
            return LogicClass.UpdateRecords(getActivity(),fname, em, tel);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.setVisibility(View.GONE);
            Utility.showAlert(result,getActivity());
        }
    }
}