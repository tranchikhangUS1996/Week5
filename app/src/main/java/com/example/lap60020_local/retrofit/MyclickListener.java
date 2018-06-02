package com.example.lap60020_local.retrofit;

import android.content.Intent;
import android.view.View;

class MyclickListener implements View.OnClickListener {
    private Integer id;
    MyclickListener(Integer id) {
        this.id = id;
    }

    @Override
    public void onClick(View v) {
        // open detail
        Intent intent = new Intent(v.getContext(),DetailActivity.class);
        intent.putExtra("ID",id);
        v.getContext().startActivity(intent);
    }
}
