package de.udos.android_demo_async;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

public class ContentFragment extends Fragment implements HttpGetJsonTask.OnTaskListener {

    public static final String ARG_PARAM = "ARG_PARAM";

    private String mParam;
    private ArrayList<Item> mData = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.ItemAnimator mItemAnimator;

    private OnFragmentInteractionListener mListener;

    public static ContentFragment newInstance(String param) {

        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM, param);

        fragment.setArguments(args);

        return fragment;
    }

    public ContentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            mParam = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_content, container, false);

        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mItemAnimator = new DefaultItemAnimator();
        mItemAnimator.setAddDuration(250);

        mAdapter = new CardViewItemAdapter(getActivity(), mData);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(mItemAnimator);
        mRecyclerView.setAdapter(mAdapter);

        HttpGetJsonTask httpGetJsonTask = new HttpGetJsonTask(this);
        httpGetJsonTask.execute(mParam);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);

        try {

            mListener = (OnFragmentInteractionListener) context;

        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }

    @Override
    public void onJsonLoaded(String json) {

        JsonObject rootObject = new JsonParser().parse(json).getAsJsonObject();
        JsonArray items = rootObject.getAsJsonArray("results");

        //Type type = new TypeToken<List<Item>>(){}.getType(); // für direktes Mapping zu Klasseninstanzen
        //List<Item> items = new Gson().fromJson(items, type);
        //mData.addAll(items);

        for (JsonElement element : items) {

            JsonObject item = element.getAsJsonObject();

            String name = item.get("collectionName").getAsString();
            String url = item.get("artworkUrl100").getAsString();

            if (url.contains("100x100bb")) {
                url = url.replace("100x100bb", "400x400bb");
            }

            mData.add(new Item(name, url));
        }

        mAdapter.notifyDataSetChanged();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
