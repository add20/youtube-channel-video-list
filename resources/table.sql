CREATE TABLE IF NOT EXISTS videos (
    videoId TEXT NOT NULL UNIQUE,
    publishedAt TEXT NOT NULL,
    channelId TEXT NOT NULL,
    video JSON NOT NULL
);