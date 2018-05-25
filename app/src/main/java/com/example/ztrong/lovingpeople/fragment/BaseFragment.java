package com.example.ztrong.lovingpeople.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Singleton Pattern
 */

public class BaseFragment extends Fragment {
	AppCompatActivity getAppCompatActivity() {
		return ((AppCompatActivity) getContext());
	}
}
