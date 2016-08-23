package com.qwe7002.reallct.smartradio;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class settingActivity extends AppCompatActivity {
    Switch s;
    EditText edit;
    Boolean switchchangestate = false;
    Boolean switchstate;
    ProgressDialog mpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(listener);
        toolbar.setNavigationIcon(R.drawable.ic_check_white_36dp);
        s = (Switch) findViewById(R.id.switch1);
        try {
            if (public_value.settings.get("permission").getAsString().equals("0")) {
                s.setChecked(false);
            } else {
                s.setChecked(true);
            }
            edit = (EditText) findViewById(R.id.Setting_notify);
            edit.setText(public_value.settings.get("notice").getAsString());
        } catch (Exception e) {
        }
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchstate = isChecked;
                switchchangestate = true;
            }
        });
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new startsetsetting().execute();
        }
    };

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog
                .setTitle("提示")
                .setMessage("您是否要保存当前操作？")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new startsetsetting().execute();
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setCancelable(false).create().show();

    }

    private class startsetsetting extends AsyncTask<Void, Void, Boolean> {
        String value;
        int mode = 0;
        String permission;

        @Override
        protected void onPreExecute() {
            mpDialog = new ProgressDialog(settingActivity.this);
            mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mpDialog.setTitle("正在连接服务器...");
            mpDialog.setMessage("正在获取数据，请稍后...");
            mpDialog.setIndeterminate(false);
            mpDialog.setCancelable(false);
            mpDialog.show();
            if (!edit.getText().equals(public_value.settings.get("notice").getAsString())) {
                value = edit.getText().toString();
                mode = 1;
            }
            if (switchchangestate) {
                if (switchstate){
                    permission = "1";
                } else {
                    permission = "0";
                }
                if (mode == 1) {
                    mode = 3;
                } else {
                    mode = 2;
                }
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                switch (mode) {
                    case 1:
                        Log.i("setnotece", APIs.setsetting("notice", value));
                        break;
                    case 2:
                        Log.i("setpermission", APIs.setsetting("permission", permission));
                        break;
                    case 3:
                        Log.i("setnotece", APIs.setsetting("notice", value));
                        Log.i("setpermisson", APIs.setsetting("permission", permission));
                        break;
                }
                JsonParser parser = new JsonParser();
                JsonObject jsonobj = (JsonObject) parser.parse(APIs.getsetting());
                public_value.settings = jsonobj.get("settings").getAsJsonObject();
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean s) {
            mpDialog.cancel();
            finish();
        }
    }
}
