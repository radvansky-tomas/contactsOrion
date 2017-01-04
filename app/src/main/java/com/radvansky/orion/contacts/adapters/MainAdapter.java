package com.radvansky.orion.contacts.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.radvansky.orion.contacts.R;
import com.radvansky.orion.contacts.activities.DetailActivity;
import com.radvansky.orion.contacts.model.User;

import java.util.List;

/**
 * Created by tomasradvansky on 04/01/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<User> mDataset;
    private Context ctx;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        LinearLayout mainView;
        TextView firstNameTextView;
        TextView lastNameTextView;
        TextView emailTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            mainView = (LinearLayout) itemView.findViewById(R.id.mainView);
            firstNameTextView = (TextView) itemView.findViewById(R.id.firstName);
            lastNameTextView = (TextView) itemView.findViewById(R.id.lastName);
            emailTextView = (TextView) itemView.findViewById(R.id.email);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainAdapter(List<User> myDataset, Context context) {
        mDataset = myDataset;
        ctx = context;
    }

    public void clear() {
        mDataset.clear();
        notifyDataSetChanged();
    }

    public void setData(List<User> data) {
        mDataset = data;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_basic, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final User user = mDataset.get(position);
        if (user.name != null) {
            String[] parts = mDataset.get(position).name.split(" ");
            if (parts.length >= 2) {
                holder.firstNameTextView.setText(parts[0]);
                holder.lastNameTextView.setText(parts[1]);
            } else if (parts.length == 1) {
                holder.firstNameTextView.setText(parts[0]);
            } else {
                holder.emailTextView.setText("");
            }
        } else {
            holder.emailTextView.setText("");
        }
        if (user.email != null) {
            holder.emailTextView.setText(user.email);
        } else {
            holder.emailTextView.setText("");
        }
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, DetailActivity.class);
                i.putExtra("userObject", user);
                ctx.startActivity(i);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
