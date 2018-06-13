package net.ddns.zimportant.lovingpeople.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.ChatRoom;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.helper.RequestHelper;
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;

import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.Constant.PARTNER;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.COUNSELOR;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.STORYTELLER;

public class ConversationActivity extends AppCompatActivity {

	public static final int REQUEST_CODE = 1;

	@BindView(R.id.tb_conversation)
	Toolbar toolbar;
	@BindView(R.id.messagesList)
	MessagesList messagesList;
	@BindView(R.id.input)
	MessageInput messageInput;
	@BindView(R.id.bt_connect)
	Button buttonConnect;

	String storytellerId;
	String counselorId;

	Realm realm;
	ChatRoom chatRoom;
	UserChat counselor, storyteller;

	public static void open(Context context, String storyteller, String counselor) {
		Intent i = new Intent(context, ConversationActivity.class);
		i.putExtra(STORYTELLER, storyteller);
		i.putExtra(COUNSELOR, counselor);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpLayout();
		setUpUser();
		setUpRealm();
		setUpConversation();
		setUpButton();
	}

	private void setUpLayout() {
		setContentView(R.layout.activity_conversation);
		ButterKnife.bind(this);
		setUpActionBar();
	}

	private void setUpActionBar() {
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
	}

	private void setUpUser() {
		storytellerId = getIntent().getStringExtra(STORYTELLER);
		counselorId = getIntent().getStringExtra(COUNSELOR);
	}

	private void setUpRealm() {
		realm = Realm.getDefaultInstance();
	}

	private void setUpConversation() {
		storyteller = realm
				.where(UserChat.class)
				.equalTo("id", storytellerId)
				.findFirst();
		counselor = realm
				.where(UserChat.class)
				.equalTo("id", counselorId)
				.findFirst();
		chatRoom = realm
				.where(ChatRoom.class)
				.equalTo("storytellerId", storytellerId)
				.and()
				.equalTo("counselorId", counselorId)
				.findFirst();
	}

	private void setUpButton() {
		if (roomConnected()) {
			buttonConnect.setVisibility(View.GONE);
		} else {
			buttonConnect.setOnClickListener(v -> {
				startActivityRequest();
			});
		}
	}

	private boolean roomConnected() {
		return chatRoom != null
				&& storyteller.getConnectedRoom()
				.equals(chatRoom.getId())
				&& counselor.getConnectedRoom()
				.equals(chatRoom.getId());
	}

	private void startActivityRequest() {
		Intent intent;
		intent = new Intent(this, RequestConversationActivity.class);
		intent.putExtra(PARTNER, getPartnerId());
		startActivityForResult(intent, REQUEST_CODE);
	}

	private String getPartnerId() {
		if (SyncUser.current().getIdentity().equals(storytellerId)) {
			return counselorId;
		} else {
			return storytellerId;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				String error = data.getExtras().getString("error", null);
				if (error != null) {
					AppUtils.showToast(this, error, true);
				} else {
					AppUtils.showToast(this, "Connected", true);
					restartActivity();
				}
			}
		}
	}

	private void restartActivity() {
		recreate();
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}
