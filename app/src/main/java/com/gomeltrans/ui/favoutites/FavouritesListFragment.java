package com.gomeltrans.ui.favoutites;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gomeltrans.R;

/**
 * Created by Yahor_Fralou on 8/25/2015.
 */
public class FavouritesListFragment extends Fragment {

    private static final String ARG_PAGE_NUMBER = "arg_page_number";
    private int pageNumber;

    public static FavouritesListFragment getInstance(int pageNumArg) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, pageNumArg);

        FavouritesListFragment fragment = new FavouritesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static String getTabTitle(Context ctx, int position) {
        String title;
        switch (position) {
            case 0: {
                title = ctx.getString(R.string.tab_bus);
            } break;
            case 1: {
                title = ctx.getString(R.string.tab_trolley);
            } break;
            case 2: {
                title = ctx.getString(R.string.tab_stop);
            } break;
            default: {
                title = ctx.getString(R.string.tab_bus);
            }
        }

        return title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARG_PAGE_NUMBER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_fav_list, container, false);

        TextView tv = (TextView) v.findViewById(R.id.tvTabNum);
        tv.setText(tv.getText() + String.valueOf(pageNumber));

        return v;
    }
}
