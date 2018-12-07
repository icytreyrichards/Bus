package com.my.easybus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import BusinessLogic.LogicClass;
import localdata.Data;
import utility.Utility;

public class Forgotten extends Fragment implements View.OnClickListener {
    private EditText email;
    private ProgressBar progress;
    private String em;
    private Button send;
    private ArrayList<String>countrylist,countryCodes;
    private Spinner countryspinner;
    private ArrayAdapter adapter;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public Forgotten() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Forgotten newInstance(int sectionNumber) {
        Forgotten fragment = new Forgotten();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.forgotten, container, false);

       email = (EditText) rootView.findViewById(R.id.editTextemail);
        progress = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        send=(Button)rootView.findViewById(R.id.buttonregisteraccount);
         send.setOnClickListener(this);
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
        if (v.getId() == R.id.textViewlogin) {
            Intent i=new Intent(getActivity(),Login.class);
            i.putExtra("cmd","forgot");
        } else if (v.getId() == R.id.buttonregisteraccount) {
            em = email.getText().toString();
            if (em.equals("")) {
                email.setError("email missing");
            }
            else
            {
                NetWork task = new NetWork();
            // if(passconfirm.getText().equals(pwd)) {
            task.execute();
        }
            /*}
            else
            {
                passconfirm.setError("pins dont match");
            }
            */
        }
    }
    public class NetWork extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Void... arg0) {
                return LogicClass.forgottenPassword(em);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("res",result);
            progress.setVisibility(View.GONE);
            Utility.showAlert(result, getActivity());

            }
        }
}