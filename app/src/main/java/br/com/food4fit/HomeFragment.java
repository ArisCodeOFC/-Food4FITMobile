package br.com.food4fit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.food4fit.food4fit.R;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        MainActivity activity = ((MainActivity) getActivity());
        if (activity != null) {
            Toolbar toolbar = view.findViewById(R.id.toolbar);
            activity.configureToolbar(toolbar);
        }

        return view;
    }
}