package buttons.games.sounds.mc_ultra_skins;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

class CustomFilter extends Filter {
    SkinsAdapter adapter;
    List<SkinModel> filterList;

    public CustomFilter(List<SkinModel> filterList, SkinsAdapter skinsAdapter) {
        this.adapter=skinsAdapter;
        this.filterList = filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<SkinModel> filteredPlayers=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getTitle().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }

            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.itemsList= (ArrayList<SkinModel>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
