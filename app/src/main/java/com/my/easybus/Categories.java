package com.my.easybus;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import BusinessLogic.LogicClass;
import Entities.Topics;
import adapters.TopicAdapter;

@SuppressLint({ "NewApi", "InlinedApi" })
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class Categories extends Fragment {
    private ProgressDialog prgDialog;
    ArrayList<Topics> items = new ArrayList<>();
    ArrayList<Topics> filteritems = new ArrayList<>();
    /////////////recycle view
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rv;
    private LinearLayoutManager llm;
    private TopicAdapter rvadapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.topics,
                container, false);
        prgDialog = new ProgressDialog(getActivity());
        rv = (RecyclerView)rootView.findViewById(R.id.rvcategories);
        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        rv.setLayoutManager(llm);
        RequestTast task=new RequestTast();
        task.execute();
        return rootView;
    }


    public class RequestTast extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            prgDialog.setTitle("Retrieving List");
            prgDialog.setMessage("wait");
            prgDialog.setMax(10000);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            return "";

        }

        // Show Dialog Box with Progress bar
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            prgDialog.dismiss();
            String []cats=getResources().getStringArray(R.array.query_suggestions);
            String []tit=getResources().getStringArray(R.array.query_subs);
            items = LogicClass.getCategories(cats,tit);
            rvadapter = new TopicAdapter(getActivity(), items);
            rv.setAdapter(rvadapter);

        }
    }

}