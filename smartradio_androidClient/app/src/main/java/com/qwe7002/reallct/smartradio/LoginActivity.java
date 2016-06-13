package com.qwe7002.reallct.smartradio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class LoginActivity extends AppCompatActivity
{

    private UserLoginTask mAuthTask = null;
    private TextView mUsernameView;
    private EditText mPasswordView;
    ProgressDialog mpDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (!isNetConnected())
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("请链接网络");
            alertDialog.setMessage("没有检测到可用网络连接，请检查您的网络设置！");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                            finish();
                        }
                    });
            alertDialog.show();
        } else
        {
            try
            {
                sharedPreferences = getSharedPreferences("Hostinfo", Context.MODE_PRIVATE);

            } catch (Exception e)
            {

            }
                public_value.HostURl = sharedPreferences.getString("Hostinfo", null);
                if (public_value.HostURl == null || public_value.HostURl.equals("http://") || public_value.HostURl.equals(""))
                {
                    sethosturl();
                } else
                {
                    new getsettings().execute();
                }


        }

        mUsernameView = (TextView) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == R.id.login || id == EditorInfo.IME_NULL)
                {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mUsernameView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == EditorInfo.IME_ACTION_GO)
                {
                    mPasswordView.requestFocus();
                    return true;
                }
                return false;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                attemptLogin();
            }
        });
    }

    private void sethosturl()
    {
        final EditText text = new EditText(this);
        text.setText("http://");
        text.setHint("服务器地址");
        text.setMaxLines(1);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("请输入服务器地址：");
        alertDialog.setView(text);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        public_value.HostURl = text.getText().toString();
                        dialog.dismiss();
                        new getsettings().execute();
                    }
                });
        alertDialog.show();
    }

    private class getsettings extends AsyncTask<Void, Void, Boolean>
    {
        String projectname;

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try
            {
                JsonParser parser = new JsonParser();
                JsonObject jsonobj = (JsonObject) parser.parse(APIs.getsetting());
                public_value.settings = jsonobj.get("settings").getAsJsonObject();
                projectname = public_value.settings.get("projectname").getAsString();
                return true;
            } catch (Exception e)
            {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean s)
        {
            mpDialog.cancel();
            if (s)
            {
                setTitle(projectname);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Hostinfo", public_value.HostURl);
                editor.commit();

            } else
            {
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("输入的地址错误");
                alertDialog.setMessage("您输入的服务器地址无法连接，请重新输入!");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                sethosturl();
                            }
                        });
                alertDialog.show();

            }
            super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mpDialog = new ProgressDialog(LoginActivity.this);
            mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mpDialog.setTitle("正在连接服务器...");
            mpDialog.setMessage("获取配置文件中，请稍后...");
            mpDialog.setIndeterminate(false);
            mpDialog.setCancelable(false);
            mpDialog.show();
        }
    }

    private boolean isNetConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null)
        {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null)
            {
                for (NetworkInfo ni : infos)
                {
                    if (ni.isConnected())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void attemptLogin()
    {
        if (!isNetConnected() || public_value.settings == null)
        {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("请链接网络");
            alertDialog.setMessage("没有检测到可用网络连接，请检查您的网络设置！");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        if (mAuthTask != null)
        {
            return;
        }

        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email))
        {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }
        if (cancel)
        {
            focusView.requestFocus();
        } else
        {
            showProgress();
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password)
    {
        return password.length() > 4;
    }

    private void showProgress()
    {
        mpDialog = new ProgressDialog(this);
        mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mpDialog.setTitle("正在连接服务器...");
        mpDialog.setMessage("登录中，请稍后...");
        mpDialog.setIndeterminate(false);
        mpDialog.setCancelable(false);
        mpDialog.show();
    }

    public class UserLoginTask extends AsyncTask<Void, Void, String>
    {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password)
        {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params)
        {
            try
            {
                String result = APIs.Login(mEmail, mPassword);
                return result;
            } catch (Exception e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result)
        {
            mpDialog.cancel();
            mAuthTask = null;
            if (result != null)
            {
                try
                {
                    JsonParser parser = new JsonParser();
                    JsonObject Jobj = (JsonObject) parser.parse(result);
                    Log.i("result", result);
                    if (Jobj.get("mode").getAsString().equals("success"))
                    {
                        public_value.sessionid = Jobj.get("authkey").getAsString();
                        public_value.username = mEmail;
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else
                    {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }
                } catch (Exception e)
                {

                    Log.e("loginerror", e.toString());
                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                    alertDialog.setTitle("服务器错误");
                    alertDialog.setMessage("服务器异常!请检查连接!");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                }
                            });
                    alertDialog.show();
                }
            }
        }

        @Override
        protected void onCancelled()
        {
            mAuthTask = null;
        }

    }
}

