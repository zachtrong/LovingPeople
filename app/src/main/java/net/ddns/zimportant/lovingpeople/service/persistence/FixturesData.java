package net.ddns.zimportant.lovingpeople.service.persistence;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.UUID;

import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_BUSY;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_OFFLINE;
import static net.ddns.zimportant.lovingpeople.service.common.model.UserChat.USER_ONLINE;

/*
 * Created by Anton Bevza on 1/13/17.
 */
public class FixturesData {

	static SecureRandom rnd = new SecureRandom();

	public static ArrayList<String> getHomePostContentData() {
		return homePostContentData;
	}

	private static ArrayList<String> homePostContentData = new ArrayList<String>() {
		{
			add("Depression distorts your thinking. When you are depressed, your mind can play tricks on you. If you have thoughts of suicide, please call someone immediately. Don't let a temporary glitch in your thinking cause you to harm yourself or another.");
			add("Depression makes it hard to give. It's very hard to think of other people when you're wrapped in a prickly blanket of sadness, and all you can think about is your own pain. Be proactive and just a few steps you need to heal. Try reading a book to help you understand what you are going through and how best to deal with it.");
			add("Depression is experienced as anxiety 65 percent of the time. Make sure you get an accurate diagnosis, so you can get the most effective treatment available. There is a saying in psychotherapy, \"No pills without skills.\" If you are taking medication you should also be getting therapy.");
			add("Persistent irritability can be a symptom of depression. If the world, your life, or your loved ones constantly tick you off, the cause might be something that's going on inside of you. That anger can lead to lashing out or withdrawing from those who love you. Neither one will get you what you need.");
			add("Chronic pain can be another symptom of depression. At the same time, being in continual discomfort can cause you to become depressed. When you are depressed and in pain, it can be hard to know which came first.");
			add("Alcohol is a depressant. So are marijuana and a host of other recreational or street drugs. Self-medication is not going to get you better and will surely make you worse over time. Remember that all medications, including anti-depressants, have side effects.");
			add("People don't choose to be depressed, but they do make a choice about how to deal with it. You can choose to do nothing, but denying that you have a problem will only make you feel worse. Choose to just make one step, just one and if it feels okay, try it again. That's how many people get through it.");
			add("The origin of depression can be situational and/or bio-chemical. If you are experiencing mild to moderate situational depression (resulting from the loss of a job, for example), counseling will help you. Most bio-chemical depressions that are moderate to severe are best treated with a combination of medicine and psychotherapy.");
			add("Depression can be as hard on your loved ones as it is on you. Those closest to you may start to feel unloved, and may distance themselves so they aren't pulled into your pain. Remember that others are counting on you");
			add("Exercise is the easiest and least expensive cure for depression. Just walking 30 minutes a day will help you and sometimes completely alleviate your symptoms. For this very reason, many therapists take walks with clients instead of doing \"couch time.\"");
		}
	};

	static ArrayList<String> colors = new ArrayList<String>() {
		{
			add("#f44336");
			add("#e91e63");
			add("#9c27b0");
			add("#673ab7");
			add("#2196f3");
			add("#009688");
			add("#4caf50");
			add("#ff9800");
			add("#ff5722");
			add("#607d8b");
		}
	};

	static ArrayList<String> avatars = new ArrayList<String>() {
		{
			add("http://i.imgur.com/pv1tBmT.png");
			add("http://i.imgur.com/R3Jm1CL.png");
			add("http://i.imgur.com/ROz4Jgh.png");
			add("http://i.imgur.com/Qn9UesZ.png");
		}
	};

	static final ArrayList<String> groupChatImages = new ArrayList<String>() {
		{
			add("http://i.imgur.com/hRShCT3.png");
			add("http://i.imgur.com/zgTUcL3.png");
			add("http://i.imgur.com/mRqh5w1.png");
		}
	};

	static final ArrayList<String> groupChatTitles = new ArrayList<String>() {
		{
			add("Samuel, Michelle");
			add("Jordan, Jordan, Zoe");
			add("Julia, Angel, Kyle, Jordan");
		}
	};

	static final ArrayList<String> names = new ArrayList<String>() {
		{
			add("Samuel Reynolds");
			add("Kyle Hardman");
			add("Zoe Milton");
			add("Angel Ogden");
			add("Zoe Milton");
			add("Angelina Mackenzie");
			add("Kyle Oswald");
			add("Abigail Stevenson");
			add("Julia Goldman");
			add("Jordan Gill");
			add("Michelle Macey");
		}
	};

	static final ArrayList<String> messages = new ArrayList<String>() {
		{
			add("Hello!");
			add("This is my phone number - +1 (234) 567-89-01");
			add("Here is my e-mail - myemail@example.com");
			add("Hey! Check out this awesome link! www.github.com");
			add("Hello! No problem. I can today at 2 pm. And after we can go to the office.");
			add("At first, for some time, I was not able to answer him one word");
			add("At length one of them called out in a clear, polite, smooth dialect, not unlike in sound to the Italian");
			add("By the bye, Bob, said Hopkins");
			add("He made his passenger captain of one, with four of the men; and himself, his mate, and five more, went in the other; and they contrived their business very well, for they came up to the ship about midnight.");
			add("So saying he unbuckled his baldric with the bugle");
			add("Just then her head struck against the roof of the hall: in fact she was now more than nine feet high, and she at once took up the little golden key and hurried off to the garden door.");
		}
	};

	static final ArrayList<String> images = new ArrayList<String>() {
		{
			add("https://habrastorage.org/getpro/habr/post_images/e4b/067/b17/e4b067b17a3e414083f7420351db272b.jpg");
			add("http://www.designboom.com/wp-content/uploads/2015/11/stefano-boeri-architetti-vertical-forest-residential-tower-lausanne-switzerland-designboom-01.jpg");
		}
	};

	static final ArrayList<String> status = new ArrayList<String>() {
		{
			add(USER_OFFLINE);
			add(USER_BUSY);
			add(USER_ONLINE);
		}
	};

	static String getRandomId() {
		return Long.toString(UUID.randomUUID().getLeastSignificantBits());
	}

	static String getRandomAvatar() {
		return avatars.get(rnd.nextInt(avatars.size()));
	}

	static String getRandomGroupChatImage() {
		return groupChatImages.get(rnd.nextInt(groupChatImages.size()));
	}

	static String getRandomGroupChatTitle() {
		return groupChatTitles.get(rnd.nextInt(groupChatTitles.size()));
	}

	static String getRandomName() {
		return names.get(rnd.nextInt(names.size()));
	}

	static String getRandomMessage() {
		return messages.get(rnd.nextInt(messages.size()));
	}

	static String getRandomImage() {
		return images.get(rnd.nextInt(images.size()));
	}

	static String getRandomStatus() {
		return status.get(rnd.nextInt(status.size()));
	}

	public static String getRandomHomeContent() {
		return homePostContentData.get(rnd.nextInt(homePostContentData.size()));
	}

	static String getRandomColor() {
		return colors.get(rnd.nextInt(colors.size()));
	}
}
