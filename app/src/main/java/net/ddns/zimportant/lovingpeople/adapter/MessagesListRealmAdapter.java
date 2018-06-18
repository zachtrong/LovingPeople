package net.ddns.zimportant.lovingpeople.adapter;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import net.ddns.zimportant.lovingpeople.service.common.model.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.RealmList;

public class MessagesListRealmAdapter implements MessagesListAdapter.OnLoadMoreListener {

	private RealmList<Message> messages;
	private MessagesListAdapter<Message> adapter;
	private String senderId;

	public MessagesListRealmAdapter(String senderId, RealmList<Message> messages) {
		this.senderId = senderId;
		this.messages = messages;
		initAdapter();
		createListener();
	}

	private void initAdapter() {
		ImageLoader imageLoader = (imageView, url) -> {
			Picasso.get().load(url).into(imageView);
		};
		adapter = new MessagesListAdapter<>(senderId, imageLoader);
		adapter.setLoadMoreListener(this);
		List<Message> messageList = new ArrayList<>(messages);
		adapter.addToEnd(messageList, true);
	}

	private void createListener() {
		messages.addChangeListener((messages, changeSet) -> {
			if (changeSet.getState() == OrderedCollectionChangeSet.State.INITIAL) {
				return;
			}
			// For deletions, the adapter has to be notified in reverse order.
			OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
			for (int i = deletions.length - 1; i >= 0; i--) {
				OrderedCollectionChangeSet.Range range = deletions[i];
				List<Message> messageList = messages.subList(
						range.startIndex,
						range.startIndex + range.length
				);
				for (Message message : messageList) {
					adapter.delete(message);
				}
			}

			OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
			for (OrderedCollectionChangeSet.Range range : insertions) {
				List<Message> messageList = messages.subList(
						range.startIndex,
						range.startIndex + range.length
				);
				for (Message message : messageList) {
					adapter.addToStart(message, true);
				}
			}

			OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
			for (OrderedCollectionChangeSet.Range range : modifications) {
				List<Message> messageList = messages.subList(
						range.startIndex,
						range.length
				);
				for (Message message : messageList) {
					adapter.update(message);
				}
			}
		});
	}

	public MessagesListAdapter<Message> getListAdapter() {
		return adapter;
	}

	public void setListAdapter(MessagesListAdapter<Message> listAdapter) {
		this.adapter = listAdapter;
	}

	@Override
	public void onLoadMore(int page, int totalItemsCount) {
	}
}
