-- name: insert-video!
INSERT INTO videos (
    videoId,
    publishedAt,
    channelId,
    video,
    createdAt,
    updatedAt)
VALUES (
    :videoId,
    :publishedAt,
    :channelId,
    :video,
    datetime('now', 'localtime'),
    datetime('now', 'localtime'))

-- name: select-videos
SELECT
    video
FROM videos
WHERE channelId = :channelId
ORDER BY publishedAt DESC
