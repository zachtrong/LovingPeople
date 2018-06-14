package net.ddns.zimportant.lovingpeople.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.fragment.PrivacyPoliciesFragment;
import net.ddns.zimportant.lovingpeople.fragment.TermsConditionsFragment;
import net.ddns.zimportant.lovingpeople.service.interfaces.OnNotifyRegisterFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity
		implements OnNotifyRegisterFragment {

	@BindView(R.id.tb_register_counselor)
	Toolbar toolbar;
	@BindView(R.id.fl_register_counselor)
	FrameLayout frameLayout;

	FragmentManager fragmentManager;
	ArrayList<Class> fragments = new ArrayList<Class>() {{
		add(PrivacyPoliciesFragment.class);
		add(TermsConditionsFragment.class);
	}};
	private int fragmentIndex = 0;

	public static void open(Context context) {
		context.startActivity(new Intent(context, RegisterActivity.class));
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_counselor);
		setUpLayout();
		setUpFragment();
	}

	private void setUpLayout() {
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public boolean onSupportNavigateUp() {
		onBackPressed();
		return true;
	}

	private void setUpFragment() {
		fragmentManager = getSupportFragmentManager();
		onNextFragment();
	}

	@Override
	public void onNextFragment() {
		try {
			if (fragmentIndex == fragments.size()) {
				finish();
				return;
			}
			fragmentManager.beginTransaction()
					.replace(R.id.fl_register_counselor,
							(Fragment) fragments
									.get(fragmentIndex)
									.newInstance())
					.commit();
			++fragmentIndex;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		int numFragment = fragmentManager.getBackStackEntryCount();

		if (numFragment == 0) {
			super.onBackPressed();
		} else {
			--fragmentIndex;
			fragmentManager.popBackStack();
		}
	}
}
