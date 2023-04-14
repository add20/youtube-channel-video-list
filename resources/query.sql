-- name: insert-video!
INSERT INTO videos (videoId, publishedAt, channelId, video)
VALUES (:videoId, :publishedAt, :channelId, :video)

-- name: select-videos
SELECT
    video
FROM videos
WHERE channelId = :channelId
ORDER BY publishedAt DESC
