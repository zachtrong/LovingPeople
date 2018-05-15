package com.example.ztrong.lovingpeople.activity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.transition.TransitionManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.ztrong.lovingpeople.R;
import com.example.ztrong.lovingpeople.service.model.User;
import com.example.ztrong.lovingpeople.service.utils.InputChecker;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.ObjectServerError;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

import static com.example.ztrong.lovingpeople.service.messenger.Constant.AUTH_URL;

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.sp_bt_signin_welcome)
    ActionProcessButton signInButton;

    @BindView(R.id.sp_bt_signup_welcome)
    ActionProcessButton signUpButton;

    @BindView(R.id.et_user_welcome)
    EditText usernameEditText;

    @BindView(R.id.et_pass_welcome)
    EditText passwordEditText;

    @BindView(R.id.et_pass_confirm_welcome)
    EditText passwordConfirmEditText;

    @BindView(R.id.til_pass_confirm_welcome)
    TextInputLayout passwordConfirmTextInputLayout;

    @BindView(R.id.bt_signin_back_welcome)
    Button signInBackButton;

    @BindView(R.id.root_welcome)
    ViewGroup transitionsContainer;

    boolean isSignIn = true;
    ActionProcessButton buttonLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

        signUpButton.setOnClickListener(signUpOnClickListener);
        signInBackButton.setOnClickListener(signInBackListener);
        signInButton.setOnClickListener(signInListener);
        signInButton.setMode(ActionProcessButton.Mode.ENDLESS);
        signUpButton.setMode(ActionProcessButton.Mode.ENDLESS);
    }

    private void changeViewGoneOrVisible() {
        TransitionManager.beginDelayedTransition(transitionsContainer);
        signInButton.setVisibility(isSignIn ? View.GONE : View.VISIBLE);
        passwordConfirmTextInputLayout.setVisibility(isSignIn ? View.VISIBLE : View.GONE);
        signInBackButton.setVisibility(isSignIn ? View.VISIBLE : View.GONE);

        isSignIn = !isSignIn;
    }

    private User getUserDataFromView() {
        User user = new User();
        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());
        return user;
    }

    private void getCredential(boolean isSignIn) {
        User user = getUserDataFromView();
        InputChecker inputChecker = getInputChecker(user);

        if (inputChecker.isCancel()) {
            inputChecker.getFocusView().requestFocus();
        } else {
            checkCredential(user.getUsername(), user.getPassword(), isSignIn);
        }
    }

    private InputChecker getInputChecker(User user) {
        InputChecker inputChecker = null;
        InputChecker currentChecker;
        currentChecker = checkPasswordConfirm(user, passwordConfirmEditText);
        if (currentChecker != null) inputChecker = currentChecker;
        currentChecker = checkPassword(user, passwordEditText);
        if (currentChecker != null) inputChecker = currentChecker;
        currentChecker = checkUsername(user, usernameEditText);
        if (currentChecker != null) inputChecker = currentChecker;
        if (inputChecker == null) inputChecker = new InputChecker();
        return inputChecker;
    }

    private InputChecker checkPasswordConfirm(User user, EditText passwordConfirmEditText) {
        InputChecker inputChecker = null;
        if (!isSignIn) {
            String passwordConfirm = passwordConfirmEditText.getText().toString();
            if (!user.getPassword().equals(passwordConfirm)) {
                passwordConfirmEditText.setError(getString(R.string.error_invalid_matching));
                inputChecker = new InputChecker(true, passwordConfirmEditText);
            }
        }
        return inputChecker;
    }

    private InputChecker checkPassword(User user, EditText passwordEditText) {
        InputChecker inputChecker = null;
        if (TextUtils.isEmpty(user.getPassword()) || !isPasswordValid(user.getPassword())) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            inputChecker = new InputChecker(true, passwordEditText);
        }
        return inputChecker;
    }

    private InputChecker checkUsername(User user, EditText usernameEditText) {
        InputChecker inputChecker = null;
        if (TextUtils.isEmpty(user.getUsername()) || !isUsernameValid(user.getUsername())) {
            usernameEditText.setError(getString(R.string.error_invalid_username));
            inputChecker = new InputChecker(true, usernameEditText);
        }
        return inputChecker;
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkCredential(String username, String password, boolean isSignIn) {
        setButtonLoading(isSignIn ? signInButton : signUpButton);
        doUserConnect(username, password, isSignIn);
    }

    private void doUserConnect(String username, String password, boolean isSignIn) {
        SyncCredentials mCredentials = SyncCredentials
                .usernamePassword(username, password, !isSignIn);
        SyncUser.logInAsync(mCredentials, AUTH_URL, syncUserCallback);
    }

    private void setButtonLoading(ActionProcessButton button) {
        buttonLoading = button;
        buttonLoading.setProgress(1);
    }

    private SyncUser.Callback<SyncUser> syncUserCallback = new SyncUser.Callback<SyncUser>() {
        @Override
        public void onSuccess(@NonNull SyncUser result) {
            runOnUiThread(() -> {
                buttonLoading.setProgress(100);
                goToMainActivity();
            });
        }

        @Override
        public void onError(@NonNull ObjectServerError error) {
            runOnUiThread(() -> {
                buttonLoading.setProgress(-1);
            });
        }
    };

    private boolean isUsernameValid(String username) {
        return username.length() <= 20;
    }

    private boolean isPasswordValid(String password) {
        return password.length() <= 20;
    }

    private View.OnClickListener signInListener = v -> {
        if (isSignIn) {
            getCredential(isSignIn);
        }
    };

    private View.OnClickListener signUpOnClickListener = v -> {
        if (isSignIn) {
            changeViewGoneOrVisible();
        } else {
            getCredential(isSignIn);
        }
    };

    private View.OnClickListener signInBackListener = v -> {
        if (!isSignIn) {
            changeViewGoneOrVisible();
        } else {
            getCredential(isSignIn);
        }
    };
}
