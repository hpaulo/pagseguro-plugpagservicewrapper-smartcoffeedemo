package br.com.uol.pagseguro.smartcoffee.permissions;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import br.com.uol.pagseguro.smartcoffee.HomeFragment;
import br.com.uol.pagseguro.smartcoffee.R;
import br.com.uol.pagseguro.smartcoffee.utils.UIFeedback;

public class PermissionsFragment extends Fragment implements HomeFragment {

    private static final int PERMISSIONS_REQUEST_CODE = 0x1234;

    public static PermissionsFragment getInstance() {
        return new PermissionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_permissions, container, false);

        return rootView;
    }

//    @OnClick(R.id.btn_permissions)
    public void onRequestPermissionsClicked() {
        requestPermissions();
    }

    private String[] filterMissingPermissions(String[] permissions) {
        String[] missingPermissions = null;
        List<String> list = null;

        list = new ArrayList<>();

        if (permissions != null && permissions.length > 0) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this.getContext().getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                    list.add(permission);
                }
            }
        }

        missingPermissions = list.toArray(new String[0]);

        return missingPermissions;
    }

    private void requestPermissions() {
        String[] missingPermissions = null;

        missingPermissions = this.filterMissingPermissions(this.getManifestPermissions());

        if (missingPermissions != null && missingPermissions.length > 0) {
            requestPermissions(missingPermissions, PERMISSIONS_REQUEST_CODE);
        } else {
            showMessage();
        }
    }

    private void showMessage() {
        UIFeedback.showDialog(getContext(), R.string.msg_all_permissions_granted);
    }

    private String[] getManifestPermissions() {
        String[] permissions = null;
        PackageInfo info = null;

        try {
            info = getContext().getPackageManager()
                    .getPackageInfo(this.getContext().getApplicationContext().getPackageName(), PackageManager.GET_PERMISSIONS);
            permissions = info.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SmartCoffee", "Package name not found", e);
        }

        if (permissions == null) {
            permissions = new String[0];
        }

        return permissions;
    }
}
