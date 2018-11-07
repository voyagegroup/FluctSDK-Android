package jp.fluct.sample.samplefluctsdkapp.nativead;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.fluct.fluctsdk.FluctAdError;
import jp.fluct.fluctsdk.FluctAdRequestTargeting;
import jp.fluct.fluctsdk.FluctNativeAd;
import jp.fluct.fluctsdk.FluctNativeAdContent;
import jp.fluct.fluctsdk.FluctViewBinder;
import jp.fluct.sample.samplefluctsdkapp.R;

public class NativeAdRecyclerActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private NativeAdItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad_recycler);

        // ユーザ属性の設定を行うことで広告のターゲティングに利用することが出来ます
        FluctAdRequestTargeting targeting = new FluctAdRequestTargeting();
        targeting.setUserId("f8c5cbe3-9da9-4ec5-a1a2-948c79eb8391");
        targeting.setAge(18);
        targeting.setGender(FluctAdRequestTargeting.FluctGender.MALE);
        targeting.setBirthday(new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime());

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NativeAdItemAdapter(this, targeting);
        mAdapter.setItems(createSampleData());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.destroy();
    }

    private List<String> createSampleData() {
        List<String> items = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            items.add("Content Item #" + i);
        }

        return items;
    }

    private static class NativeAdItemAdapter extends RecyclerView.Adapter {

        private static final String GROUP_ID = "1000098841";
        private static final String UNIT_ID = "1000149834";

        private static final int AD_POSITION_INTERVAL = 30;
        /**
         * 広告を表示するpositionとそれに紐付けられた`FluctNativeAd`インスタンスを保持する
         */
        private final Map<Integer, FluctNativeAd> mNativeAds = new HashMap<>();
        private final Context mContext;
        private final FluctAdRequestTargeting mAdRequestTargeting;
        private List<String> mItems = new ArrayList<>();

        private NativeAdItemAdapter(Context context, @Nullable FluctAdRequestTargeting targeting) {
            mContext = context;
            mAdRequestTargeting = targeting;
        }

        private void setItems(List<String> items) {
            mItems = items;
            setupAds();
            notifyDataSetChanged();
        }

        private void destroy() {
            for (FluctNativeAd nativeAd : mNativeAds.values()) {
                // `destroy`メソッドを呼ぶことで、任意のタイミングでリソースの解放を行うことが出来ます
                nativeAd.destroy();
            }
        }

        @Override
        public int getItemViewType(int position) {
            FluctNativeAd nativeAd;
            if (mNativeAds.containsKey(position) && (nativeAd = mNativeAds.get(position)) != null) {
                return nativeAd.isLoaded() ? ViewType.AD.id : ViewType.NO_AD.id;
            } else {
                return ViewType.ITEM.id;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewType type = ViewType.fromId(viewType);

            if (type == null) {
                throw new IllegalStateException();
            }

            switch (type) {
                case ITEM:
                    TextView textView = new TextView(parent.getContext());
                    textView.setTextSize(30);
                    textView.setLayoutParams(
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    (int) TypedValue.applyDimension(
                                            TypedValue.COMPLEX_UNIT_DIP,
                                            60,
                                            parent.getResources().getDisplayMetrics()
                                    )
                            )
                    );
                    return new ItemViewHolder(textView);
                case NO_AD:
                    View emptyView = new View(parent.getContext());
                    emptyView.setLayoutParams(
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                    );
                    return new NoAdViewHolder(emptyView);
                case AD:
                    LinearLayout adView = new LinearLayout(parent.getContext());
                    adView.setLayoutParams(
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                    );
                    return new NativeAdViewHolder(adView);
                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public int getItemCount() {
            int itemCount = mItems.size();
            // 表示する広告分countを増加させる
            for (FluctNativeAd nativeAd : mNativeAds.values()) {
                if (nativeAd.isLoaded()) {
                    itemCount++;
                }
            }
            return itemCount;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ViewType type = ViewType.fromId(getItemViewType(position));

            if (type == null) {
                throw new IllegalStateException();
            }

            switch (type) {
                case ITEM:
                    ((ItemViewHolder) holder).setText(getItem(position));
                    break;
                case NO_AD:
                    break; // no-op
                case AD:
                    FluctNativeAd nativeAd = mNativeAds.get(position);

                    if (nativeAd == null || !nativeAd.isLoaded()) {
                        throw new IllegalStateException();
                    }

                    // 広告表示時は毎回renderAdViewをコールしてください
                    NativeAdViewHolder adHolder = (NativeAdViewHolder) holder;
                    View adView = nativeAd.renderAdView(adHolder.mLayout);

                    if (adView == null) {
                        throw new IllegalStateException();
                    }

                    FluctNativeAdContent content = nativeAd.getAdContent();
                    if (content != null) {
                        if (!content.hasElement(FluctNativeAdContent.Element.CTA_LABEL)) {
                            adView.findViewById(R.id.callToAction).setVisibility(View.GONE);
                        }
                        if (!content.hasElement(FluctNativeAdContent.Element.DESCRIPTION)) {
                            adView.findViewById(R.id.description).setVisibility(View.GONE);
                        }
                    }

                    adHolder.setAdView(adView);

                    break;
            }
        }

        @Nullable
        private String getItem(int position) {
            if (getItemViewType(position) != ViewType.ITEM.id) {
                throw new IllegalStateException();
            }

            // 広告の分positionをずらす
            int itemPosition = position;
            for (Map.Entry<Integer, FluctNativeAd> entry : mNativeAds.entrySet()) {
                if (position > entry.getKey() && entry.getValue().isLoaded()) {
                    itemPosition--;
                }
            }

            return mItems.get(itemPosition);
        }

        private void setupAds() {
            // 広告用のLayoutのidを設定する
            FluctViewBinder viewBinder = new FluctViewBinder.Builder(R.layout.native_ad_layout)
                    .setMainMediaLayoutId(R.id.mainMedia)
                    .setTitleId(R.id.title)
                    .setDescriptionId(R.id.description)
                    .setIconId(R.id.icon)
                    .setCallToActionLabelId(R.id.callToAction)
                    .setAdchoiceId(R.id.adchoice)
                    .build();

            int adCount = mItems.size() / AD_POSITION_INTERVAL;
            for (int i = 1; i <= adCount; i++) {
                int position = i * (AD_POSITION_INTERVAL + 1) - 1; // 30個間隔に配置（30, 61, 92...）
                if (mNativeAds.get(position) != null || mItems.size() <= position) {
                    continue;
                }
                FluctNativeAd nativeAd = new FluctNativeAd(mContext, GROUP_ID, UNIT_ID, viewBinder);
                nativeAd.setListener(new FluctNativeAdListener(position));
                mNativeAds.put(position, nativeAd);
                nativeAd.loadAd(mAdRequestTargeting);
            }
        }

        enum ViewType {
            ITEM(0),
            AD(1),
            NO_AD(2),;

            public final int id;

            ViewType(int id) {
                this.id = id;
            }

            public static ViewType fromId(int id) {
                for (ViewType type : ViewType.values()) {
                    if (type.id == id) {
                        return type;
                    }
                }
                return null;
            }

        }

        private class FluctNativeAdListener implements FluctNativeAd.Listener {

            private final int position;

            private FluctNativeAdListener(int position) {
                this.position = position;
            }

            @Override
            public void onLoaded(@NonNull FluctNativeAdContent content) {
                // 広告の読み込みが完了した時に呼ばれる
                Log.i("NativeAdSample", "onLoaded");
                notifyItemChanged(position);
            }

            @Override
            public void onClicked(@NonNull FluctNativeAd fluctNativeAd) {
                // 広告がクリックされた時に呼ばれる
                Log.i("NativeAdSample", "onClicked");
            }

            @Override
            public void onLoggingImpression(@NonNull FluctNativeAd fluctNativeAd) {
                // 広告のImpressionが発生した時に呼ばれる
                Log.i("NativeAdSample", "onLoggingImpression");
            }

            @Override
            public void onFailedToLoad(@NonNull FluctAdError adError) {
                // 広告の読み込みが失敗した時に呼ばれる
                Log.i("NativeAdSample", "onFailedToLoad"
                        + "\n  ErrorCode: " + adError.getErrorCode()
                        + "\n  Message: " + adError.getErrorMessage());
            }

            @Override
            public void onFailedToRender(@NonNull FluctAdError adError) {
                // 広告の表示に失敗した時に呼ばれる
                Log.i("NativeAdSample", "onFailedToRender"
                        + "\n  ErrorCode: " + adError.getErrorCode()
                        + "\n  Message: " + adError.getErrorMessage());
                notifyItemChanged(position);
            }
        }
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;

        private ItemViewHolder(TextView itemView) {
            super(itemView);
            mTextView = itemView;
        }

        private void setText(String text) {
            mTextView.setText(text);
        }
    }

    private static class NoAdViewHolder extends RecyclerView.ViewHolder {

        private NoAdViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class NativeAdViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout mLayout;
        private final ViewGroup.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        private NativeAdViewHolder(LinearLayout itemView) {
            super(itemView);
            mLayout = itemView;
        }

        private void setAdView(View adView) {
            mLayout.removeAllViews();
            mLayout.addView(adView, mLayoutParams);
        }
    }
}
