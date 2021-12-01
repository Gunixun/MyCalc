package com.example.mycalc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.mycalc.tools.Symbol;

import java.util.HashMap;
import java.util.Map;

public class SelectThemeActivity extends AppCompatActivity {
    public static final String EXTRA_THEME = "EXTRA_THEME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_theme);

        setupUI();
    }

    private void setupUI(){

        Intent launchIntent = getIntent();
        Integer modeTheme = launchIntent.getIntExtra(EXTRA_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        Resources res = getResources();
        RadioGroup container = findViewById(R.id.group_theme);

        for (Map.Entry<Integer, String> theme : new HashMap<Integer, String>() {{
            put(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, res.getString(R.string.def_theme));
            put(AppCompatDelegate.MODE_NIGHT_NO, res.getString(R.string.light_theme));
            put(AppCompatDelegate.MODE_NIGHT_YES, res.getString(R.string.dark_theme));
        }}.entrySet()){
            View view = LayoutInflater.from(this).inflate(R.layout.item_theme, container, false);

            view.setOnClickListener(v->{
                Intent data = new Intent();
                data.putExtra(EXTRA_THEME, theme.getKey());
                setResult(Activity.RESULT_OK, data);
                finish();
            });

            RadioButton button = view.findViewById(R.id.button_theme);
            button.setText(theme.getValue());
            if (theme.getKey().equals(modeTheme))
                button.setChecked(true);
            container.addView(view);
        }
    }
}