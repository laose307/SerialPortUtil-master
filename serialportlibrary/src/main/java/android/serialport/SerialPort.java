package android.serialport;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 串口通信，打开串口，读写数据
 */
public class SerialPort {

    private  static String TAG="SerialPort";
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    static {
        System.loadLibrary("serial_port");
    }


	/**
	 *
	 * @param device
	 * @param baudrate
	 * @throws SecurityException
	 * @throws IOException
	 */
    	public SerialPort(File device, int baudrate) throws SecurityException, IOException {

		/* Check access permission */
		if (!device.canRead() || !device.canWrite()) {
			try {
				/* Missing read/write permission, trying to chmod the file */
				Process su;
				String root = "su";
                File suf,alasuf;
				final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};
				for (int i = 0; i < kSuSearchPaths.length; i++) {
					suf = new File(kSuSearchPaths[i] + "su");
					alasuf = new File(kSuSearchPaths[i] + "alasu");
					if (suf != null && suf.exists()) {
						root = "su";
						Log.i(TAG,suf+" find root -- "+root);
					}else if(alasuf != null && alasuf.exists()){
						root = "alasu";
						Log.i(TAG,alasuf+" find root -- "+root);
					}else Log.e(TAG,kSuSearchPaths[i]+" no find root --");
				}
				su = Runtime.getRuntime().exec(root);
				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
					throw new SecurityException();
				}
			} catch (Exception e) {
				e.printStackTrace();
				 throw new SecurityException();
			}
		}

		mFd = open(device.getAbsolutePath(), baudrate);
		if (mFd == null) {
			Log.e(TAG, "native open returns null");
			throw new IOException();
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
	}





    /**
     * 关闭串口
     */
    public void closePort() {
        if (this.mFd != null) {
            try {
                this.close();
                this.mFd = null;
                this.mFileInputStream = null;
                this.mFileOutputStream = null;
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }
    }

    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    /**
     * JNI，设备地址和波特率
     */
    private native static FileDescriptor open(String path, int baudrate);

    private native void close();


}
