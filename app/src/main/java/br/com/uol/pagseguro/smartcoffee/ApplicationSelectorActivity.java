package br.com.uol.pagseguro.smartcoffee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.annotation.Nullable;

import javax.inject.Inject;

import br.com.uol.pagseguro.smartcoffee.demo.DemoInternoActivity;
import br.com.uol.pagseguro.smartcoffee.injection.ApplicationSelectorComponent;
import br.com.uol.pagseguro.smartcoffee.utils.FragmentFlowManager;

public class ApplicationSelectorActivity extends Activity {

    ApplicationSelectorComponent mInjector;

    @Inject
    FragmentFlowManager mFragmentFlowManager;

    public static ApplicationSelectorActivity getInstance() {
        return new ApplicationSelectorActivity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_selector);

        Button btnDemo = findViewById(R.id.btn_demo);
        btnDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDemoClicked();
                // FormularioActivity formulario = new FormularioActivity();


            }
        });

        Button btnAllFeatures = findViewById(R.id.btn_all_features);
        btnAllFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAllFeaturesClicked();
            }
        });
    }

    private void startActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }

    // @OnClick(R.id.btn_demo)
    public void onDemoClicked() {
        startActivity(DemoInternoActivity.class);
    }

    // @OnClick(R.id.btn_all_features)
    public void onAllFeaturesClicked() {
        startActivity(MainActivity.class);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
