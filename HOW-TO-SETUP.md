# fluct 広告配信 SDK for Android 簡易導入ガイド
以下が簡易導入手順となります。

## app/build.gradle への追加
### FluctSDK の リモート maven リポジトリ を設定します
```gradle
repositories {
    // FluctSDK maven repository
    maven {
        url 'https://raw.github.com/voyagegroup/FluctSDK-Android/master/m2/repository/'
    }
}
```

### dependencies へ以下の依存情報を設定します
```gradle
dependencies {
    ...
    // FluctSDK
    compile 'jp.fluct:FluctSDK:+'
    // Google play services - base
    compile 'com.google.android.gms:play-services-base:+'
    ...
}
```
- この記述だと最新のバージョンを使用するようになっていますが、バージョン番号指定をしてもらうと、問題発生時にサポートしやすくなります。

## app/src/main/AndroidManifest.xml へ追加
### FluctSDK を使用する上で必要な uses-permission を追加します
```xml
<manifest
    ...
    <!-- FluctSDK using permission -->
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

### Google play services version 定義を追加します
```xml
<application
    ...
    <!-- Required Google play services definition for using FluctSDK -->
    <meta-data android:name="com.google.android.gms.version"
               android:value="@integer/google_play_services_version"/>
```

### インタースティシャル広告 を使用する場合、以下の Activity 設定を追加します
```xml
<!-- Required Activity definition for using FluctInterstitial -->
<activity
        android:name="jp.fluct.fluctsdk.FluctInterstitialActivity"
        android:configChanges="orientation|keyboardHidden|screenSize"
        android:theme="@android:style/Theme.Translucent.NoTitleBar"
        android:launchMode="singleTask">
</activity>
```

- 以上の記述追加/設定により、 FluctSDK を使用する準備ができます。

## メディア ID の設定
- 広告を表示する際には、弊社システムで 貴社アプリ広告枠 を識別するため発行される **メディア ID** が必要となります。
- 貴社アプリ広告枠毎の **メディア ID** は弊社担当営業にお問い合わせください。
- アプリの開発時およびテスト時には、必ず **テスト用 メディア ID** を使用してください。
- **0000000108** が バナー広告 および インタースティシャル広告 の **テスト用 メディア ID** となっています。
- 公開前に実際の広告を表示する必要がある場合も、表示された広告をタップしないでください。

### AndroidManifest.xml で定義 (バナー広告/インタースティシャル広告)
- アプリ内全般で使用されます。
- こちらは **オプション** としての実装方法となります。
```xml
<application
    ...
    <meta-data android:name="FLUCT_MEDIA_ID" android:value="{メディアID}"/>
```

## バナー広告
- 特定サイズでの広告表示となります。
- レイアウト幅としては match_parent 、高さは wrap_content を指定する事を推奨しますが、固定サイズを指定する場合は、 320dp x 50dp は最低限確保するようにしてください。
- 詳細は [導入仕様書](fluct広告配信SDK導入仕様書.pdf) を参照してください。

### 実装例 (レイアウトXML)

- 配置するレイアウトに FluctView を追加します。
- ここで メディアID を指定する実装が、推奨実装方法となります。

```xml
<jp.fluct.fluctsdk.FluctView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
		FLUCT_MEDIA_ID="{メディアID}"/>
```

### 実装例 (コード)

- 配置する Activity / Fragment で FluctView を追加するコードを記述します。

```java
FluctView banner = new FluctView(this, "{メディアID}");

...

addView(banner);
```

## インタースティシャル広告
- 全画面を覆う広告表示となります。
- 詳細は [導入仕様書](fluct広告配信SDK導入仕様書.pdf) を参照してください。

### 実装例 (コード)

- 表示する Activity / Fragment で FluctInterstitial を使用します。
- ここで メディアID を指定する実装が、推奨実装方法となります。

* 初期化
```java
FluctInterstitial mInterstitial;

...

mInterstitial = new FluctInterstitial(this, "{メディアID}");
```

- 表示
```java
mInterstitial.showInterstitialAd();
```

- 終了化
```java
mInterstitial.destroy();
mInterstitial = null;
```

---
## LICENSE
Copyright fluct, Inc. All rights reserved.
