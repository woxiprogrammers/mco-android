package com.android.login_mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.constro360.R;
import com.android.dashboard.DashBoardActivity;
import com.android.models.LoginResponse;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * <b></b>
 * <p>This class is used to </p>
 * Created by Rohit.
 */
public class LoginActivity extends AppCompatActivity implements LoginView {
    private EditText edUserName;
    private EditText edPassword;
    private ProgressBar pbLoad;
    private LoginPresenter mLoginPresenter;

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
        edUserName = (EditText) findViewById(R.id.userName);
        edPassword = (EditText) findViewById(R.id.password);
        pbLoad = (ProgressBar) findViewById(R.id.login_progress);
        mLoginPresenter = new LoginPresenter(this);
        Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPresenter.validateCred(edUserName.getText().toString().trim(), edPassword.getText().toString().trim());
            }
        });
        edPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    mLoginPresenter.validateCred(edUserName.getText().toString().trim(), edPassword.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
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
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults loginResponse = realm.where(LoginResponse.class).findAll();
                    Log.d("Realm", "execute: " + loginResponse);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    @Override
    public void loginFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.onDestroy();
    }
}