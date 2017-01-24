package jp.fluct.sample.samplefluctsdkapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.fluct.fluctsdk.FluctAdBanner;

public class ContentsListFragment extends Fragment {

    private ListView mContentListView;

    public ContentsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_contents_list, container, false);
        mContentListView = (ListView) layout.findViewById(R.id.list);

        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Object> listContents = generateContents();
        ContentsListAdapter adapter = new ContentsListAdapter(getContext(), listContents);

        // Fluct広告を入れる
        FluctAdBanner.SizeType sizeType = FluctAdBanner.SizeType.BANNER;
        String mediaId = "0000005617";
        adapter.insert(new Ad(sizeType, mediaId), 3);
        adapter.insert(new Ad(sizeType, mediaId), 12);
        adapter.insert(new Ad(sizeType, mediaId), 30);
        adapter.insert(new Ad(sizeType, mediaId), 55);
        adapter.insert(new Ad(sizeType, mediaId), 80);

        // リストへ反映
        mContentListView.setAdapter(adapter);
    }

    // リストコンテンツの作成
    private List<Object> generateContents(){
        List<Object> contents = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Content content = new Content(i);
            content.name = "item " + (i + 1);
            contents.add(content);
        }
        return contents;
    }

    /**
     * コンテンツのモデル
     */
    private static class Content {
        final int identity;
        String name;

        Content(int id) {
            this.identity = id;
        }
    }

    /**
     * 広告モデル
     */
    private static class Ad {
        final FluctAdBanner.SizeType sizeType;
        final String mediaId;

        Ad(FluctAdBanner.SizeType sizeType, String mediaId) {
            this.sizeType = sizeType;
            this.mediaId = mediaId;
        }
    }

    private static class ContentsListAdapter extends ArrayAdapter<Object> {

        private final LayoutInflater mInflater;

        public ContentsListAdapter(Context context, List<Object> contents) {
            super(context, R.layout.item_list_content, contents);
            mInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Object content = getItem(position);

            int itemViewType = getItemViewType(position);
            ViewType viewType = ViewType.find(itemViewType);
            switch (viewType) {
                case CONTENT:
                    convertView = getViewForContent((Content) content, position, convertView, parent);
                    break;
                case AD:
                    convertView = getViewForFluctAd((Ad) content, position, convertView, parent);
                    break;
            }

            return convertView;
        }

        private View getViewForContent(Content content, int position, View convertView, ViewGroup parent) {
            ContentViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_list_content, null);
                holder = new ContentViewHolder();
                holder.index = (TextView) convertView.findViewById(R.id.list_index);
                holder.name = (TextView) convertView.findViewById(R.id.list_name);
                holder.thumbnail = (ImageView) convertView.findViewById(R.id.list_thumbnail);
                convertView.setTag(holder);
            } else {
                holder = (ContentViewHolder) convertView.getTag();
            }

            // リスト番号
            holder.index.setText(String.valueOf(position + 1));

            // 項目名
            holder.name.setText(content.name);

            return convertView;
        }

        private View getViewForFluctAd(Ad ad, int position, View convertView, ViewGroup parent) {
            FluctAdBanner adView;
            if (convertView == null) {
                // 初回作成
                adView = new FluctAdBanner(getContext());
                adView.getViewSettings().setSizeType(ad.sizeType);
                adView.getViewSettings().setMediaId(ad.mediaId);
                adView.setTag(ad);

            } else {
                // 広告の再利用
                adView = (FluctAdBanner) convertView;
                Ad preListAd = (Ad) adView.getTag();

                if (preListAd != ad) {
                    // リサイクルされた広告と違う広告を表示させたい場合は再生成する
                    adView = new FluctAdBanner(getContext());
                    adView.getViewSettings().setSizeType(ad.sizeType);
                    adView.getViewSettings().setMediaId(ad.mediaId);
                    adView.setTag(ad);
                }
            }

            return adView;
        }


        @Override
        public int getViewTypeCount() {
            return ViewType.values().length;
        }

        @Override
        public int getItemViewType(int position) {
            Object content = getItem(position);
            if (content instanceof Ad) {
                // 広告
                return ViewType.AD.id;

            } else {
                // デフォルト
                return ViewType.CONTENT.id;

            }
        }

        private enum ViewType {
            CONTENT(0), AD(1);

            final int id;

            ViewType(int id) {
                this.id = id;
            }

            static ViewType find(int id) {
                for(ViewType vt: values()){
                    if(vt.id == id){
                        return vt;
                    }
                }
                return CONTENT;
            }
        }

        private static class ContentViewHolder {
            TextView index;
            TextView name;
            ImageView thumbnail;
        }
    }
}
