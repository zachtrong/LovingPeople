package net.ddns.zimportant.lovingpeople.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.RealmHelper;
import net.ddns.zimportant.lovingpeople.service.utils.InputChecker;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.ObjectServerError;
import io.realm.Realm;
import io.realm.SyncCredentials;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.Constant.AUTH_URL;

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

	boolean isSignInScreen = true;
	boolean isLockSwitchScreen = false;
	ActionProcessButton buttonLoading;

	public static void open(Context context) {
		context.startActivity(new Intent(context, WelcomeActivity.class));
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		if (SyncUser.current() != null) {
			MainActivity.open(this);
			finish();
		}
		ButterKnife.bind(this);

        signUpButton.setOnClickListener(signUpOnClickListener);
        signInBackButton.setOnClickListener(signInBackListener);
        signInButton.setOnClickListener(signInListener);
        signInButton.setMode(ActionProcessButton.Mode.ENDLESS);
        signUpButton.setMode(ActionProcessButton.Mode.ENDLESS);
	}

	private View.OnClickListener signUpOnClickListener = v -> {
		if (!isLockSwitchScreen) {
			if (isSignInScreen) {
				changeViewGoneOrVisible();
			} else {
				getCredential();
			}
		}
	};

	private View.OnClickListener signInBackListener = v -> {
		if (!isLockSwitchScreen) {
			if (!isSignInScreen) {
				changeViewGoneOrVisible();
			} else {
				getCredential();
			}
		}
	};

	private View.OnClickListener signInListener = v -> {
		if (!isLockSwitchScreen) {
			if (isSignInScreen) {
				getCredential();
			}
		}
	};


	private void changeViewGoneOrVisible() {
		TransitionManager.beginDelayedTransition(transitionsContainer);
		signInButton.setVisibility(isSignInScreen ? View.GONE : View.VISIBLE);
		passwordConfirmTextInputLayout.setVisibility(isSignInScreen ? View.VISIBLE : View.GONE);
		signInBackButton.setVisibility(isSignInScreen ? View.VISIBLE : View.GONE);

		isSignInScreen = !isSignInScreen;
	}

	private void getCredential() {
		isLockSwitchScreen = true;
		UserData user = getUserDataFromView();
		InputChecker inputChecker = getFailInputChecker(user);

		if (inputChecker != null && inputChecker.isCancel()) {
			inputChecker.getFocusView().requestFocus();
			isLockSwitchScreen = false;
		} else {
			checkCredential(user.getUsername(), user.getPassword());
		}
	}

	private UserData getUserDataFromView() {
		return new UserData(
				usernameEditText.getText().toString(),
				passwordEditText.getText().toString()
		);
	}

	private InputChecker getFailInputChecker(UserData user) {
		InputChecker inputChecker = null;
		InputChecker currentChecker;
		currentChecker = checkPasswordConfirm(user, passwordConfirmEditText);
		if (currentChecker != null) inputChecker = currentChecker;
		currentChecker = checkPassword(user, passwordEditText);
		if (currentChecker != null) inputChecker = currentChecker;
		currentChecker = checkUsername(user, usernameEditText);
		if (currentChecker != null) inputChecker = currentChecker;
		return inputChecker;
	}

	private InputChecker checkPasswordConfirm(UserData user, EditText passwordConfirmEditText) {
		InputChecker inputChecker = null;
		if (!isSignInScreen) {
			String passwordConfirm = passwordConfirmEditText.getText().toString();
			if (!user.getPassword().equals(passwordConfirm)) {
				passwordConfirmEditText.setError(getString(R.string.error_invalid_matching));
				inputChecker = new InputChecker(true, passwordConfirmEditText);
			}
		}
		return inputChecker;
	}

	private InputChecker checkPassword(UserData user, EditText passwordEditText) {
		InputChecker inputChecker = null;
		if (TextUtils.isEmpty(user.getPassword()) || !isPasswordValid(user.getPassword())) {
			passwordEditText.setError(getString(R.string.error_invalid_password));
			inputChecker = new InputChecker(true, passwordEditText);
		}
		return inputChecker;
	}

	private InputChecker checkUsername(UserData user, EditText usernameEditText) {
		InputChecker inputChecker = null;
		if (TextUtils.isEmpty(user.getUsername()) || !isUsernameValid(user.getUsername())) {
			usernameEditText.setError(getString(R.string.error_invalid_username));
			inputChecker = new InputChecker(true, usernameEditText);
		}
		return inputChecker;
	}

	private void checkCredential(String username, String password) {
		setButtonLoading(isSignInScreen ? signInButton : signUpButton);
		doUserConnect(username, password);
	}

	private void setButtonLoading(ActionProcessButton button) {
		buttonLoading = button;
		buttonLoading.setProgress(1);
	}

	private void doUserConnect(String username, String password) {
		SyncCredentials mCredentials = SyncCredentials
				.usernamePassword(username, password, !isSignInScreen);
		SyncUser.logInAsync(mCredentials, AUTH_URL, syncUserCallback);
	}

	private SyncUser.Callback<SyncUser> syncUserCallback = new SyncUser.Callback<SyncUser>() {
		@Override
		public void onSuccess(@NonNull SyncUser result) {
			runOnUiThread(() -> {
				buttonLoading.setProgress(100);
				if (!isSignInScreen) {
					createUser();
				}
				isLockSwitchScreen = false;
				MainActivity.open(WelcomeActivity.this);
			});
		}

		@Override
		public void onError(@NonNull ObjectServerError error) {
			runOnUiThread(() -> {
				isLockSwitchScreen = false;
				buttonLoading.setProgress(-1);
			});
		}
	};

	private void createUser() {
		RealmHelper.setUpDefaultRealm();
		Realm realm = Realm.getDefaultInstance();
		realm.executeTransaction(bgRealm -> {
			bgRealm.insert(new UserChat(SyncUser.current().getIdentity()));
		});
		realm.close();
	}

	private boolean isUsernameValid(String username) {
		return username.length() <= 20;
	}

	private boolean isPasswordValid(String password) {
		return password.length() <= 20;
	}

	public class UserData {
		private String username;
		private String password;

		UserData(String username, String password) {
			this.username = username;
			this.password = password;
		}

		String getUsername() {
			return username;
		}

		void setUsername(String username) {
			this.username = username;
		}

		String getPassword() {
			return password;
		}

		void setPassword(String password) {
			this.password = password;
		}
	}
}
