package net.ddns.zimportant.lovingpeople.service.persistence;

import android.graphics.Color;

import java.util.ArrayList;

public class HomePostsData extends FixturesData {

	public static final int NUM_POST = 100;
	private static ArrayList<String> colors = getColors();

	public static ArrayList<String> getHomePosts() {
		ArrayList<String> homePosts = new ArrayList<>();
		for (int i = 0; i <= NUM_POST; ++i) {
			homePosts.add(getRandomHomeContent());
		}
		return homePosts;
	}

	public static int getColor() {
		return Color.parseColor(getRandomColor());
	}

	public static int getColor(int position) {
		return Color.parseColor(colors.get(position % colors.size()));
	}

	public static ArrayList<String> getColors() {
		ArrayList<String> colors = new ArrayList<>();
		for (int i = 0; i <= NUM_POST; ++i) {
			colors.add(getRandomColor());
		}
		return colors;
	}
}
