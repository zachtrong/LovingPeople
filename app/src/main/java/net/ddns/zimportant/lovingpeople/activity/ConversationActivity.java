package net.ddns.zimportant.lovingpeople.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.ChatRoom;
import net.ddns.zimportant.lovingpeople.service.utils.AppUtils;

import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.COUNSELOR;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.STORYTELLER;

public class ConversationActivity extends AppCompatActivity {

	@BindView(R.id.tb_conversation)
	Toolbar toolbar;
	@BindView(R.id.messagesList)
	MessagesList messagesList;
	@BindView(R.id.input)
	MessageInput messageInput;

	String storyteller;
	String counselor;

	Realm realm;
	ChatRoom chatRoom;

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
		storyteller = getIntent().getStringExtra(STORYTELLER);
		counselor = getIntent().getStringExtra(COUNSELOR);
	}

	private void setUpRealm() {
		realm = Realm.getDefaultInstance();
	}

	private void setUpConversation() {
		chatRoom = realm
				.where(ChatRoom.class)
				.equalTo("storytellerId", storyteller)
				.and()
				.equalTo("counselorId", counselor)
				.findFirst();

		AppUtils.d(String.valueOf(chatRoom.isValid()));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		realm.close();
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}
}
