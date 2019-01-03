package jigsaw.lcw.com.jigsaw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * 拼图数据帮助类（处理和view无关的业务）
 * Create by: chenWei.li
 * Date: 2019/1/2
 * Time: 下午11:06
 * Email: lichenwei.me@foxmail.com
 */
public class JigsawHelper {

    private static volatile JigsawHelper mInstance;

    private JigsawHelper() {
    }

    public static JigsawHelper getInstance() {
        if (mInstance == null) {
            synchronized (JigsawHelper.class) {
                if (mInstance == null) {
                    mInstance = new JigsawHelper();
                }
            }
        }
        return mInstance;
    }


    /**
     * 获取拼图（大图）
     *
     * @return
     */
    public Bitmap getJigsaw(Context context) {
        //加载Bitmap原图,并获取宽高
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.abc);
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        //按屏幕宽铺满显示，算出缩放比例
        int screenWidth = getScreenWidth(context);
        float scale = 1.0f;
        if (screenWidth < bitmapWidth) {
            scale = screenWidth * 1.0f / bitmapWidth;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, (int) (bitmapHeight * scale), false);
        return bitmap;
    }


    /**
     * 判断当前view是否在可移动范围内（在空白View的上下左右）
     *
     * @param imageView
     * @param emptyImageView
     * @return
     */
    public boolean isNearByEmptyView(ImageView imageView, ImageView emptyImageView) {

        Jigsaw emptyJigsaw = (Jigsaw) imageView.getTag();
        Jigsaw jigsaw = (Jigsaw) emptyImageView.getTag();

        if (emptyJigsaw != null && jigsaw != null) {
            //点击拼图在空拼图的左边
            if (jigsaw.getOriginalX() == emptyJigsaw.getOriginalX() && jigsaw.getOriginalY() + 1 == emptyJigsaw.getOriginalY()) {
                return true;
            }
            //点击拼图在空拼图的右边
            if (jigsaw.getOriginalX() == emptyJigsaw.getOriginalX() && jigsaw.getOriginalY() - 1 == emptyJigsaw.getOriginalY()) {
                return true;
            }
            //点击拼图在空拼图的上边
            if (jigsaw.getOriginalY() == emptyJigsaw.getOriginalY() && jigsaw.getOriginalX() + 1 == emptyJigsaw.getOriginalX()) {
                return true;
            }
            //点击拼图在空拼图的下边
            if (jigsaw.getOriginalY() == emptyJigsaw.getOriginalY() && jigsaw.getOriginalX() - 1 == emptyJigsaw.getOriginalX()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断游戏是否结束
     *
     * @param imageViewArray
     * @return
     */
    public boolean isFinishGame(ImageView[][] imageViewArray, ImageView emptyImageView) {

        int rightNum = 0;//记录匹配拼图数

        for (int i = 0; i < imageViewArray.length; i++) {
            for (int j = 0; j < imageViewArray[0].length; j++) {
                if (imageViewArray[i][j] != emptyImageView) {
                    Jigsaw jigsaw = (Jigsaw) imageViewArray[i][j].getTag();
                    if (jigsaw != null) {
                        if (jigsaw.getOriginalX() == jigsaw.getCurrentX() && jigsaw.getOriginalY() == jigsaw.getCurrentY()) {
                            rightNum++;
                        }
                    }
                }
            }
        }

        if (rightNum == (imageViewArray.length * imageViewArray[0].length) - 1) {
            return true;
        }
        return false;
    }


    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    private int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

}
