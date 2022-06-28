package com.downloaddemo;

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
