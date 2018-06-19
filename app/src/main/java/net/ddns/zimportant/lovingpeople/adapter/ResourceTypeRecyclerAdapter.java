package net.ddns.zimportant.lovingpeople.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.ddns.zimportant.lovingpeople.R;
import net.ddns.zimportant.lovingpeople.service.common.model.ResourceType;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class ResourceTypeRecyclerAdapter extends
		RealmRecyclerViewAdapter<ResourceType, ResourceTypeRecyclerAdapter.ViewHolder>{

	public ResourceTypeRecyclerAdapter(@Nullable OrderedRealmCollection<ResourceType> data) {
		super(data, true);
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_resource, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.setItem(getItem(position));
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.tv_title)
		TextView title;
		@BindView(R.id.tv_content)
		TextView content;

		ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		void setItem(ResourceType resourceType) {
			title.setText(resourceType.getTitle());
			content.setText(resourceType.getContent());
		}
	}
}
