package net.ddns.zimportant.lovingpeople.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import net.ddns.zimportant.lovingpeople.R;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConversationActivity extends AppCompatActivity {

	@BindView(R.id.tb_conversation)
	Toolbar toolbar;
	@BindView(R.id.messagesList)
	MessagesList messagesList;
	@BindView(R.id.input)
	MessageInput messageInput;

	public static void open(Context context) {
		Intent i = new Intent(context, ConversationActivity.class);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setUpLayout();
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
}
