package com.gomeltrans.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gomeltrans.R;

/**
 * Created by Yahor_Fralou on 8/24/2015.
 */
public class FavouriteFragment extends Fragment {
    private MainActivity parent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_favourite, container, false);

        parent = ((MainActivity) getActivity());
        parent.getSupportActionBar().setTitle(getActivity().getString(R.string.item_favourite));

        return v;
    }
}
