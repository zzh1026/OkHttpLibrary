package com.ok.zzh.okhttplibrary.callback.callbackextend;

import com.ok.zzh.okhttplibrary.OkHttpHelper;
import com.ok.zzh.okhttplibrary.callback.CallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;

/**
 * ---------------------------
 * <p>
 * Created by zhaozh on 2017/8/15.
 */

public abstract class FileCallBack extends CallBack<File> {

    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;

    public FileCallBack(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    @Override
    public File parseNetworkResponse(Response response, int requestCode) throws Exception {
        return saveFile(response, requestCode);
    }

    private File saveFile(Response response, final int requestCode) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[1024 * 8];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();

            long sum = 0;

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);

            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                OkHttpHelper.getInstance().getDelivery().post(new Runnable() {
                    @Override
                    public void run() {
                        inProgress(finalSum * 1.0f / total, total, requestCode);
                    }
                });
            }
            fos.flush();

            return file;
        } finally {
            try {
                response.body().close();
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void onResponse(File response, int reqeustCode) {
    }

    @Override
    public void onError(Call call, Exception e, int reqeustCode) {
    }

    @Override
    public void inProgress(float progress, long total, int reqeustCode) {
        super.inProgress(progress, total, reqeustCode);
    }
}
