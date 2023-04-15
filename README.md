# youtube

このプロジェクトは、YouTube Data APIを用いて特定のチャンネルの動画一覧を取得・表示します。

## Installation

このプロジェクトではSQLiteを使用します。

```
$ brew install leiningen
$ brew install sqlite
$ git clone https://github.com/add20/youtube-channel-video-list.git
$ cd youtube-channel-video-list
$ bash resources/setup.sh
```

設定ファイル`.lein-env`を用意します。

- `:api-key`にYouTubeのAPI Keyを書きます。
- `:channel-id`に検索したいYouTubeチャンネルのチャンネルIDを書きます。
- `:latest-date`には現在日時を表す"now"か取得する動画期間の終了日時を書きます。フォーマットは、"yyyy/MM/dd HH:mm:ss"です。
- `:register-date`にはチャンネル登録日を書きます。もしくは、取得する動画期間の開始日を書きます。フォーマットは、"yyyy/MM/dd"です。
- `:interval-month`に動画情報を一度に取得する期間（月）を指定します。使用するYouTube Data APIは一度に500件の動画情報までしか返しません。そのため、あたり500件以下になるように`:interval-month`を調節する必要があります。最小値は、"1"です。
- `:work-dir`と`:log-dir`と`:html-dir`は存在しなければ自動でディレクトリを作成します。

```
$ cat .lein-env
{:database-url "jdbc:sqlite:work/youtube.sqlite"
;;  in resources/path
 :query-file "query.sql"
 :api-key "<<YouTube API KEY>>"
 :channel-id "<<YouTube Channel Id>>"
 :time-zone-for-offset "9"
;;  "now" or "yyyy/MM/dd HH:mm:ss"
 :latest-date "<<nowまたは、期間終了日時（例：2023/04/13 23:59:59）>>"
;;  "yyyy/MM/dd"
 :register-date "<<チャンネル登録日（例：2022/06/01）>>"
 :interval-month "6"
 :work-dir "work"
 :log-dir "work/log"
 :json-file "work/youtube.json"
 :sqlite-file "work/youtube.sqlite"
 :template-file "resources/index.html.selmer"
 :html-dir "work/html"}
```

## Usage

```
$ lein run -- --help
usage:
  -f, --fetch          fetch YouTube data to JSON
  -l, --load           load YouTube JSON data to SQLite database
  -g, --generate-html  generate HTML file
  -h, --help
```

## Options

`--fetch`オプションでYouTubeから動画一覧をダウンロードしてJSONファイルに保存します。
`--load`オプションで保存したJSONファイルのデータをsqliteに保存します。
`--generate-html`オプションでsqliteに保存された動画情報をHTMLに書き出します。

## Examples

```
$ lein run -- --fetch --load --generate-html > work/log/log.txt 2> work/log/err.txt
```

## License

Copyright © 2023 add20

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
