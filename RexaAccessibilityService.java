package com.rexa.agent;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.Locale;

public class RexaAccessibilityService extends AccessibilityService {
    private long lastClickAt = 0;
    private static final long CLICK_GUARD_MS = 900;
    private static final String[] TERMS = {"lucky bag", "luckybox", "حقيبة الحظ", "الحقيبة"};

    @Override public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null || System.currentTimeMillis() - lastClickAt < CLICK_GUARD_MS) return;
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null) return;
        try { scan(root); } finally { root.recycle(); }
    }

    private boolean scan(AccessibilityNodeInfo node) {
        CharSequence text = node.getText();
        CharSequence desc = node.getContentDescription();
        String value = ((text == null ? "" : text.toString()) + " " + (desc == null ? "" : desc.toString())).toLowerCase(Locale.ROOT);
        for (String term : TERMS) {
            if (value.contains(term.toLowerCase(Locale.ROOT)) && clickSelfOrParent(node)) {
                lastClickAt = System.currentTimeMillis();
                return true;
            }
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            if (child != null) {
                boolean found = scan(child);
                child.recycle();
                if (found) return true;
            }
        }
        return false;
    }

    private boolean clickSelfOrParent(AccessibilityNodeInfo node) {
        AccessibilityNodeInfo current = node;
        for (int depth = 0; current != null && depth < 5; depth++) {
            if (current.isClickable()) return current.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            current = current.getParent();
        }
        return false;
    }

    @Override public void onInterrupt() { }
}
