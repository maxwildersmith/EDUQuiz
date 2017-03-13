package com.example.quiz.eduquiz;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by csaper6 on 3/13/17.
 */
public class WikiFragment extends ListFragment {
    private List<Wiki> people;
    private WikiAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        //create our list of heroes
        people = new ArrayList<>();
        //populateList();
        Comparator c = null;
        Collections.sort(people, c);


        //fill the custom adapter
        adapter = new WikiAdapter(getActivity(), people);

        //set the listView's adapter
        setListAdapter(adapter);

        return rootView;
    }

    public void populateList(ArrayList<Wiki> wikis) {
        people.clear();
        Collections.sort(wikis);
        people.addAll(wikis);
        adapter.notifyDataSetChanged();

    }
}

