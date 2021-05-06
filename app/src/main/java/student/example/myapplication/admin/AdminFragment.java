package student.example.myapplication.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import student.example.myapplication.R;
import student.example.myapplication.admin.set.applimits.AdminModePassword;

public class AdminFragment extends Fragment {

    private AdminViewModel adminViewModel;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    AdminModePassword adminModePassword;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        adminViewModel =
                ViewModelProviders.of(this).get(AdminViewModel.class);
        View view = inflater.inflate(R.layout.fragment_admin, container, false);

        /*
        final TextView textView = root.findViewById(R.id.text_admin);
        adminViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
         */
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        adminModePassword = new AdminModePassword(getActivity());
        adminModePassword.setPassword("12345");

        final EditText editText = view.findViewById(R.id.admin_pwd);
        //final TextView textView = view.findViewById(R.id.textView);
        final Button button = view.findViewById(R.id.enter);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(editText.getText().toString().equals(adminModePassword.getPassword())){
                    //Fragment newFragment = new Fragment();
                    //FragmentTransaction transaction =getFragmentManager().beginTransaction();
                    //transaction.replace(R.id.navigation_admin_set, AdminSetFragment);
                    //transaction.commit();

                    Intent intent = new Intent(getActivity(), AdminSet.class);
                    startActivity(intent);
                }
                else{
                    if (getActivity() != null){
                        Toast.makeText(getActivity().getApplicationContext(), "Wrong PatternPassword", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return view;
    }
}