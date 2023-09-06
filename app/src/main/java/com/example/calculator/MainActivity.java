package com.example.calculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity {
    private EditText numInput;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch mode;
    private int currentLayout;
    private int savedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numInput = findViewById(R.id.input);
        mode = (Switch) findViewById(R.id.switchMode);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(String.valueOf(currentLayout), savedLayout);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        savedLayout = savedInstanceState.getInt(String.valueOf(savedLayout));
        setContentView(savedLayout);
        super.onRestoreInstanceState(savedInstanceState);
    }


    @SuppressLint("SetTextI18n")
    public void onClick(View v){
        Button b = (Button)v;
        numInput.setText(numInput.getText().toString() + b.getText().toString());
    }

    public void clear(View v){
        numInput.setText("");
    }

    public void changeMode(View v){
        if(mode.isChecked()){
            setContentView(R.layout.activity_main1);
            numInput = findViewById(R.id.input);
            mode = (Switch) findViewById(R.id.switchMode);
            currentLayout = R.layout.activity_main1;
        } else {
            setContentView(R.layout.activity_main);
            numInput = findViewById(R.id.input);
            mode = (Switch) findViewById(R.id.switchMode);
            currentLayout = R.layout.activity_main;
        }
    }

    public void delete(View v){
        int length = numInput.getText().length();
        if (length > 0) {
            numInput.getText().delete(length - 1, length);
        }
    }

    public void equals(View v) {
        try {
            Context context = Context.enter(); //
            context.setOptimizationLevel(-1); // this is required[2]
            Scriptable scope = context.initStandardObjects();
            String result_ = numInput.getText().toString();
            Object result = context.evaluateString(scope, result_, "<cmd>", 1, null);
            if(result.toString().equals("Infinity") || result.toString().equals("-Infinity")) {
                Toast infinity_preview = Toast.makeText(this, "Cannot be divided by 0", Toast.LENGTH_SHORT);
                infinity_preview.setGravity(Gravity.CENTER, 0, 0);
                infinity_preview.show();
                numInput.setText("");
            }
            else if(!result.toString().equals("org.mozilla.javascript.Undefined@0")) {
                Toast preview = Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT);
                preview.setGravity(Gravity.CENTER, 0, 0);
                preview.show();
            } else{
                Toast undefined_preview = Toast.makeText(this, "Undefined", Toast.LENGTH_LONG);
                undefined_preview.setGravity(Gravity.CENTER, 0, 0);
                undefined_preview.show();
                numInput.setText("");
            }
        }catch (Exception e){
            Toast error_preview = Toast.makeText(this, "SyntaxError", Toast.LENGTH_SHORT);
            error_preview.setGravity(Gravity.CENTER, 0, 0);
            error_preview.show();
            numInput.setText("");
        }
    }

}





