package student.example.myapplication.admin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import student.example.myapplication.R;

public class AdminFragment extends Fragment {

    private AdminViewModel adminViewModel;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    AdminModePassword adminModePassword;

    private EditText editText;
    private Button button;

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
        if(adminModePassword.getPassword()==null){
            adminModePassword.setPassword("12345");
        }

        editText = view.findViewById(R.id.admin_pwd);
        button = view.findViewById(R.id.enter);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);

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
                        Toast.makeText(getActivity().getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        editText.setText("");
    }
}