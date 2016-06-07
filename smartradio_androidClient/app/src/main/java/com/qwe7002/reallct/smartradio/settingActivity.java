package com.qwe7002.reallct.smartradio;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

public class settingActivity extends AppCompatActivity
{
    Switch s;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(listener);
        toolbar.setNavigationIcon(R.drawable.ic_check_white_36dp);
        s = (Switch) findViewById(R.id.switch1);
        if (public_value.settings.get("permission").getAsString().equals("0"))
        {
            s.setChecked(false);
        } else
        {
            s.setChecked(true);
        }

    }

    private View.OnClickListener listener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            finish();
        }
    };

    @Override
    public void onBackPressed()
    {

        super.onBackPressed();
    }

}
