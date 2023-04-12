DIR=`pwd`

cd `dirname -- "$0"`

[ ! -d "../work" ] && mkdir ../work
cat table.sql | sqlite3 ../work/youtube.sqlite

cd "$DIR"
