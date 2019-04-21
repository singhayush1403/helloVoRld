package com.example.hellovorld;

import android.widget.TextView;

public class Changes {
    private TextView changes;

    public Changes(TextView changes) {
        this.changes = changes;
    }

    public TextView getChanges() {
        return changes;
    }

    public void setChanges(TextView changes) {
        this.changes = changes;
    }
}
