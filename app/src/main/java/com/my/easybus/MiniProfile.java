package com.my.easybus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import Entities.Account;
import adapters.AccountAdapter;
import localdata.Data;

/**
 * Created by Richard.Ezama on 12/04/2016.
 */
public class MiniProfile extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<Account>profilelist=new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private AccountAdapter adapter ;
    private String UserID,json;
    private FloatingActionButton fab;

    public MiniProfile() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.miniprofile, container, false);
        rv = (RecyclerView)rootView.findViewById(R.id.rv);
        fab=(FloatingActionButton)rootView.findViewById(R.id.fab);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        // new method for grid layout
        mLayoutManager = new GridLayoutManager(getActivity(), 1);
        rv.setLayoutManager(mLayoutManager);
        new RequestTast().execute();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),UpdateProfile.class);
                startActivity(i);
            }
        });
        return rootView;
    }
    @Override
    public void setMenuVisibility(final boolean visible) {
        if (visible) {
            //((MainActivity)getActivity()).toolbar.setTitle("NWSC HOME");
        }

        super.setMenuVisibility(visible);
    }
    public class RequestTast extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... arg0) {

                profilelist.add(new Account("FullName", Data.fullname(getActivity())));
                profilelist.add(new Account("Email", Data.email(getActivity())));
                profilelist.add(new Account("Telephone", Data.telephone(getActivity())));


            return  null;
        }
        // Show Dialog Box with Progress bar
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            adapter=new AccountAdapter(getActivity(),profilelist);
            //list.setAdapter(adapter);
            //Utility.showAlert(contactlist.size()+" contacts",Saviours.this);
            rv.setAdapter(adapter);
        }

    }

}
