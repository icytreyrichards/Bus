package com.my.easybus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import BusinessLogic.LogicClass;
import adapters.MenuAdapter;
import adapters.ScheduleAdapter;
import adapters.SpacesItemDecoration;
import utility.Utility;

/**
 * Created by Richard.Ezama on 10/19/2017.
 */

public class HeroActivity extends AppCompatActivity implements View.OnClickListener {
    //private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    //private Button start;
    private RecyclerView rv;
    private MenuAdapter adapter;
    private ArrayList<Entities.Menu> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heropage);
        rv=(RecyclerView)findViewById(R.id.rv);
        StaggeredGridLayoutManager st=new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(st);
        SpacesItemDecoration decoration = new SpacesItemDecoration(1);
        list= LogicClass.getMenuList();
        adapter=new MenuAdapter(this,list);
        rv.setAdapter(adapter);
        // start=(Button)findViewById(R.id.buttonstart);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.menuwhite);
        toolbar.setTitle("Book a Trip");
        setSupportActionBar(toolbar);
        //collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setTitle("Booknow Ug");
        toolbarTextAppernce();
        //start.setOnClickListener(this);
    }
    private void dynamicToolbarColor() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.logo);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
               // collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
                //collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.colorPrimaryDark));
            }
        });
    }

    private void toolbarTextAppernce() {
       // collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        //collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hero, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}
