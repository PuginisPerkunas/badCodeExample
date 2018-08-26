package buttons.games.sounds.mc_ultra_skins;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class patvirtinimoAct extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1000;
    public static patvirtinimoAct patvirtinimoActivity;
    TextView skinName;
    ImageView skinImage;
    Button download, back;

    String folderName, savedImageLocation;
    RelativeLayout relativeLayoutMain;
    ProgressDialog loadingDialog;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    //private AdView mAdView;
    //private InterstitialAd mInterstitialAd;
    public static List<SkinModel> getItemsList = new ArrayList<SkinModel>();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patvirtinimo);
        setLoadingDialog();
        folderName = getString(R.string.app_name);
        relativeLayoutMain = findViewById(R.id.relativeLayoutMain);
        folderName = folderName.replaceAll(" ", "");

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
                getItemsList.clear();
                finish();
            }

        });

        patvirtinimoActivity = this;
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },PERMISSION_REQUEST_CODE);

        init();
//        Typeface fontText = Typeface.createFromAsset(getAssets(), "MineAlt.ttf");
//        Typeface fontButton = Typeface.createFromAsset(getAssets(), "MineReg.ttf");
//        skinName.setTypeface(fontButton);
//        download.setTypeface(fontText);
//        back.setTypeface(fontButton);
        getDataFromIntent();
        skinName.setText(getItemsList.get(0).getTitle());
        Picasso.with(this).load(getItemsList.get(0).getPhotoUrl()).into(skinImage);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ActivityCompat.checkSelfPermission(patvirtinimoAct.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(patvirtinimoAct.this,"You should grant permission",Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },PERMISSION_REQUEST_CODE);
                    return;
                } else {
                    loadingDialog.show();
                    Log.d("linkas",getItemsList.get(0).getDownloadUrl());
                    Glide.with(patvirtinimoAct.this)
                            .asBitmap()
                            .load(getItemsList.get(0).getDownloadUrl())
                            .into(new SimpleTarget<Bitmap>(100,100) {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    saveImage(resource);
                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    super.onLoadFailed(errorDrawable);
                                    loadingDialog.dismiss();
                                    showSnackbar("Check your internet connection! ", true);
                                }
                            });
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rand = new Random();
                int randomNumber = rand.nextInt(10)+1;
                if(randomNumber % 2 == 0){
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                }
                getItemsList.clear();
                finish();
            }
        });
    }

    public void showSnackbar(String message, boolean error){
        Snackbar snackbar;
        snackbar = Snackbar.make(relativeLayoutMain, message, Snackbar.LENGTH_SHORT);
        if(error){
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.color11));
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(getResources().getColor(R.color.primaryTextColor));
        } else {
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(getResources().getColor(R.color.primaryTextColor));
        }
        snackbar.show();
    }

    private void setLoadingDialog(){
        loadingDialog = new ProgressDialog(patvirtinimoAct.this,  R.style.MyAlertDialogStyle);
        //loadingDialog.setProgressStyle( R.style.MyAlertDialogStyle);
        loadingDialog.setMessage("Saving...");
        loadingDialog.setIndeterminate(true);
        loadingDialog.setCanceledOnTouchOutside(false);
    }

    private String saveImage(Bitmap image) {
        String savedImagePath = null;
        String skinName = Integer.toString(getItemsList.get(0).getId()) + getItemsList.get(0).getTitle();
        skinName.replace(" ", "_");
        skinName = skinName.replaceAll(" ", "_");
        Log.d("skin name: ", skinName);
        String imageFileName = "PNG_" + skinName + ".png";
        File storageDir = new File(            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/" +folderName);
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            loadingDialog.dismiss();
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
                loadingDialog.dismiss();
                showSnackbar("Check your internet connection! ", true);
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            savedImageLocation = savedImagePath;
            Snackbar snackbar;
            snackbar = Snackbar.make(relativeLayoutMain,"Image SAVED in " + folderName + " folder",Snackbar.LENGTH_LONG)
                    .setAction("OPEN", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openFolder(savedImageLocation);
                        }
                    });
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.secondaryColor));
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(getResources().getColor(R.color.primaryTextColor));
            snackbar.show();

        } else {
            loadingDialog.dismiss();
            showSnackbar("Check your internet connection! ", true);
        }

        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    private void init(){
        skinName = (TextView) findViewById(R.id.skinNameTXT);
        skinImage = (ImageView) findViewById(R.id.skinImage);
        download = (Button) findViewById(R.id.downloadFinal);
        back = (Button) findViewById(R.id.backBtn);
    }

    public void openFolder(String location)
    {

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri mydir = Uri.parse("file://"+location);
        intent.setDataAndType(mydir,"application/*");    // or use */*
        startActivity(intent);
    }

    public void getDataFromIntent(){
        Intent getIntent = getIntent();
        getItemsList = getIntent.getParcelableArrayListExtra("extra");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getItemsList.clear();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            finish();
        }
    }
}
