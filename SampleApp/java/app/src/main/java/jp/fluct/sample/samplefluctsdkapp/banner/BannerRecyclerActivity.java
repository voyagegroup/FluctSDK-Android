package jp.fluct.sample.samplefluctsdkapp.banner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.fluct.fluctsdk.FluctAdView;
import jp.fluct.fluctsdk.FluctErrorCode;
import jp.fluct.sample.samplefluctsdkapp.R;

public class BannerRecyclerActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private FluctAdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_recycler_activity);

        recycler = findViewById(R.id.recycler);
        recycler.setAdapter(new MyAdapter());
        recycler.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        adView =
            new FluctAdView(
                this,
                getString(R.string.banner_test_group_id),
                getString(R.string.banner_test_unit_id),
                getString(R.string.banner_test_ad_size),
                null,
                new FluctAdView.Listener() {
                    @Override
                    public void onLoaded() {
                        toast("onLoaded");
                    }

                    @Override
                    public void onFailedToLoad(@NonNull FluctErrorCode fluctErrorCode) {
                        toast("onFailedToLoad");
                    }

                    @Override
                    public void onLeftApplication() {
                        toast("onLeftApplication");
                    }

                    @Override
                    public void onUnloaded() {
                        toast("onUnloaded");
                    }
                }
            );

        adView.loadAd();
    }

    private void toast(@NonNull String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemViewType(int position) {
            return position % 50 == 0 ? 1 : 0;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0:
                    return new MyTextItemVH(parent);
                case 1:
                    return new MyAdItemVH(parent);
                default:
                    throw new IllegalArgumentException("Not implemented!");
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder org, int position) {
            switch (getItemViewType(position)) {
                case 0:
                    {
                        MyTextItemVH vh = (MyTextItemVH) org;
                        vh.text.setText("Position: " + vh.getAdapterPosition());
                        break;
                    }
                case 1:
                    {
                        MyAdItemVH vh = (MyAdItemVH) org;

                        if (vh.container.getChildCount() > 0) {
                            vh.container.removeAllViews();
                        }

                        vh.container.addView(adView);
                        break;
                    }
                default:
                    throw new IllegalArgumentException("Not implemented!");
            }
        }

        @Override
        public int getItemCount() {
            return 1000;
        }
    }

    private class MyTextItemVH extends RecyclerView.ViewHolder {

        public final TextView text;

        public MyTextItemVH(@NonNull ViewGroup parent) {
            this(
                LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.banner_recycler_text_item_layout, parent, false)
            );
        }

        public MyTextItemVH(@NonNull View itemView) {
            super(itemView);
            this.text = itemView.findViewById(R.id.text);
        }
    }

    private class MyAdItemVH extends RecyclerView.ViewHolder {

        public final FrameLayout container;

        public MyAdItemVH(@NonNull ViewGroup parent) {
            this(
                LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.banner_recycler_ad_item_layout, parent, false)
            );
        }

        public MyAdItemVH(@NonNull View itemView) {
            super(itemView);
            this.container = itemView.findViewById(R.id.container);
        }
    }
}
