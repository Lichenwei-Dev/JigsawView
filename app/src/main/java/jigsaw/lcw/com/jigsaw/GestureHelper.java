package jigsaw.lcw.com.jigsaw;

import android.view.MotionEvent;

/**
 * 拼图手势帮助类
 * Create by: chenWei.li
 * Date: 2019/1/2
 * Time: 下午11:06
 * Email: lichenwei.me@foxmail.com
 */
public class GestureHelper {

    //标志手势方向
    public static final int NONE = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;
    private static final int LEFT_OR_RIGHT = 5;
    private static final int UP_OR_DOWN = 6;


    private static volatile GestureHelper mInstance;

    private GestureHelper() {
    }

    public static GestureHelper getInstance() {
        if (mInstance == null) {
            synchronized (GestureHelper.class) {
                if (mInstance == null) {
                    mInstance = new GestureHelper();
                }
            }
        }
        return mInstance;
    }


        /**
         * 判断手指移动的方向，
         *
         * @param startEvent
         * @param endEvent
         * @return
         */
        public int getGestureDirection(MotionEvent startEvent, MotionEvent endEvent) {
            float startX = startEvent.getX();
            float startY = startEvent.getY();
            float endX = endEvent.getX();
            float endY = endEvent.getY();
            //根据滑动距离判断是横向滑动还是纵向滑动
            int gestureDirection = Math.abs(startX - endX) > Math.abs(startY - endY) ? LEFT_OR_RIGHT : UP_OR_DOWN;
            //具体判断滑动方向
            switch (gestureDirection) {
                case LEFT_OR_RIGHT:
                    if (startEvent.getX() < endEvent.getX()) {
                        //手指向右移动
                        return RIGHT;
                    } else {
                        //手指向左移动
                        return LEFT;
                    }
                case UP_OR_DOWN:
                    if (startEvent.getY() < endEvent.getY()) {
                        //手指向下移动
                        return DOWN;
                    } else {
                        //手指向上移动
                        return UP;
                    }
            }
            return NONE;
        }

}
