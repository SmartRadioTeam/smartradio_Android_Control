package com.qwe7002.reallct.smartradio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

public class settingActivity extends AppCompatActivity
{
    Switch s;
    EditText edit;

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
        try
        {
            if (public_value.settings.get("permission").getAsString().equals("0"))
            {
                s.setChecked(false);
            } else
            {
                s.setChecked(true);
            }
            edit = (EditText) findViewById(R.id.editText);
            edit.setText(public_value.settings.get("notice").getAsString());
        } catch (Exception e) {}
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
        if (edit.getText().equals(public_value.settings.get("notice").getAsString()))
        {

        }
        super.onBackPressed();
    }

}
