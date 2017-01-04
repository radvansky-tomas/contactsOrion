package com.radvansky.orion.contacts.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirGetCallback;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import com.google.gson.reflect.TypeToken;
import com.radvansky.orion.contacts.adapters.MainAdapter;
import com.radvansky.orion.contacts.R;
import com.radvansky.orion.contacts.interfaces.WS;
import com.radvansky.orion.contacts.model.User;
import com.radvansky.orion.contacts.model.UserComparator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences settings;
    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private WS service;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Restore preferences
        settings = getSharedPreferences(PREFS_NAME, 0);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(WS.class);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (mAdapter == null) {
            //Create empty adapted
            mAdapter = new MainAdapter(new ArrayList<User>(), this);
            try {
                //Check for cached data
                if (Reservoir.contains("contacts")) {
                    loadFromCache();
                } else {
                    loadData();
                }
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
                loadData();
            }
        }

        if (mRecyclerView.getAdapter() == null) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem sortASC = menu.findItem(R.id.action_asc);
        MenuItem sortDESC = menu.findItem(R.id.action_desc);
        if (settings.getBoolean("isASC", false)) {
            sortASC.setChecked(true);
            sortDESC.setChecked(false);
        } else {
            sortASC.setChecked(false);
            sortDESC.setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_asc) {
            settings.edit().putBoolean("isASC", true).apply();
            invalidateOptionsMenu();
            loadData();
            return true;
        } else if (id == R.id.action_desc) {
            settings.edit().putBoolean("isASC", false).apply();
            invalidateOptionsMenu();
            loadData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_title)
                .content(R.string.progress_msg)
                .progress(true, 0)
                .show();

        service.GetUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                // specify an adapter (see also next example)
                final List<User> data = response.body();
                //Cache data
                Reservoir.putAsync("contacts", data, new ReservoirPutCallback() {
                    @Override
                    public void onSuccess() {
                        loadFromCache();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e(TAG, e.getLocalizedMessage());
                        //Caching error -> use existing data
                        Collections.sort(data, new UserComparator());
                        if (!settings.getBoolean("isASC", false)) {
                            Collections.reverse(data);
                        }
                        mAdapter.clear();
                        mAdapter.setData(data);
                        dialog.dismiss();
                    }
                });

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                dialog.dismiss();
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Error")
                        .content(t.getMessage())
                        .positiveText(android.R.string.ok)
                        .show();
            }
        });
    }

    private void loadFromCache() {
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_title)
                .content(R.string.progress_msg)
                .progress(true, 0)
                .show();
        Type resultType = new TypeToken<List<User>>() {
        }.getType();
        Reservoir.getAsync("contacts", resultType, new ReservoirGetCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> data) {
                Collections.sort(data, new UserComparator());
                if (!settings.getBoolean("isASC", false)) {
                    Collections.reverse(data);
                }
                mAdapter.clear();
                mAdapter.setData(data);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Exception e) {
                dialog.dismiss();
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Error")
                        .content(e.getMessage())
                        .positiveText(android.R.string.ok)
                        .show();
            }
        });
    }
}
