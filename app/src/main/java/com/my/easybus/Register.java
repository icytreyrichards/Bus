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

public class Register extends Fragment implements View.OnClickListener {
    private TextView login;
    private EditText fullname, email, phone, password;
    private ProgressBar progress;
    private String fname,em,tel,pwd,telCode="256";
    private Button register;
    private ArrayList<String>countrylist,countryCodes;
    private Spinner countryspinner;
    private ArrayAdapter adapter;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public Register() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Register newInstance(int sectionNumber) {
        Register fragment = new Register();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register, container, false);



        login = (TextView) rootView.findViewById(R.id.textViewlogin);
        fullname = (EditText) rootView.findViewById(R.id.editTextfullname);
        email = (EditText) rootView.findViewById(R.id.editTextemail);
        phone = (EditText) rootView.findViewById(R.id.editTextphone);
        password = (EditText) rootView.findViewById(R.id.editTextpin);
        progress = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        register=(Button)rootView.findViewById(R.id.buttonregisteraccount);
        countryspinner=(Spinner)rootView.findViewById(R.id.spinner);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
         new SetUpTask().execute();
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
            i.putExtra("cmd","reg");
            startActivity(i);
            getActivity().finish();

        } else if (v.getId() == R.id.buttonregisteraccount) {
            fname = fullname.getText().toString();
            em = email.getText().toString();
            tel = phone.getText().toString();
            pwd = password.getText().toString();
            telCode = countryCodes.get(countryspinner.getSelectedItemPosition());
            if (fname.equals("")) {
                fullname.setError("fullname missing");
            } else if (tel.equals("")) {
                phone.setError("Telephone Missing");
            }
            else if(!tel.startsWith("07"))
            {
                phone.setError("invalid Telephone Number");
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
                return LogicClass.SignUp(fname, em, tel, pwd,telCode);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("res",result);
            progress.setVisibility(View.GONE);
           // Utility.showAlert(result, getActivity());
            if(!result.startsWith("[")) {
               // Utility.showAlert(result, getActivity());
            }
            else {
                try
                {
                    JSONArray jarray = new JSONArray(result);
                    int x = 0;
                    for (x = 0; x < jarray.length(); x++) {
                        JSONObject obj = jarray.getJSONObject(x);
                        if (obj.getString("data").equals("available")) {
                            String fullname=obj.getString("fullname");
                            String email=obj.getString("email");
                            String telephone=obj.getString("telephone");
                            String userid=obj.getString("UserID");
                            String roleid=obj.getString("RoleID");
                            Data.SaveData(getActivity(),result,"enterprise");
                            if(LogicClass.LocalRegistryreg(getActivity(),fullname, telephone, userid, email,roleid))
                            {
                                Intent user = new Intent(getActivity(), MainActivity.class);
                                if(roleid.equals("5"))
                                {
                                    user = new Intent(getActivity(), MainActivity.class);

                                }
                                else
                                {
                                    user = new Intent(getActivity(), BookingActivity.class);

                                }
                                getActivity().startActivity(user);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Login Failed",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    public class SetUpTask extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... arg0) {
            return LogicClass.getTelephoneCodes(getActivity());
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("json",result);
            countryCodes=new ArrayList<>();
            countrylist=new ArrayList<>();

            countryCodes.add("+256");
            countryCodes.add("+254");
            countryCodes.add("+255");

            countrylist.add("Uganda");
            countrylist.add("Kenya");
            countrylist.add("Tanzania");


            Log.e("res",result);
            //Utility.showAlert(result,SlideIndex.this);
            if(!result.startsWith("[{")) {
                //Utility.showAlert(result,getActivity());
            }
            else {
                countryCodes.clear();
                countrylist.clear();
                int x=0;
                try {
                    JSONArray jarray = new JSONArray(result);
                    for (x=0; x<jarray.length();x++) {
                        JSONObject o=jarray.getJSONObject(x);
                        String country=o.getString("name");
                        String code=o.getString("dial_code").trim().replace("+","");
                        String countryCode=o.getString("code");
                        countrylist.add(country);
                        countryCodes.add(code);
                    }
                }
                catch(Exception er)
                {

                }


            }
            int ugindex=countrylist.indexOf("Uganda");
            adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,countrylist);
            countryspinner.setAdapter(adapter);
            countryspinner.setSelection(ugindex);
        }
    }
}