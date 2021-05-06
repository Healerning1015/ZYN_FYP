package student.example.myapplication.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import student.example.myapplication.R;
import student.example.myapplication.admin.set.applimits.PatternLockPage;
import student.example.myapplication.home.LearningModule;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        /*
        final TextView textView = view.findViewById(R.id.text_profile);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
        Button button = view.findViewById(R.id.sign_in);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //if(not login){
                Intent intent = new Intent(getActivity(), LearningModule.class);
                startActivity(intent);
                //}

            }
        });

        return view;
    }



}