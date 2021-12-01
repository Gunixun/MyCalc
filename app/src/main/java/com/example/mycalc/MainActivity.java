package com.example.mycalc;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import com.example.mycalc.tools.CalcImpl;
import com.example.mycalc.tools.StorageTheme;
import com.example.mycalc.tools.Symbol;
import com.example.mycalc.ui.CalculatorPresenter;
import com.example.mycalc.ui.CalculatorView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CalculatorView {
    private final String ARGUSERSTRING = "USERSTRING";
    private final String ARGISWRITE = "ISWRITE";

    private int sizeInactive;
    private int sizeActive;

    private TextView textViewResult;
    private TextView textViewData;
    private boolean write = true;

    private CalculatorPresenter presenter;
    private StorageTheme storage;


    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                int modeTheme = result.getData().getIntExtra(SelectThemeActivity.EXTRA_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                storage.saveModeTheme(modeTheme);
                recreate();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storage = new StorageTheme(this);

        AppCompatDelegate.setDefaultNightMode(storage.getSavedModeTheme());
        setContentView(R.layout.activity_main);

        textViewData = findViewById(R.id.text_view_data);
        textViewResult = findViewById(R.id.text_view_result);

        sizeActive = getResources().getDimensionPixelSize(R.dimen.text_size_active);
        sizeInactive = getResources().getDimensionPixelSize(R.dimen.text_size_inactive);

        presenter = new CalculatorPresenter(this, new CalcImpl());
        connectSignals();
        textViewData.callOnClick();

        if (savedInstanceState != null) {
            textViewData.setText(savedInstanceState.getString(ARGUSERSTRING));
            presenter.setUserStr(textViewData.getText().toString());
            if (!savedInstanceState.getBoolean(ARGISWRITE)) {
                textViewResult.callOnClick();
            }
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARGUSERSTRING, textViewData.getText().toString());
        outState.putBoolean(ARGISWRITE, write);
    }

    private void connectSignals() {

        for (Map.Entry<Integer, Symbol> symbol : new HashMap<Integer, Symbol>() {{
                put(R.id.button_0, Symbol.ZERO);
                put(R.id.button_1, Symbol.ONE);
                put(R.id.button_2, Symbol.TWO);
                put(R.id.button_3, Symbol.THREE);
                put(R.id.button_4, Symbol.FOUR);
                put(R.id.button_5, Symbol.FIVE);
                put(R.id.button_6, Symbol.SIX);
                put(R.id.button_7, Symbol.SEVEN);
                put(R.id.button_8, Symbol.EIGHT);
                put(R.id.button_9, Symbol.NINE);
                put(R.id.button_sub, Symbol.SUB);
                put(R.id.button_mult, Symbol.MULT);
                put(R.id.button_sum, Symbol.SUM);
                put(R.id.button_dot, Symbol.DOT);
                put(R.id.button_equals, Symbol.EQUALS);
                put(R.id.button_percent, Symbol.PERCENT);
                put(R.id.button_exhibitor, Symbol.EXHIBITOR);
                put(R.id.button_div, Symbol.DIV);
                put(R.id.button_clean, Symbol.CLEAN);
                put(R.id.button_backspace, Symbol.BACKSPACE);
            }}.entrySet()){
                findViewById(symbol.getKey()).setOnClickListener(v -> {
                    presenter.onKeyPressed(symbol.getValue());
                });
        }

        textViewResult.setOnClickListener(v -> {
            textViewData.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeInactive);
            textViewResult.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeActive);
            write = false;
        });

        textViewData.setOnClickListener(v -> {
            textViewData.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeActive);
            textViewResult.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizeInactive);
            write = true;
        });

        findViewById(R.id.choose_theme).setOnClickListener(v->{
            Intent intent = new Intent(this, SelectThemeActivity.class);
            intent.putExtra(SelectThemeActivity.EXTRA_THEME, storage.getSavedModeTheme());
            launcher.launch(intent);
        });
    }

    public boolean isWriting(){return write;}

    @Override
    public void setUserData(String value) {
        textViewData.setText(value);
    }

    @Override
    public void setResult(String value) {
        textViewResult.setText(value);
    }

    @Override
    public void disableInput() {
        textViewResult.callOnClick();
    }

    @Override
    public void clean() {
        textViewData.setText("");
        textViewResult.setText("");
        textViewData.callOnClick();
    }
}