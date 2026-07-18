# ExamMap v2.0 alpha 実装メモ

## 今回まとめて行ったこと

従来の「Kotlinソース断片」から、Android Studioが直接認識できる単一アプリプロジェクトへ再構成した。

- Gradle Kotlin DSL、アプリモジュール、Manifest、テーマリソースを追加
- package階層をAndroid標準構成へ移行
- Compose Material 3、Navigation、ViewModelの依存関係を定義
- minSdk 26 / targetSdk 35 / JDK 17を基準化
- v1.6までの全画面・永続化・結果確認機能を統合
- README、除外設定、バージョン情報を追加
- Androidテーマはフレームワーク標準テーマを参照し、Android Studioの既存`themes.xml`とのリソース名重複を回避

## 保持されるデータ

SharedPreferencesのキーとJSON形式はv1.6互換であり、同一applicationIdから更新した場合は既存の試験・学習記録を継続利用できる。

## 留意点

この実行環境にはAndroid SDKとGradle本体がないため、端末向けAPKの生成までは未実施。Android StudioでGradle Syncを行うことで必要なSDKと依存関係が解決される。
