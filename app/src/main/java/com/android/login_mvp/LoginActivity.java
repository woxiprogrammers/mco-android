package com.android.login_mvp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.BaseActivity;
import com.android.constro360.R;
import com.android.dashboard.DashBoardActivity;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class LoginActivity extends BaseActivity implements LoginView {
    private EditText edUserName;
    private EditText edPassword;
    private ProgressBar pbLoad;
    private LoginPresenter mLoginPresenter;
    private Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        //Calling function to initialize required views.
        initializeViews();
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        edUserName = findViewById(R.id.mobileNumber);
        edPassword = findViewById(R.id.password);
        pbLoad = findViewById(R.id.login_progress);
        mLoginPresenter = new LoginPresenter(this);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignIn.setEnabled(false);
                mLoginPresenter.validateCred(edUserName.getText().toString().trim(), edPassword.getText().toString().trim());
            }
        });
        edPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                /*if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    mLoginPresenter.validateCred(edUserName.getText().toString().trim(), edPassword.getText().toString().trim());
                    return true;
                }
                return false;*/
                ////////////////
                // If triggered by an enter key, this is the event; otherwise, this is null.
                btnSignIn.setEnabled(false);
                if (keyEvent != null) {
                    // if shift key is down, then we want to insert the '\n' char in the TextView;
                    // otherwise, the default action is to send the message.
                    if (!keyEvent.isShiftPressed()) {
                        mLoginPresenter.validateCred(edUserName.getText().toString().trim(), edPassword.getText().toString().trim());
                        return true;
                    }
                    return false;
                }
                mLoginPresenter.validateCred(edUserName.getText().toString().trim(), edPassword.getText().toString().trim());
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.onDestroy();
    }

    @Override
    public void showProgress() {
        edUserName.setEnabled(false);
        edPassword.setEnabled(false);
        pbLoad.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        edUserName.setEnabled(true);
        edPassword.setEnabled(true);
        pbLoad.setVisibility(View.GONE);
    }

    @Override
    public void setUserNameEmptyError(String strError) {
        edUserName.requestFocus();
        edUserName.setError(strError);
    }

    @Override
    public void setPasswordEmptyError(String strError) {
        edPassword.requestFocus();
        edPassword.setError(strError);
    }

    @Override
    public void setUserNameValidationError(String strError) {
        edUserName.requestFocus();
        edUserName.setError(strError);
    }

    @Override
    public void setPasswordValidationError(String strError) {
        edPassword.requestFocus();
        edPassword.setError(strError);
    }

    @Override
    public void loginSuccess(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
        finish();
    }

    @Override
    public void loginFailure(String message) {
        btnSignIn.setEnabled(true);
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}