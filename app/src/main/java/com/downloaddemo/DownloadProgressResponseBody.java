package com.downloaddemo;

import static com.downloaddemo.Constants.outputObservable;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class DownloadProgressResponseBody extends ResponseBody {

    private ResponseBody responseBody;
    private DownloadProgressListener progressListener;
    private BufferedSource bufferedSource;

    public DownloadProgressResponseBody(ResponseBody responseBody,
                                        DownloadProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }


    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
               totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                int progress = (int) ((bytesRead * 100) /  responseBody.contentLength());


              //  totalBytesRead += bytesRead != -1 ? bytesRead : 0;

               // responseBody.source().apply{request(Long.MAX_VALUE)}.buffer.size


                float percent = bytesRead == -1 ? 100f : (((float)totalBytesRead / (float) responseBody.contentLength()) * 100);

                Log.d("Tag","Progress:"+progress);
                Log.d("Tag","Progress %:"+percent);
                Log.d("Tag","Progress bytesCount: "+bytesRead );
                Log.d("Tag","Progress contentLength :"+responseBody.contentLength());
                Log.d("Tag","Progress contentLength 33 :"+responseBody.source().getBuffer().size());

                outputObservable.postValue(bytesRead);
                if (null != progressListener) {
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                }
                return bytesRead;
            }
        };

    }
}