package net.ddns.zimportant.lovingpeople.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.adapter.MessagesListRealmAdapter;
import net.ddns.zimportant.lovingpeople.service.common.model.ChatRoom;
import net.ddns.zimportant.lovingpeople.service.common.model.Message;
import net.ddns.zimportant.lovingpeople.service.common.model.UserChat;
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncUser;

import static net.ddns.zimportant.lovingpeople.service.Constant.COUNSELOR_ID;
import static net.ddns.zimportant.lovingpeople.service.Constant.PARTNER;
import static net.ddns.zimportant.lovingpeople.service.Constant.STORYTELLER_ID;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.COUNSELOR;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.STORYTELLER;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_ONLINE;

public class ConversationActivity extends AppCompatActivity
		implements MessageInput.InputListener {

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
	Disposable checkChatRoom;
	ChatRoom chatRoom;
	UserChat counselor, storyteller;
	MessagesListRealmAdapter realmAdapter;
	ImageLoader imageLoader;
	boolean isLoadedView = false;

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
		setUpRealm();
		setUpUser();
		setUpChatRoom();
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
		storyteller = realm
				.where(UserChat.class)
				.equalTo("id", storytellerId)
				.findFirst();
		counselorId = getIntent().getStringExtra(COUNSELOR);
		counselor = realm
				.where(UserChat.class)
				.equalTo("id", counselorId)
				.findFirst();
		if (storytellerId.equals(getPartnerId())) {
			getSupportActionBar().setTitle(storyteller.getName());
		} else {
			getSupportActionBar().setTitle(counselor.getName());
		}
	}

	private void setUpRealm() {
		realm = Realm.getDefaultInstance();
	}

	private void setUpChatRoom() {
		checkChatRoom = realm
				.where(ChatRoom.class)
				.equalTo(STORYTELLER_ID, storytellerId)
				.and()
				.equalTo(COUNSELOR_ID, counselorId)
				.findAllAsync()
				.asFlowable()
				.filter(RealmResults::isLoaded)
				.subscribe(realmResults -> {
					if (realmResults.size() != 0) {
						chatRoom = realmResults.first();
						if (!isLoadedView) {
							isLoadedView = true;
							setUpMessages();
							setUpButton();
						}
					} else {
						if (!isLoadedView) {
							isLoadedView = true;
							setUpButton();
						}
					}
				});
	}

	private void setUpMessages() {
		setUpAdapter();
		setUpInput();
	}

	private void setUpAdapter() {
		imageLoader = (imageView, url) -> {
			Picasso.get().load(url).into(imageView);
		};
		realmAdapter = new MessagesListRealmAdapter(
				SyncUser.current().getIdentity(),
				chatRoom.getMessages()
		);
		//realmAdapter.enableSelectionMode(this);
		messagesList.setAdapter(realmAdapter.getListAdapter());
	}

	private void setUpInput() {
		messageInput.setInputListener(this);
	}

	private void setUpButton() {
		if (isChatRoomConnected()) {
			buttonConnect.setVisibility(View.GONE);
		} else {
			buttonConnect.setOnClickListener(v -> {
				startActivityRequest();
			});
		}
	}

	private boolean isChatRoomConnected() {
		return chatRoom != null
				&& storyteller.getConnectedRoom()
				.equals(chatRoom.getId())
				&& counselor.getConnectedRoom()
				.equals(chatRoom.getId());
	}

	private void startActivityRequest() {
		Intent intent;
		intent = new Intent(this, RequestActivity.class);
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

	@Override
	public boolean onSubmit(CharSequence input) {
		if (!isChatRoomConnected()) {
			restartActivity();
			return false;
		}
		Message message = new Message(
				input.toString(),
				SyncUser.current().getIdentity()
		);
		realm.executeTransaction(bgRealm -> {
			chatRoom
					.getMessages()
					.add(message);
		});
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_conversation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_disconnect) {
			disconnectChatRoom();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void disconnectChatRoom() {
		if (isChatRoomConnected()) {
			realm.executeTransaction(bgRealm -> {
				storyteller.setUserRequestId("");
				storyteller.setConnectedRoom("");
				storyteller.setStatus(USER_ONLINE);
				counselor.setUserRequestId("");
				counselor.setConnectedRoom("");
				counselor.setStatus(USER_ONLINE);
			});
			restartActivity();
		}
	}
}
