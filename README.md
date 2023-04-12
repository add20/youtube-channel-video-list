# youtube

このプロジェクトは、YouTube Data APIを用いて特定のチャンネルの動画一覧を取得・表示します。

## Installation

このプロジェクトではsqliteを使用します。

```
$ brew install sqlite
$ git clone https://github.com/add20/youtube-channel-video-list.git
$ cd youtube-channel-video-list
$ bash resources/setup.sh
```

設定ファイル`.lein-env`を用意します。`:api-key`にYouTubeのAPI Keyを書きます。`:channel-id`に検索したいYouTubeチャンネルのチャンネルIDを書きます。`:register-date`にチャンネル登録日を書きます。`:interval-month`に動画情報を一度に取得する期間（月）で指定します。

```
$ cat .lein-env
{:database-url "jdbc:sqlite:work/youtube.sqlite"
;;  in resources/path
 :query-file "query.sql"
 :api-key "<<YouTube API KEY>>"
 :channel-id "<<YouTube Channel Id>>"
 :register-date "<<チャンネル登録日（例：2022/06/01）"
 :interval-month "6"
 :work-dir "work"
 :log-dir "work/log"
 :json-file "work/youtube.json"
 :template-file "resources/index.html.selmer"
 :html-file "work/index.html"}
```

## Usage

```
$ lein run -- --help
usage:
  -f, --fetch          fetch YouTube data
  -l, --load           load YouTube json data to sqlite database
  -g, --generate-html  generate html file
  -h, --help
```

## Options

`--fetch`オプションでYouTubeから動画一覧をダウンロードしてJSONファイルに保存します。
`--load`オプションで保存したJSONファイルのデータをsqliteに保存します。
`--generate-html`オプションでsqliteに保存された動画情報をHTMLに書き出します。

## Examples

...

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
