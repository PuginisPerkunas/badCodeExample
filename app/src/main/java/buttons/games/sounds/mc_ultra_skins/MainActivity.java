package buttons.games.sounds.mc_ultra_skins;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HorizontalButtonsAdapter.ItemClickListener{

    HorizontalButtonsAdapter mCategoryButtons;
    SkinsAdapter forMainItemsTestAdapter;
    RecyclerView mainItemsRc;
    List<SkinModel> allSkinList;
    List<SkinModel> populatingList;
    ArrayList<String> categorys;
    List<SkinModel> mSkinSelectedCat;

    ProgressDialog loadingDialog;
    LinearLayoutManager layoutManager;
    LinearLayoutManager layoutManagerSec;
    Gson gson;
    String json;

    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawer;
    SearchView sv;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setLoadingDialog();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Constants.inesticialAd);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        toolbar.setBackgroundColor(getResources().getColor(R.color.primaryDarkColor));

        populatingList = new ArrayList<>();
        mainItemsRc = findViewById(R.id.skinsRc);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sv= (SearchView) findViewById(R.id.mSearch);
        EditText searchEditText = (EditText) sv.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.primaryTextColor));

        loadingDialog.show();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                List<SkinModel> skins = new ArrayList<>();
                gson = new Gson();
                Log.d("json", loadJSONFromAsset());
                json = loadJSONFromAsset();
                allSkinList = gson.fromJson(json, new TypeToken<List<SkinModel>>(){}.getType());
                categorys = new ArrayList<>();
                int counter = 0;
                for (SkinModel skin : allSkinList) {
                    Log.d("SkinName",skin.title);
                    Log.d("SkinNo",String.valueOf(counter));

                    if (categorys.contains(skin.category.substring(0, 1).toUpperCase() + skin.category.substring(1))) {
                        System.out.println("Account found");
                    } else {
                        categorys.add(skin.category.substring(0, 1).toUpperCase() + skin.category.substring(1));
                    }

                    counter++;
                }
                Log.d("fullJson", json);
                try {
                    JSONObject obj = new JSONObject(json);
                    Log.d("obj",obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                initThings();

                loadingDialog.dismiss();
            }
        });

    }



    private void initThings(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                layoutManager
                        = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);

                layoutManagerSec
                        = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);


                //TODO your background code
                RecyclerView myList = (RecyclerView) findViewById(R.id.horizontalRc);
                myList.setLayoutManager(layoutManager);
                mCategoryButtons = new HorizontalButtonsAdapter(MainActivity.this, categorys);
                mCategoryButtons.setClickListener(MainActivity.this);
                myList.setItemAnimator(new DefaultItemAnimator());
                myList.setAdapter(mCategoryButtons);

                mainItemsRc.setLayoutManager(layoutManagerSec);
                mSkinSelectedCat = new ArrayList<>();
//                mSkinSelectedCat.add("test");
               // forMainItemsTestAdapter = new SkinsAdapter(MainActivity.this,mSkinSelectedCat);
               // forMainItemsTestAdapter.notifyDataSetChanged();
              //  mainItemsRc.setItemAnimator(new DefaultItemAnimator());
               // mainItemsRc.setAdapter(forMainItemsTestAdapter);

                loadingDialog.show();
                mSkinSelectedCat.clear();
                if(!populatingList.isEmpty()){
                    populatingList.clear();

                }
                for(int i = 0; i < allSkinList.size(); i++){
                    if(allSkinList.get(i).category.equals("latest")){
                        populatingList.add(allSkinList.get(i));
                        mSkinSelectedCat.add(allSkinList.get(i));
                    }
                }
                Collections.shuffle(mSkinSelectedCat);
                forMainItemsTestAdapter = new SkinsAdapter(MainActivity.this,mSkinSelectedCat,mSkinSelectedCat);
                forMainItemsTestAdapter.notifyDataSetChanged();
                mainItemsRc.setAdapter(forMainItemsTestAdapter);

                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String query) {
                        //FILTER AS YOU TYPE
                        forMainItemsTestAdapter.getFilter().filter(query);
                        return false;
                    }
                });

                loadingDialog.dismiss();
            }
        });
    }


    private void setLoadingDialog(){
        loadingDialog = new ProgressDialog(MainActivity.this,  R.style.MyAlertDialogStyle);
        //loadingDialog.setProgressStyle( R.style.MyAlertDialogStyle);
        loadingDialog.setMessage("Loading...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onItemClick(View view, int position) {
        loadingDialog.show();
        sv.setQuery("",false);
        mSkinSelectedCat.clear();
        if(!populatingList.isEmpty()){
            populatingList.clear();

        }
        for(int i = 0; i < allSkinList.size(); i++){
            if(mCategoryButtons.getItem(position).toLowerCase().equals(allSkinList.get(i).category)){
                populatingList.add(allSkinList.get(i));
                mSkinSelectedCat.add(allSkinList.get(i));
            }
        }
        Collections.shuffle(mSkinSelectedCat);
        forMainItemsTestAdapter = new SkinsAdapter(this,mSkinSelectedCat,mSkinSelectedCat);
        forMainItemsTestAdapter.notifyDataSetChanged();
        mainItemsRc.setAdapter(forMainItemsTestAdapter);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                forMainItemsTestAdapter.getFilter().filter(query);
                return false;
            }
        });
        loadingDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_messenger) {
            //Facebook messanger
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent
                    .putExtra(Intent.EXTRA_TEXT,
                            "http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName());
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.facebook.orca");
            try {
                startActivity(sendIntent);
            }
            catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this,"Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
            }
            return true;
        } else if (id == R.id.nav_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String appName = getString(R.string.app_name);
            String linkBody = "http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName();
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,appName);
            shareIntent.putExtra(Intent.EXTRA_TEXT,linkBody);
            startActivity(Intent.createChooser(shareIntent,"Share using..."));
            return true;
        } else if (id == R.id.nav_api) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
            Intent mailIntent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:?subject=" + "API Request"+ "&body=" + "Hello, I want to get API!" + "&to=" + "simplegames01@gmail.com");
            mailIntent.setData(data);
            startActivity(Intent.createChooser(mailIntent, "Send mail..."));
        } else if (id == R.id.nav_upgrade) {
            Intent mailIntent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:?subject=" + "Category Request"+ "&body=" + "Hello, I want to that you add this skin category: " + "&to=" + "simplegames01@gmail.com");
            mailIntent.setData(data);
            startActivity(Intent.createChooser(mailIntent, "Send mail..."));
        } else if (id == R.id.nav_code) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
            Intent mailIntent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:?subject=" + "Code Request"+ "&body=" + "Hello, I want to get Source Code!" + "&to=" + "simplegames01@gmail.com");
            mailIntent.setData(data);
            startActivity(Intent.createChooser(mailIntent, "Send mail..."));
        } else if (id == R.id.nav_rate) {
            Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
            }
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = MainActivity.this.getAssets().open("skinData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }



}
