package net.ddns.zimportant.lovingpeople.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.helper.UserViewLoader;

public class ProfileCounselorFragment extends ProfileFragment {

	TextView fieldsTextView;
	TextView introduceTextView;
	Button messageButton;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_profile_counselor, container, false);
	}

	@Override
	protected void setUpProfile() {
		// todo
	}
}
