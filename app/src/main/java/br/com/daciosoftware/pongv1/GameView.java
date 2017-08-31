package br.com.daciosoftware.pongv1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import br.com.daciosoftware.simplegameenginev1.SGImage;
import br.com.daciosoftware.simplegameenginev1.SGImageFactory;
import br.com.daciosoftware.simplegameenginev1.SGRenderer;
import br.com.daciosoftware.simplegameenginev1.SGView;

/**
 * Created by fdacio on 18/08/17.
 */
public class GameView extends SGView {

    private Context context;
    private Paint mTempPaint = new Paint();

    private final static int BALL_SIZE = 16;
    private final static int DISTANCE_FROM_EDGE = 16;
    private final static int PADDLE_HEIGHT = 98;
    private final static int PADDLE_WIDTH = 23;
    private final static int AREA_HEIGTH = 55;
    private final static int AREA_WIDTH = 200;

    private final static int BALL_SPEED = 120;
    private final static int PLAYER_SPEED = 120;
    private final static int OPPONENT_SPEED = 120;

    private RectF mBallDestination = new RectF();
    private RectF mOpponentDestination = new RectF();
    private RectF mPlayerDestination = new RectF();

    private SGImage mBallImage;
    private SGImage mOpponentImage;
    private SGImage mPlayerImage;

    private Rect mTempImageSource = new Rect();

    private Rect mAreaAlfa1 = new Rect();
    private Rect mAreaAlfa2 = new Rect();


    private boolean mBallMoveRight = true;
    private boolean mPlayerMoveUp = true;
    private boolean mOpponentMoveDown = true;

    public GameView(Context context){
        super(context);
        this.context = context;
    }

    @Override
    protected void setup(){

        Point viewDimensions = getmDimensions();
        Point viewCenter = new Point(viewDimensions.x/2, viewDimensions.y/2);
        SGImageFactory mImageFactory = getmImageFactory();

        int halfBall = BALL_SIZE / 2;
        int halfPaddleHeight = PADDLE_HEIGHT / 2;

        mBallDestination.set(viewCenter.x - halfBall, //Esquerda
                viewCenter.y - halfBall, //Topo
                viewCenter.x + halfBall, //Direita
                viewCenter.y + halfBall //Base
        );

        mPlayerDestination.set(DISTANCE_FROM_EDGE,//Esquerda
                viewCenter.y - halfPaddleHeight, //Topo
                DISTANCE_FROM_EDGE + PADDLE_WIDTH, //Direita
                viewCenter.y + halfPaddleHeight //Base
        );

        mOpponentDestination.set(viewDimensions.x - (DISTANCE_FROM_EDGE + PADDLE_WIDTH), //Esquerda
                viewCenter.y - halfPaddleHeight,//Topo
                viewDimensions.x - DISTANCE_FROM_EDGE,//Direita
                viewCenter.y + halfPaddleHeight //Base
        );

        mAreaAlfa1.set(0,
                viewCenter.y - (AREA_WIDTH/2),
                AREA_HEIGTH,
                viewCenter.y + (AREA_WIDTH/2)
        );

        mAreaAlfa2.set(
                viewDimensions.x - (AREA_HEIGTH),
                viewCenter.y - (AREA_WIDTH/2),
                viewDimensions.x,
                viewCenter.y + (AREA_WIDTH/2)
        );

        mBallImage = mImageFactory.createImage(R.drawable.ball);
        mOpponentImage = mImageFactory.createImage("opponent.png");
        mPlayerImage = mImageFactory.createImage("player.png");

    }

    public void moveBall(float elapsedTimeInSeconds){

        Point viewDimensions = getmDimensions();

        int limitRigth = viewDimensions.x -(PADDLE_WIDTH + DISTANCE_FROM_EDGE);
        int limitLeft = PADDLE_WIDTH + DISTANCE_FROM_EDGE;

        if(mBallMoveRight){

            mBallDestination.left += BALL_SPEED * elapsedTimeInSeconds;
            mBallDestination.right += BALL_SPEED * elapsedTimeInSeconds;

            if(mBallDestination.right >= limitRigth){

                mBallDestination.left = limitRigth - BALL_SIZE;
                mBallDestination.right = limitRigth;

                mBallMoveRight = false;
            }
        }
        else{

            mBallDestination.left -= BALL_SPEED * elapsedTimeInSeconds;
            mBallDestination.right -= BALL_SPEED * elapsedTimeInSeconds;

            if(mBallDestination.left < limitLeft){

                mBallDestination.left = limitLeft;
                mBallDestination.right = BALL_SIZE + limitLeft;

                mBallMoveRight = true;

            }
        }
    }

    public void movePlayer(float elapsedTimeInSeconds){

        Point viewDimensions = getmDimensions();

        if(mPlayerMoveUp){

            mPlayerDestination.top -= PLAYER_SPEED * elapsedTimeInSeconds;
            mPlayerDestination.bottom -= PLAYER_SPEED * elapsedTimeInSeconds;

            if(mPlayerDestination.top < 0){

                mPlayerDestination.top = 0;
                mPlayerDestination.bottom = PADDLE_HEIGHT;

                mPlayerMoveUp = false;

            }

        }else{

            mPlayerDestination.top += PLAYER_SPEED * elapsedTimeInSeconds;
            mPlayerDestination.bottom += PLAYER_SPEED * elapsedTimeInSeconds;

            if(mPlayerDestination.top > (viewDimensions.y - PADDLE_HEIGHT) ){

                mPlayerDestination.top = viewDimensions.y - PADDLE_HEIGHT;
                mPlayerDestination.bottom = viewDimensions.y;

                mPlayerMoveUp = true;

            }
        }
    }

    public void moveOpponent(float elapsedTimeInSeconds){

        Point viewDimensions = getmDimensions();

        if(mOpponentMoveDown){

            mOpponentDestination.top += OPPONENT_SPEED * elapsedTimeInSeconds;
            mOpponentDestination.bottom += OPPONENT_SPEED * elapsedTimeInSeconds;

            if(mOpponentDestination.bottom >= viewDimensions.y){

                mOpponentDestination.top = viewDimensions.y - PADDLE_HEIGHT;
                mOpponentDestination.bottom = viewDimensions.y;

                mOpponentMoveDown = false;

            }
        }
        else{

            mOpponentDestination.top -= OPPONENT_SPEED * elapsedTimeInSeconds;
            mOpponentDestination.bottom -= OPPONENT_SPEED * elapsedTimeInSeconds;

            if(mOpponentDestination.top < 0){

                mOpponentDestination.top = 0;
                mOpponentDestination.bottom = PADDLE_HEIGHT;

                mOpponentMoveDown = true;

            }

        }

    }

    @Override
    public void step(Canvas canvas, float elapsedTimeInSeconds){

        moveBall(elapsedTimeInSeconds);
        movePlayer(elapsedTimeInSeconds);
        moveOpponent(elapsedTimeInSeconds);

        SGRenderer renderer = getmRenderer();

        renderer.beginDrawing(canvas, Color.BLACK);

        mTempImageSource.set(0, 0, PADDLE_WIDTH, PADDLE_HEIGHT);
        renderer.drawImage(mPlayerImage, mTempImageSource, mPlayerDestination);
        renderer.drawImage(mOpponentImage, mTempImageSource, mOpponentDestination);

        mTempImageSource.set(0,0,BALL_SIZE,BALL_SIZE);
        renderer.drawImage(mBallImage, mTempImageSource, mBallDestination);

        renderer.drawRect(mAreaAlfa1,Color.WHITE, 125);
        renderer.drawRect(mAreaAlfa2,Color.WHITE, 125);

        renderer.endDrawing();

    }
}
