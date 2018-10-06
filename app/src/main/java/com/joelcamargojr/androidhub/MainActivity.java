package com.joelcamargojr.androidhub;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.joelcamargojr.androidhub.data.PodcastAPIEndpoints;
import com.joelcamargojr.androidhub.data.RetrofitApi;
import com.joelcamargojr.androidhub.databinding.ActivityMainBinding;
import com.joelcamargojr.androidhub.model.Podcast;
import com.joelcamargojr.androidhub.recyclerview.MainRecyclerviewAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public Podcast fragPodcast;
    String fragmentedPodcastId;
    public PodcastAPIEndpoints podcastAPIInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create data binding instance for views
        final ActivityMainBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_main);

        // set up custom toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        // sets podcast ids needed for api calls
        fragmentedPodcastId = getString(R.string.fragmentedPodcastId);

        podcastAPIInterface =
                RetrofitApi.getPodcastClient().create(PodcastAPIEndpoints.class);

        // gets list of episodes for given fragPodcast ID
        Timber.d("ABOUT TO HIT THE API");
        // Calls API to get the list of recent Podcast episodes for given fragPodcast ID
        Call<Podcast> call =
                podcastAPIInterface.getFragmentedPodcastList(fragmentedPodcastId);

        call.enqueue(new Callback<Podcast>() {
            @Override
            public void onResponse(Call<Podcast> call, Response<Podcast> response) {
                int statusCode = response.code();
                Timber.d("STATUS CODE IS: " + statusCode);
                fragPodcast = response.body();
                RecyclerView recyclerView = binding.recyListenFrag;
                MainRecyclerviewAdapter adapter = new MainRecyclerviewAdapter(fragPodcast, getApplicationContext());
                recyclerView.setAdapter(adapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
            }

            @Override
            public void onFailure(Call<Podcast> call, Throwable t) {
                Timber.d("ERROR: " + t.getLocalizedMessage());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
