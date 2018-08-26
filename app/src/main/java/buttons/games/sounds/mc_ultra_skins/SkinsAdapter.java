package buttons.games.sounds.mc_ultra_skins;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.appolica.flubber.Flubber;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SkinsAdapter extends RecyclerView.Adapter<SkinsAdapter.ViewHolder> implements Filterable {

    private LayoutInflater mInflater;


    public List<SkinModel> itemsList, filterList;
    public static List<SkinModel> sendItemsList = new ArrayList<SkinModel>();
    private Context context;
    CustomFilter filter;
    private URL url;
    public String shortBy;
    View layoutView;

    // data is passed into the constructor
    SkinsAdapter(Context context, List<SkinModel> itemsList, List<SkinModel> filterList) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.itemsList = itemsList;
        this.filterList = itemsList;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String urlPhoto = itemsList.get(position).getPhotoUrl();
        String urlSkin = itemsList.get(position).getDownloadUrl();
        String skinName = itemsList.get(position).getTitle();



        holder.skinText.setText(skinName);
        Picasso.with(context).load(urlPhoto).into(holder.skinPhoto);
        holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendItemsList.add(itemsList.get(position));
                Intent toFinal = new Intent(context,patvirtinimoAct.class);
                toFinal.putParcelableArrayListExtra("extra",(ArrayList<SkinModel>)sendItemsList);
                context.startActivity(toFinal);
                sendItemsList.clear();
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(filterList,this);
        }

        return filter;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView skinText;
        public ImageView skinPhoto;
        public Button downloadBtn;
        public Context context;
        public long duration = 350;

        ViewHolder(View itemView) {
            super(itemView);
            skinText = itemView.findViewById(R.id.skinText);
            itemView.setOnClickListener(this);

            skinText = (TextView) itemView.findViewById(R.id.skinText);
            skinPhoto = (ImageView) itemView.findViewById(R.id.img);
            downloadBtn = (Button) itemView.findViewById(R.id.downloadBTN);
            skinText.setVisibility(View.VISIBLE);
            skinPhoto.setVisibility(View.VISIBLE);
            downloadBtn.setVisibility(View.VISIBLE);
            context = itemView.getContext();
//            Flubber.with()
//                    .animation(Flubber.AnimationPreset.POP)
//                    .repeatCount(1)                              // Repeat once
//                    .duration(1000)                              // Last for 1000 milliseconds(1 second)
//                    .createFor(itemView)                             // Apply it to the view
//                    .start();

        }

        @Override
        public void onClick(View view) {
            //if (mClickListener != null) mClickListener.onItemClickSec(view, getAdapterPosition());
        }
    }

}
