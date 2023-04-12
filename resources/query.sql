-- name: insert-video!
INSERT INTO videos (videoId, publishedAt, video)
VALUES (:videoId, :publishedAt, :video)

-- name: select-videos
SELECT
    video
FROM videos
ORDER BY publishedAt DESC
