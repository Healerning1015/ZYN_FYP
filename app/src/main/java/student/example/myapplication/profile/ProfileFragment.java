package student.example.myapplication.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import student.example.myapplication.R;
import student.example.myapplication.admin.set.applimits.Utils;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private LinearLayout drawOverOtherApps;
    private LinearLayout allowUsageAccess;
    private ImageView drawOver;
    private ImageView usageAccess;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initView(view);
        initListener();



        return view;
    }

    private void initListener() {
        drawOverOtherApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                startActivity(intent);
            }
        });

        allowUsageAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                startActivity(intent);
            }
        });
    }

    private void initView(View view) {
        drawOverOtherApps = view.findViewById(R.id.draw_over_other_apps);
        allowUsageAccess = view.findViewById(R.id.allow_usage_access);
        drawOver = view.findViewById(R.id.img_draw_over);
        usageAccess = view.findViewById(R.id.img_usage_access);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Settings.canDrawOverlays(getActivity())) {
            //若已授权
            drawOver.setImageDrawable(getActivity().getDrawable(R.drawable.ic_twotone_true_24));
            //drawOverOtherApps.setEnabled(false);
        } else {
            drawOver.setImageDrawable(getActivity().getDrawable(R.drawable.ic_twotone_false_24));
        }

        if(Utils.checkPermission(getActivity())){
            usageAccess.setImageDrawable(getActivity().getDrawable(R.drawable.ic_twotone_true_24));
            //allowUsageAccess.setEnabled(false);
        } else {
            usageAccess.setImageDrawable(getActivity().getDrawable(R.drawable.ic_twotone_false_24));
        }
    }
}