package com.example.ztrong.lovingpeople.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.example.ztrong.lovingpeople.activity.MainActivity;

/**
 * Singleton Pattern
 */

public class BaseFragment extends Fragment {
	MainActivity getMainActivity() {
		return ((MainActivity) getContext());
	}

	private ActionBarDrawerToggle mToggle = null;

	protected void setUpToolbar(Toolbar toolbar) {
		getMainActivity().setSupportActionBar(toolbar);
		mToggle = getMainActivity().registerDrawerToggle(toolbar);
	}

	@Override
	public void onDestroyView() {
		if (mToggle != null) {
			getMainActivity().unRegisterDrawerToggle(mToggle);
		}
		super.onDestroyView();
	}
}
