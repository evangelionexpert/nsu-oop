package ru.nsu.fit.smolyakov.snake.presenter;

import ru.nsu.fit.smolyakov.snake.model.GameField;
import ru.nsu.fit.smolyakov.snake.model.Snake;
import ru.nsu.fit.smolyakov.snake.view.View;

public class Presenter {
    private View view;
    private GameField model;

    public Presenter(View view, GameField model) {
        this.view = view;
        this.model = model;
    }

    public void start() {
        new Thread(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            while (true) {
                model.update();

                view.clear();
                view.draw(model.getApplesSet());
                view.draw(model.getBarrier());
                view.draw(model.getPlayerSnake());
                view.setScoreAmount(model.getPlayerSnake().getPoints());

                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        ).start();
    }

    public void update() {

    }

    public void onLeftKeyPressed() {
        model.getPlayerSnake().setMovingDirection(Snake.MovingDirection.LEFT);

    }

    public void onRightKeyPressed() {
        model.getPlayerSnake().setMovingDirection(Snake.MovingDirection.RIGHT);
    }

    public void onUpKeyPressed() {
        model.getPlayerSnake().setMovingDirection(Snake.MovingDirection.UP);
    }

    public void onDownKeyPressed() {
        model.getPlayerSnake().setMovingDirection(Snake.MovingDirection.DOWN);
    }
}
