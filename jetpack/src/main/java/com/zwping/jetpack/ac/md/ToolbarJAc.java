package com.zwping.jetpack.ac.md;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gyf.immersionbar.ImmersionBar;
import com.zwping.jetpack.R;
import com.zwping.jetpack.databinding.AcToolBarBinding;
import com.zwping.jetpack.ktxs.ToolbarKtx;

/**
 * zwping @ 2020/12/2
 */
@Route(path = "/jetpack/toolbarj")
public class ToolbarJAc extends AppCompatActivity {

    private AcToolBarBinding viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).init();

        viewBinding = AcToolBarBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        setSupportActionBar(viewBinding.toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ToolbarKtx.setStatusBarImmersion(viewBinding.toolBar);
        viewBinding.toolBar.setOnMenuItemClickListener(item -> {
            showToast(item.toString() + item.getItemId());
            return false;
        });
        viewBinding.toolBar.setNavigationOnClickListener(v -> finish());

        viewBinding.lyContainer.post(() -> {
            ToolbarAc.initExample(viewBinding.lyContainer);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ToolbarKtx.addMenu(menu, 0x01, R.drawable.ic_baseline_android_24, "临时加", MenuItem.SHOW_AS_ACTION_IF_ROOM);
        ToolbarKtx.addMenu(menu, 0x02, R.drawable.ic_baseline_search_24, "搜索", MenuItem.SHOW_AS_ACTION_IF_ROOM);
        ToolbarKtx.addMenu(menu, 0x03, R.drawable.ic_baseline_settings_24, "设置", MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
