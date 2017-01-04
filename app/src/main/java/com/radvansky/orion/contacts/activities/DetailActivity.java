package com.radvansky.orion.contacts.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.radvansky.orion.contacts.R;
import com.radvansky.orion.contacts.model.User;

/**
 * Created by tomasradvansky on 04/01/2017.
 */

public class DetailActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        selectedUser = (User) i.getSerializableExtra("userObject");

        if (selectedUser == null) {
            //serialization error
            this.finish();
        }

        this.setTitle(selectedUser.name == null ? getResources().getString(R.string.unknown) : selectedUser.name);

        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView userNameTextView = (TextView) findViewById(R.id.userNameTextView);
        TextView phoneTextView = (TextView) findViewById(R.id.phoneTextView);
        TextView websiteTextView = (TextView) findViewById(R.id.websiteTextView);
        TextView addressTextView = (TextView) findViewById(R.id.addressTextView);
        TextView companyTextView = (TextView) findViewById(R.id.companyTextView);

        userNameTextView.setText(selectedUser.username == null ? getResources().getString(R.string.unknown) : selectedUser.username);
        phoneTextView.setText(selectedUser.phone == null ? getResources().getString(R.string.unknown) : selectedUser.phone);
        websiteTextView.setText(selectedUser.website == null ? getResources().getString(R.string.unknown) : selectedUser.website);
        if (selectedUser.address != null) {
            addressTextView.setText(selectedUser.address.getFriendlyAddress());
        } else {
            addressTextView.setText(getResources().getString(R.string.unknown));
        }
        if (selectedUser.company != null) {
            companyTextView.setText(selectedUser.company.getFriendlyCompany());
        } else {
            companyTextView.setText(getResources().getString(R.string.unknown));
        }

        LinearLayout websiteCell = (LinearLayout) findViewById(R.id.websiteCell);
        websiteCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String HTTPS = "https://";
                final String HTTP = "http://";
                String url = selectedUser.website;
                if (url != null) {
                    if (!url.startsWith(HTTP) && !url.startsWith(HTTPS)) {
                        url = HTTP + url;
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            }
        });

        LinearLayout phoneCell = (LinearLayout) findViewById(R.id.phoneCell);
        phoneCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = selectedUser.phone;
                if (phone != null) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    if (ActivityCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(callIntent);
                }
            }
        });

        LinearLayout addressCell = (LinearLayout) findViewById(R.id.addressCell);
        addressCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedUser.address != null) {
                    String uri = "geo:0,0,?q=" + selectedUser.address.getFriendlyAddress();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
