package net.ddns.zimportant.lovingpeople.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.UserViewLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_CANCEL;
import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_NOT_AVAILABLE;
import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_NOT_BOTH_COUNSELOR;
import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_NOT_BOTH_STORYTELLER;
import static net.ddns.zimportant.lovingpeople.service.Constant.ERR_USER_NOT_REQUEST_MORE;
import static net.ddns.zimportant.lovingpeople.service.Constant.PARTNER;
import static net.ddns.zimportant.lovingpeople.service.Constant.TIMEOUT;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.COUNSELOR;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.STORYTELLER;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_ONLINE;

public class RequestActivity extends AppCompatActivity {

	@BindView(R.id.bt_cancel)
	Button buttonCancel;
	@BindView(R.id.tv_name)
	TextView nameTextView;
	@BindView(R.id.civ_avatar)
	CircleImageView avatar;
	@BindView(R.id.tv_timeout_second)
	TextView timeout;

	private Realm realm;
	private String storytellerId, counselorId;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_conversation);
		ButterKnife.bind(this);
		setUpRealm();
		prepareInformation();
		prepareRealmData();
	}

	private void setUpRealm() {
		realm = Realm.getDefaultInstance();
	}

	private void prepareInformation() {
		storytellerId = getIntent().getExtras().getString(COUNSELOR);
		counselorId = getIntent().getExtras().getString(STORYTELLER);
	}

	private void prepareRealmData() {
		RealmResults<UserChat> partner = realm
				.where(UserChat.class)
				.equalTo("id", getPartnerId())
				.findAllAsync();
		UserViewLoader userViewLoader = new UserViewLoader.Builder(partner)
				.setAvatarView(avatar)
				.setNameView(nameTextView)
				.build();
		userViewLoader.startListening();
	}

	private void cancelRequest(String error) {
		Intent i = new Intent();
		i.putExtra("error", error);
		setResult(Activity.RESULT_OK, i);
		finish();
	}

	private void setUpButtonCancel() {
		timeout.setText(String.valueOf(TIMEOUT));
		/*
		countDownTimer =
				new CountDownTimer(TIMEOUT * 1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				timeout.setText(String.valueOf(millisUntilFinished / 1000).concat("s"));
			}

			@Override
			public void onFinish() {
				cancelRequest(ERR_USER_CANCEL);
			}
		}.start();
		*/
	}

	private void finishActivitySuccess() {
		Intent i = new Intent();
		i.putExtra("error", (String) null);
		setResult(Activity.RESULT_OK, i);
		finish();
	}

	@Override
	public void onBackPressed() {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}

	private String getUserId() {
		return SyncUser.current().getIdentity();
	}

	private String getPartnerId() {
		if (SyncUser.current().getIdentity().equals(storytellerId)) {
			return counselorId;
		} else {
			return storytellerId;
		}
	}
}
