package com.example.ztrong.lovingpeople.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.ztrong.lovingpeople.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConversationActivity extends BaseActivity {

	@BindView(R.id.tb_conversation)
	Toolbar toolbar;

	public static void open(Context context) {
		Intent i = new Intent(context, ConversationActivity.class);
		context.startActivity(i);
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
