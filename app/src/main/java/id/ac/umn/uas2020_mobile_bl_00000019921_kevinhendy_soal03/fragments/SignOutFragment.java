package id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;

import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.LoginActivity;
import id.ac.umn.uas2020_mobile_bl_00000019921_kevinhendy_soal03.R;

public class SignOutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_out, container, false);

        AuthUI.getInstance().signOut(getActivity());

        Intent logoutIntent = new Intent(getActivity(), LoginActivity.class);
        startActivity(logoutIntent);
        getActivity().finish();
        return root;
    }
}
