package com.example.ztrong.lovingpeople.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.ztrong.lovingpeople.R;
import com.example.ztrong.lovingpeople.fragmentintro.FirstSlide;
import com.example.ztrong.lovingpeople.fragmentintro.FourthSlide;
import com.example.ztrong.lovingpeople.fragmentintro.SecondSlide;
import com.example.ztrong.lovingpeople.fragmentintro.ThirdSlide;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

public class IntroActivity extends MaterialIntroActivity {

	public static void open(Context context) {
		context.startActivity(new Intent(context, IntroActivity.class));
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getBackButtonTranslationWrapper()
				.setEnterTranslation(View::setAlpha);

		addSlide(new FirstSlide());
		addSlide(new SecondSlide());
		addSlide(new ThirdSlide());
		addSlide(new FourthSlide());
	}

	@Override
	public void onFinish() {
		//MainActivity.open(this);
		super.onFinish();
	}
}
