# 旧バージョンからの移行ガイド

v4.0.0 より、ライブラリの形式を JAR から AAR に変更しました。
それにより、使用中の SDK を削除して新しい SDK を使用する設定が必要となります。

## Android Studio にて使用中

### 旧バージョンの削除

- JAR ファイルを libs 等に直接配置をして使用していた場合
  1. libs 等に現在使用している FluctSDK の jar ファイルを削除します。

- サブモジュールとして JAR ファイルを追加して使用していた場合
  1. File > Project Structure を選択します。
  2. 左ペインの Modules にある FluctSDK のサブモジュールを選択します。
  3. 上にある 「-」 を押下でサブモジュールを削除します。

### build.gradle の修正
  1. FluctSDK を直接参照している記述がある場合、その記述を削除します。
  2. dependencies の Google play services 依存記述を base のみに変更します。
  
  ```gradle
  compile 'com.google.android.gms:play-services:+'
  ```
  を
  ```gradle
  compile 'com.google.android.gms:play-services-base:+'
  ```
  に変更します。
  
  - 上記のバージョン記述であれば、常に最新版を使用することになりますが、バージョン番号の指定を行うようにしてもらうと、問題発生時に原因の特定がしやすくなります。 

### 新バージョン 設定

- [fluct 広告配信 SDK for Android 簡易導入ガイド](HOW-TO-SETUP.md) を参考に新バージョンを使用できる設定を行います。

### クラスパス 修正

- java ソース、 レイアウト XML、 AndroidManifest.xml 指定してあるクラスパスを変更します。

  1. FluctView
    ```java
    jp.co.voyagegroup.android.fluct.jar.FluctView
    ```
    を
    ```java
    jp.fluct.fluctsdk.FluctView
    ```
    に変更します。 

  2. FluctInterstitial
    ```java
    jp.co.voyagegroup.android.fluct.jar.FluctInterstitial
    ```
    を
    ```
    jp.fluct.fluctsdk.FluctInterstitial
    ```
    に変更します。

  3. FluctInterstitialActivity
    ```xml
    jp.co.voyagegroup.android.fluct.jar.FluctInterstitialActivity
    ```
    を
    ```
    jp.fluct.fluctsdk.FluctInterstitialActivity
    ```
    に変更します。

## Eclipse にて使用中でそのまま使用したい

- 基本的には Android Studio への移行をお勧めしますが、どうしても Eclipse でビルドを行いたい場合、JAR ファイルを個別にて提供しますので、弊社担当営業までご連絡ください。

### jar ファイルを差し替え

- 現在使用している SDK の JAR ファイル を 新しい SDK の JAR ファイル に差し替えます。

### ライブラリ参照変更

- プロジェクトプロパティ > Java Build Path > Libraries で、旧 SDK を削除し、新 SDK を追加します。

### クラスパス変更

- java ソース と レイアウト XML と AndroidManifest.xml にある FluctView と FluctInterstitial のクラスパスを変更します。

  1. FluctView
    ```java
    jp.co.voyagegroup.android.fluct.jar.FluctView
    ```
    を
    ```java
    jp.fluct.fluctsdk.FluctView
    ```
    に変更します。
  
  2. FluctInterstitial
    ```java
    jp.co.voyagegroup.android.fluct.jar.FluctInterstitial
    ```
    を
    ```
    jp.fluct.fluctsdk.FluctInterstitial
    ```
    に変更します。
  
  3. FluctInterstitialActivity
    ```xml
    jp.co.voyagegroup.android.fluct.jar.FluctInterstitialActivity
    ```
    を
    ```
    jp.fluct.fluctsdk.FluctInterstitialActivity
    ```
    に変更します。
