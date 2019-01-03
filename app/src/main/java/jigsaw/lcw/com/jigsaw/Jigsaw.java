package jigsaw.lcw.com.jigsaw;

import android.graphics.Bitmap;

/**
 * 拼图实体类
 * Create by: chenWei.li
 * Date: 2018/12/30
 * Time: 下午10:10
 * Email: lichenwei.me@foxmail.com
 */
public class Jigsaw {

    private int originalX;
    private int originalY;
    private Bitmap bitmap;
    private int currentX;
    private int currentY;

    public Jigsaw(int originalX, int originalY, Bitmap bitmap) {
        this.originalX = originalX;
        this.originalY = originalY;
        this.bitmap = bitmap;
        this.currentX = originalX;
        this.currentY = originalY;
    }

    public int getOriginalX() {
        return originalX;
    }

    public void setOriginalX(int originalX) {
        this.originalX = originalX;
    }

    public int getOriginalY() {
        return originalY;
    }

    public void setOriginalY(int originalY) {
        this.originalY = originalY;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getCurrentX() {
        return currentX;
    }

    public void setCurrentX(int currentX) {
        this.currentX = currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public void setCurrentY(int currentY) {
        this.currentY = currentY;
    }

    @Override
    public String toString() {
        return "Jigsaw{" +
                "originalX=" + originalX +
                ", originalY=" + originalY +
                ", currentX=" + currentX +
                ", currentY=" + currentY +
                '}';
    }
}
