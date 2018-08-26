package buttons.games.sounds.mc_ultra_skins;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

public class HorizontalButtonsAdapter extends RecyclerView.Adapter<HorizontalButtonsAdapter.ViewHolder> {


    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    // data is passed into the constructor
    HorizontalButtonsAdapter(Context context, List<String> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_rc_category, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int[] androidColors = context.getResources().getIntArray(R.array.colorArr);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];


        String titleText = mData.get(position);
        holder.myTextView.setText("#" + titleText);
        holder.buttonSelf.setBackgroundColor(randomAndroidColor);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        RelativeLayout buttonSelf;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.textView_button_title);
            buttonSelf = itemView.findViewById(R.id.relitiveLayout_button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
