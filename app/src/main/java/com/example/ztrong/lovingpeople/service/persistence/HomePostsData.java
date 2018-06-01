package com.example.ztrong.lovingpeople.service.persistence;

import java.util.ArrayList;

public class HomePostsData extends FixturesData {

	public static final int NUM_POST = 100;

	public static ArrayList<String> getHomePosts() {
		ArrayList<String> homePosts = new ArrayList<>();
		for (int i = 0; i <= NUM_POST; ++i) {
			homePosts.add(getRandomHomeContent());
		}
		return homePosts;
	}

	public static ArrayList<String> getColors() {
		ArrayList<String> colors = new ArrayList<>();
		for (int i = 0; i <= NUM_POST; ++i) {
			colors.add(getRandomColor());
		}
		return colors;
	}
}
