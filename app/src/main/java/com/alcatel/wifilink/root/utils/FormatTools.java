package com.alcatel.wifilink.root.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.alcatel.wifilink.R;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FormatTools {
    private static FormatTools tools = new FormatTools();

    public static FormatTools getInstance() {
        if (tools == null) {
            tools = new FormatTools();
            return tools;
        }
        return tools;
    }

    // 将byte[]转换成InputStream  
    public InputStream Byte2InputStream(byte[] b) {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        return bais;
    }

    // 将InputStream转换成byte[]  
    public byte[] InputStream2Bytes(InputStream is) {
        String str = "";
        byte[] readByte = new byte[1024];
        int readCount = -1;
        try {
            while ((readCount = is.read(readByte, 0, 1024)) != -1) {
                str += new String(readByte).trim();
            }
            return str.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 将Bitmap转换成InputStream  
    public InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    // 将Bitmap转换成InputStream  
    public InputStream Bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    // 将InputStream转换成Bitmap  
    public Bitmap InputStream2Bitmap(InputStream is) {
        return BitmapFactory.decodeStream(is);
    }

    // Drawable转换成InputStream  
    public InputStream Drawable2InputStream(Drawable d) {
        Bitmap bitmap = this.drawable2Bitmap(d);
        return this.Bitmap2InputStream(bitmap);
    }

    // InputStream转换成Drawable  
    public Drawable InputStream2Drawable(InputStream is) {
        Bitmap bitmap = this.InputStream2Bitmap(is);
        return this.bitmap2Drawable(bitmap);
    }

    // Drawable转换成byte[]  
    public byte[] Drawable2Bytes(Drawable d) {
        Bitmap bitmap = this.drawable2Bitmap(d);
        return this.Bitmap2Bytes(bitmap);
    }

    // byte[]转换成Drawable  
    public Drawable Bytes2Drawable(byte[] b) {
        Bitmap bitmap = this.Bytes2Bitmap(b);
        return this.bitmap2Drawable(bitmap);
    }

    // Bitmap转换成byte[]  
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    // byte[]转换成Bitmap  
    public Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    /**
     * 获取缩略图(url方式)
     *
     * @param context
     * @param url     图片地址
     * @param scale   缩小比例(数值越大缩小程度越大)
     * @return frame
     */
    public Bitmap file2ThumboBitmap(Context context, String url, int scale) {
        // 设定默认(无法正常显示时的图片)
        Bitmap btp = drawable2Bitmap(context.getResources().getDrawable(R.drawable.ic_launcher));
        try {
            // 1.加载位图
            InputStream is = new FileInputStream(url);
            //2.为位图设置100K的缓存
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTempStorage = new byte[100 * 1024];
            //3.设置位图颜色显示优化方式
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            //4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
            opts.inPurgeable = true;
            //5.设置位图缩放比例
            opts.inSampleSize = scale <= 0 ? 4 : scale;
            //6.设置解码位图的尺寸信息
            opts.inInputShareable = true;
            //7.解码位图
            btp = BitmapFactory.decodeStream(is, null, opts);
        } catch (Exception e) {
            e.printStackTrace();
            Logs.t("ma").ee("FormatTools --> file2ThumboBitmap --> " + e.getMessage());
        }
        return btp;
    }

    /**
     * 获取缩略图(inputstream方式)
     *
     * @param context
     * @param in      图片流
     * @param scale   缩小比例(数值越大缩小程度越大)
     * @return frame
     */
    public Bitmap stream2ThumboBitmap(Context context, InputStream in, int scale) {
        // 设定默认(无法正常显示时的图片)
        Bitmap btp = drawable2Bitmap(context.getResources().getDrawable(R.drawable.ic_launcher));
        try {
            // 1.加载位图
            InputStream is = in;
            //2.为位图设置100K的缓存
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTempStorage = new byte[100 * 1024];
            //3.设置位图颜色显示优化方式
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            //4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
            opts.inPurgeable = true;
            //5.设置位图缩放比例
            opts.inSampleSize = scale <= 0 ? 4 : scale;
            //6.设置解码位图的尺寸信息
            opts.inInputShareable = true;
            //7.解码位图
            btp = BitmapFactory.decodeStream(is, null, opts);
        } catch (Exception e) {
            e.printStackTrace();
            Logs.t("ma").ee("FormatTools --> file2ThumboBitmap --> " + e.getMessage());
        }
        return btp;
    }

    /**
     * 获取缩略图(bitmap方式)
     *
     * @param context
     * @param bitmap  图片源
     * @param scale   缩小比例(数值越大缩小程度越大)
     * @return frame
     */
    public Bitmap bitmap2ThumboBitmap(Context context, Bitmap bitmap, int scale) {
        // 设定默认(无法正常显示时的图片)
        Bitmap btp = drawable2Bitmap(context.getResources().getDrawable(R.drawable.ic_launcher));
        try {
            // 1.加载位图
            InputStream is = FormatTools.getInstance().Bitmap2InputStream(bitmap);
            //2.为位图设置100K的缓存
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTempStorage = new byte[100 * 1024];
            //3.设置位图颜色显示优化方式
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            //4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
            opts.inPurgeable = true;
            //5.设置位图缩放比例
            opts.inSampleSize = scale <= 0 ? 4 : scale;
            //6.设置解码位图的尺寸信息
            opts.inInputShareable = true;
            //7.解码位图
            btp = BitmapFactory.decodeStream(is, null, opts);
        } catch (Exception e) {
            e.printStackTrace();
            Logs.t("ma").ee("FormatTools --> file2ThumboBitmap --> " + e.getMessage());
        }
        return btp;
    }

    /**
     * 获取适配手机屏幕的缩略图
     *
     * @param activity
     * @param url      图片地址
     * @return bitmap
     */
    public Bitmap file2FitPhoneBitmap(Activity activity, String url) {
        // 0.设定默认(无法正常显示时的图片)
        Bitmap btp = drawable2Bitmap(activity.getResources().getDrawable(R.drawable.ic_launcher));
        try {
            // 0.1.获取图片流
            InputStream is = new FileInputStream(url);
            // 0.2.获取屏幕大小
            int ww = activity.getWindowManager().getDefaultDisplay().getWidth();
            int wh = activity.getWindowManager().getDefaultDisplay().getHeight();

            // 1.提取图片特征
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;// 申请提取图片元数据
            opts.inPurgeable = true;// 申请GC清理请求

            // 2. 使用is图片流进行bitmap图元生成(注意: 这里使用了is, 下边要使用的时候要重新new一个, 否则is为空)
            Bitmap tempBm = BitmapFactory.decodeStream(is, null, opts);
            // 3.计算比例
            int pw = opts.outWidth;
            int ph = opts.outHeight;
            // --> 3.1.默认为1
            int scale = 1;
            float rateW = pw * 1f / ww;
            float rateH = ph * 1f / wh;
            if (pw >= ph) {// 宽度>高度
                scale = (int) rateW;
            }
            if (pw < ph) {// 高度>宽度
                scale = (int) rateH;
            }
            // 4.关闭申请图片元数据以及流对象并且把生成的临时图片置空
            opts.inSampleSize = scale;
            opts.inJustDecodeBounds = false;
            is.close();
            tempBm = null;
            // 5.重新创建一个IS流
            InputStream is1 = new FileInputStream(url);
            // 6.重新创建特征对象
            BitmapFactory.Options opts1 = new BitmapFactory.Options();
            // 7.把计算的比例赋值进去以及设置其他特征值
            opts1.inSampleSize = scale;
            opts1.inTempStorage = new byte[100 * 1024];// 设置缓存为100K
            opts1.inPreferredConfig = Bitmap.Config.RGB_565;// 设置颜色为565模式
            opts1.inPurgeable = true;// 设置允许垃圾回收
            // 8.重新打包这个图片
            btp = BitmapFactory.decodeStream(is1, null, opts1);
            // 9.关流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return btp;
    }

    // Drawable转换成Bitmap  
    public Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    // Bitmap转换成Drawable  
    public Drawable bitmap2Drawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        Drawable d = (Drawable) bd;
        return d;
    }

    // File转byte
    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (Exception e) {
            Logs.t("FormatTools").ee("File2byte error: " + e.getMessage());
            e.printStackTrace();
        }
        return buffer;
    }

    // byte转file
    public static File byte2File(byte[] buf, String fileDir, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(fileDir);
            if (!dir.exists() | !dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(fileDir + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

}
