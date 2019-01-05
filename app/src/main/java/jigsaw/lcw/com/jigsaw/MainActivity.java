package jigsaw.lcw.com.jigsaw;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private GridLayout mGridLayout;

    //维护拼图碎片的集合
    private int[][] mJigsawArray = new int[3][5];
    private ImageView[][] mImageViewArray = new ImageView[3][5];
    //空白拼图碎片
    private ImageView mEmptyImageView;

    private GestureDetector mGestureDetector;

    //动画是否在执行，避免快速点击导致动画重复执行
    private boolean isAnimated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //判断手指滑动方向
                int gestureDirection = GestureHelper.getInstance().getGestureDirection(e1, e2);
                //处理滑动拼图
                handleFlingGesture(gestureDirection, true);
                return true;
            }
        });
        //初始化拼图碎片
        Bitmap jigsaw = JigsawHelper.getInstance().getJigsaw(this);
        initJigsaw(jigsaw);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                randomJigsaw();
            }
        }, 200);
    }

    /**
     * 游戏初始化，随机打乱顺序
     */
    private void randomJigsaw() {
        for (int i = 0; i < 100; i++) {
            int gestureDirection = (int) ((Math.random() * 4) + 1);
            handleFlingGesture(gestureDirection, false);
        }
    }


    /**
     * 初始化拼图碎片
     *
     * @param jigsawBitmap
     */
    private void initJigsaw(Bitmap jigsawBitmap) {

        mGridLayout = findViewById(R.id.gl_layout);

        int itemWidth = jigsawBitmap.getWidth() / 5;
        int itemHeight = jigsawBitmap.getHeight() / 3;

        //切割原图为拼图碎片装入GridLayout
        for (int i = 0; i < mJigsawArray.length; i++) {
            for (int j = 0; j < mJigsawArray[0].length; j++) {
                Bitmap bitmap = Bitmap.createBitmap(jigsawBitmap, j * itemWidth, i * itemHeight, itemWidth, itemHeight);
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(bitmap);
                imageView.setPadding(2, 2, 2, 2);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否可移动
                        boolean isNearBy = JigsawHelper.getInstance().isNearByEmptyView((ImageView) v, mEmptyImageView);
                        if (isNearBy) {
                            //处理移动
                            handleClickItem((ImageView) v, true);
                        }
                    }
                });
                //绑定数据
                imageView.setTag(new Jigsaw(i, j, bitmap));
                //添加到拼图布局
                mImageViewArray[i][j] = imageView;
                mGridLayout.addView(imageView);
            }
        }
        //设置拼图空碎片
        ImageView imageView = (ImageView) mGridLayout.getChildAt(mGridLayout.getChildCount() - 1);
        imageView.setImageBitmap(null);
        mEmptyImageView = imageView;

    }

    /**
     * 处理拼图间的移动
     *
     * @param imageView
     * @param animation
     */
    private void handleClickItem(final ImageView imageView, boolean animation) {
        if (animation) {
            handleClickItem(imageView);
        } else {
            changeJigsawData(imageView);
        }
    }


    /**
     * 处理点击拼图的移动事件
     *
     * @param imageView
     */
    private void handleClickItem(final ImageView imageView) {
        if (!isAnimated) {
            TranslateAnimation translateAnimation = null;
            if (imageView.getX() < mEmptyImageView.getX()) {
                //左往右
                translateAnimation = new TranslateAnimation(0, imageView.getWidth(), 0, 0);
            }

            if (imageView.getX() > mEmptyImageView.getX()) {
                //右往左
                translateAnimation = new TranslateAnimation(0, -imageView.getWidth(), 0, 0);
            }

            if (imageView.getY() > mEmptyImageView.getY()) {
                //下往上
                translateAnimation = new TranslateAnimation(0, 0, 0, -imageView.getHeight());
            }

            if (imageView.getY() < mEmptyImageView.getY()) {
                //上往下
                translateAnimation = new TranslateAnimation(0, 0, 0, imageView.getHeight());
            }

            if (translateAnimation != null) {
                translateAnimation.setDuration(80);
                translateAnimation.setFillAfter(true);
                translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        isAnimated = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //清除动画
                        isAnimated = false;
                        imageView.clearAnimation();
                        //交换拼图数据
                        changeJigsawData(imageView);
                        //判断游戏是否结束
                        boolean isFinish = JigsawHelper.getInstance().isFinishGame(mImageViewArray, mEmptyImageView);
                        if (isFinish) {
                            Toast.makeText(MainActivity.this, "拼图成功，游戏结束！", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                imageView.startAnimation(translateAnimation);
            }
        }
    }


    /**
     * 交换拼图数据
     *
     * @param imageView
     */
    public void changeJigsawData(ImageView imageView) {
        Jigsaw emptyJigsaw = (Jigsaw) mEmptyImageView.getTag();
        Jigsaw jigsaw = (Jigsaw) imageView.getTag();

        //更新imageView的显示内容
        mEmptyImageView.setImageBitmap(jigsaw.getBitmap());
        imageView.setImageBitmap(null);
        //交换数据
        emptyJigsaw.setCurrentX(jigsaw.getCurrentX());
        emptyJigsaw.setCurrentY(jigsaw.getCurrentY());
        emptyJigsaw.setBitmap(jigsaw.getBitmap());

        //更新空拼图引用
        mEmptyImageView = imageView;
    }

    /**
     * 处理手势移动拼图
     *
     * @param gestureDirection
     * @param animation        是否带有动画
     */
    private void handleFlingGesture(int gestureDirection, boolean animation) {
        ImageView imageView = null;
        Jigsaw emptyJigsaw = (Jigsaw) mEmptyImageView.getTag();
        switch (gestureDirection) {
            case GestureHelper.LEFT:
                if (emptyJigsaw.getOriginalY() + 1 <= mGridLayout.getColumnCount() - 1) {
                    imageView = mImageViewArray[emptyJigsaw.getOriginalX()][emptyJigsaw.getOriginalY() + 1];
                }
                break;
            case GestureHelper.RIGHT:
                if (emptyJigsaw.getOriginalY() - 1 >= 0) {
                    imageView = mImageViewArray[emptyJigsaw.getOriginalX()][emptyJigsaw.getOriginalY() - 1];
                }
                break;
            case GestureHelper.UP:
                if (emptyJigsaw.getOriginalX() + 1 <= mGridLayout.getRowCount() - 1) {
                    imageView = mImageViewArray[emptyJigsaw.getOriginalX() + 1][emptyJigsaw.getOriginalY()];
                }
                break;
            case GestureHelper.DOWN:
                if (emptyJigsaw.getOriginalX() - 1 >= 0) {
                    imageView = mImageViewArray[emptyJigsaw.getOriginalX() - 1][emptyJigsaw.getOriginalY()];
                }
                break;
            default:
                break;
        }
        if (imageView != null) {
            handleClickItem(imageView, animation);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
}
