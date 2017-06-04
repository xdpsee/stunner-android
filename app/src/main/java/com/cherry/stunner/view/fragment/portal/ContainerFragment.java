package com.cherry.stunner.view.fragment.portal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cherry.stunner.R;

import java.util.List;

public class ContainerFragment extends Fragment {

    private Class<? extends Fragment> fragmentClass;

    private String userData;

    public static ContainerFragment newInstance(String fragmentClass, String userData) {
        Bundle args = new Bundle();
        args.putString("fragmentClass", fragmentClass);
        args.putString("userData", userData);
        ContainerFragment fragment = new ContainerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userData = getArguments().getString("userData");
        String className = getArguments().getString("fragmentClass");
        try {
            fragmentClass = (Class<? extends Fragment>)Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.root_frame, null);

        try {
            Fragment fragment = fragmentClass.newInstance();

            Bundle arguments = new Bundle();
            arguments.putLong("ARG_CATEGORY_ID", Long.parseLong(userData));
            fragment.setArguments(arguments);

            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.root_frame, fragment);
            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

}
