package com.rexa.agent;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 36, 28, 28);
        TextView title = new TextView(this); title.setText("Rexa Agent"); title.setTextSize(24);
        root.addView(title);
        TextView info = new TextView(this); info.setText("اربط التطبيق بخادم Rexa ثم فعّل خدمة إمكانية الوصول يدوياً.");
        root.addView(info);
        EditText server = new EditText(this); server.setHint("عنوان الخادم مثل http://192.168.10.89:8765");
        root.addView(server);
        EditText token = new EditText(this); token.setHint("رمز الاتصال");
        root.addView(token);
        Button save = new Button(this); save.setText("حفظ الإعدادات");
        root.addView(save);
        save.setOnClickListener(v -> {
            getSharedPreferences("rexa", MODE_PRIVATE).edit()
                .putString("server", server.getText().toString().trim())
                .putString("token", token.getText().toString().trim()).apply();
            Toast.makeText(this, "تم حفظ الإعدادات على هذا الجهاز", Toast.LENGTH_SHORT).show();
        });
        Button accessibility = new Button(this); accessibility.setText("فتح إعدادات إمكانية الوصول");
        root.addView(accessibility);
        accessibility.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));
        setContentView(root);
        server.setText(getSharedPreferences("rexa", MODE_PRIVATE).getString("server", ""));
    }
}
