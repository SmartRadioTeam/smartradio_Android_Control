package com.qwe7002.reallct.smartradio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class settingActivity extends AppCompatActivity
{
    Switch s;
    EditText edit;
    Boolean switchchangestate = false;
    Boolean switchstate;

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
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    switchstate = true;
                } else
                {
                    switchstate = false;
                }
                switchchangestate = true;
            }
        });
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
        new startsetsetting().execute();
        return;
    }

    private class startsetsetting extends AsyncTask<Void, Void, Boolean>
    {
        String value;
        int mode = 0;
        String permission;

        @Override
        protected void onPreExecute()
        {

            if (!edit.getText().equals(public_value.settings.get("notice").getAsString()))
            {
                value = edit.getText().toString();
                mode = 1;
            }
            if (switchchangestate)
            {
                if (switchstate)
                {
                    value = "1";
                } else
                {
                    value = "0";
                }
                if (mode == 1)
                {
                    mode = 3;
                } else
                {
                    mode = 2;
                }
            }
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try
            {
                switch (mode)
                {
                    case 1:
                        APIs.setsetting("notice", value);
                        break;
                    case 2:
                        APIs.setsetting("permission", value);
                        break;
                    case 3:
                        APIs.setsetting("notice", value);
                        APIs.setsetting("permission", permission);
                        break;
                }

                return true;
            } catch (Exception e)
            {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean s)
        {
        }
    }
}
