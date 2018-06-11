package net.ddns.zimportant.lovingpeople.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.ddns.zimportant.lovingpeople.R;

public class RegisterCounselorActivity extends AppCompatActivity {

	public static void open(Context context) {
		context.startActivity(new Intent(context, RegisterCounselorActivity.class));
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_counselor);
	}
}
