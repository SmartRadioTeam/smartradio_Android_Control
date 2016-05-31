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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity
{

    private UserLoginTask mAuthTask = null;
    // UI references.
    private TextView mUsernameView;
    private EditText mPasswordView;
    ProgressDialog mpDialog;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try
        {
            sharedPreferences= getSharedPreferences("Hostinfo", Context.MODE_PRIVATE);

        } catch (Exception e)
        {
        }
        public_value.HostURl = sharedPreferences.getString("Hostinfo", null);
        if (public_value.HostURl == null||public_value.HostURl.equals("http://")||public_value.HostURl.equals(""))
        {

            final EditText text=new EditText(this);
            text.setText("http://");
            text.setMaxLines(1);
            AlertDialog alertDialog=new AlertDialog.Builder(this).create();
            alertDialog.setTitle("请输入服务器地址：");
            alertDialog.setView(text);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            if(!text.getText().toString().equals("http://")||!text.getText().toString().equals(""))
                            {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("Hostinfo", text.getText().toString());
                                editor.commit();
                                dialog.dismiss();
                            }
                        }
                    });
            alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK) {
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        return true;
                    }
                    return false;
                }
            });
            alertDialog.show();

        }
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
                        }
                    });
            alertDialog.show();
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

    /**
     * 检测网络是否连接
     *
     * @return
     */
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
                        }
                    });
            alertDialog.show();
        }
        if (mAuthTask != null)
        {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email))
        {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }
        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else
        {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
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

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password)
        {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try
            {
                //// TODO: 2016/5/28
                String result = APIs.Login(mEmail,mPassword);
                //public_value.sessionid = LoginResult.getSessionid();
                public_value.username = mEmail;
            } catch (Exception e)
            {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            mAuthTask = null;
            if (success)
            {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else
            {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
                mpDialog.hide();
            }
        }

        @Override
        protected void onCancelled()
        {
            mAuthTask = null;
        }

    }
}

