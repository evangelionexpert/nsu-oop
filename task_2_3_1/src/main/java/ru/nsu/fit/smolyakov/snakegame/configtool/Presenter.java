package ru.nsu.fit.smolyakov.snakegame.configtool;

import javafx.beans.value.ChangeListener;
import javafx.stage.Stage;
import ru.nsu.fit.smolyakov.snakegame.GameData;
import ru.nsu.fit.smolyakov.snakegame.executable.JavaFxSnakeGame;
import ru.nsu.fit.smolyakov.snakegame.properties.GameSpeed;

import java.io.IOException;
import java.util.Arrays;

/**
 * The presenter of the configuration tool.
 */
public class Presenter {
    private Model model;
    private View view;

    /**
     * Creates a new presenter.
     *
     * @param model the model of the configuration tool
     * @param view the view of the configuration tool
     */
    public Presenter(Model model, View view) {
        this.model = model;
        this.view = view;

        view.setPresenter(this);

        view.initWidthSelector(model.getProperties().width());
        view.initHeightSelector(model.getProperties().height());
        view.initApplesAmountSelector(model.getProperties().apples(), getMaxApplesAvailable());

        view.setAvailableGameSpeedChoices(Arrays.asList(GameSpeed.values()));
        view.setGameSpeed(model.getProperties().speed());

        view.initAiNames();
        view.setAvailableAiNames(GameData.INSTANCE.getAvailableAiNames());
        view.setSelectedAiNames(model.getProperties().aiClassNamesList());

        view.setAvailableLevelNames(GameData.INSTANCE.levelFileNames());
        view.setSelectedLevel(model.getProperties().levelFileName());

        view.setSelectedJavaFxScalingValue(model.getProperties().javaFxScaling());
        view.updateCalculatedResolution();
    }

    /**
     * Sets the {@link Model}'s properties to the values from the view
     * and saves them to the file.
     */
    public void saveConfig() {
        model.setProperties(
            model.getProperties()
                .withSpeed(view.getGameSpeed())
                .withApples(view.getApplesAmount())
                .withWidth(view.getWidth())
                .withHeight(view.getHeight())
                .withJavaFxScaling(view.getJavaFxScalingValue())
                .withBarrierFilePath(view.getLevel())
                .withAiClassNamesList(view.getSelectedAiNames())
        );

        try {
            model.sync();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Runs the game with the saved configuration.
     * Usually, if this method is called, it is called after {@link #saveConfig()}.
     */
    public void runJavaFxSnake() {
        var app = new JavaFxSnakeGame();
        var stage = new Stage();
        view.hide();

        try {
            app.runGame(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@link ChangeListener} that is called when the scaling value is changed.
     */
    public final ChangeListener<Number> onScalingChangedListener = (observable, oldValue, newValue) -> {
        onScalingChanged();
    };

    /**
     * Returns the maximum number of apples that can be placed on the game field.
     *
     * @return the maximum number of apples that can be placed on the game field
     */
    public int getMaxApplesAvailable() {
        return view.getWidth() * view.getHeight() - 4;
    }

    /**
     * {@link ChangeListener} that is called when the maximum number of apples is changed.
     * Usually, that happens when the width or height of the game field is changed.
     */
    public final ChangeListener<Number> onMaxApplesLimitChangeListener = (observable, oldValue, newValue) -> {
        var newMax = getMaxApplesAvailable();
        var prevVal = view.getApplesAmount();

        view.setApplesAvailableRange(newMax);
        view.setApplesAmount(Math.min(prevVal, newMax));
    };

    /**
     * {@link ChangeListener} that is called when the width or height of the game field is changed.
     */
    public final ChangeListener<Number> onFieldSizeChangeListener = (observable, oldValue, newValue) -> {
        onMaxApplesLimitChangeListener.changed(observable, oldValue, newValue);
        onScalingChangedListener.changed(observable, oldValue, newValue);
    };

    /**
     * Updates the resolution text in the view.
     */
    public void onScalingChanged() {
        var scaling = view.getJavaFxScalingValue();
        view.setResolutionText(
            scaling * view.getWidth(),
            scaling * view.getHeight()
        );
    }
}
