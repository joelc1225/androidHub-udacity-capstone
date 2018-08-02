package com.joelcamargojr.androidhub;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.joelcamargojr.androidhub.data.PodcastAPIEndpoints;
import com.joelcamargojr.androidhub.data.PodcastRepository;
import com.joelcamargojr.androidhub.data.RetrofitApi;
import com.joelcamargojr.androidhub.databinding.ActivityMainBinding;
import com.joelcamargojr.androidhub.fragment.FragmentPagerAdapter;
import com.joelcamargojr.androidhub.model.Podcast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    private FragmentPagerAdapter mPagerAdapter;
    PodcastRepository podcastRepository = new PodcastRepository();
    public Podcast fragPodcast;
    String fragmentedPodcastId;
    public PodcastAPIEndpoints podcastAPIInterface;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Dagger injection
        ((AndroidHubApplication) getApplication()).getAppComponent().inject(this);

        // create data binding instance for views
        final ActivityMainBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_main);

        // set up custom toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.nav_menu_icon);

        // sets podcast ids needed for api calls
        fragmentedPodcastId = getString(R.string.fragmentedPodcastId);

        // instantiates drawer to implement on optionsItemSelected
        mDrawerLayout = binding.drawerLayout;

        // handles the logic for the clicks within the drawer
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                binding.drawerLayout.closeDrawers();

                // TODO switch statement for item selections and logic
                return true;
            }
        });

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
                // plug up pager adapter and viewpager
                mPagerAdapter =
                        new FragmentPagerAdapter(fragmentManager, getApplicationContext(), fragPodcast);
                binding.viewPager.setAdapter(mPagerAdapter);
                binding.tabLayout.setupWithViewPager(binding.viewPager);
            }

            @Override
            public void onFailure(Call<Podcast> call, Throwable t) {
                Timber.d("ERROR: " + t.getLocalizedMessage());
            }
        });

//        fragPodcast = podcastRepository.getFragmentedPodcast(fragmentedPodcastId);


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
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
