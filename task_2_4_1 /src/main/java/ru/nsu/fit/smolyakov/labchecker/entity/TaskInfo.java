package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.*;
import lombok.experimental.NonFinal;

import java.time.LocalDate;
import java.util.Objects;

@Value
@RequiredArgsConstructor
public class TaskInfo {
    @NonNull Task task;
    @NonNull String branch;

    @NonNull @NonFinal LocalDate started = LocalDate.MAX;
    @NonNull @NonFinal LocalDate finished = LocalDate.MAX;

    @Setter @NonFinal @NonNull String message = "no message";

    @NonFinal Double overriddenTaskPoints = null;

    @NonFinal boolean buildOk = false;
    @NonFinal boolean testsOk = false;
    @NonFinal boolean javadocOk = false;

    public void startedAt(String dateString) {
        this.started = LocalDate.parse(dateString);
    }

    public void finishedAt(String dateString) {
        this.finished = LocalDate.parse(dateString);
    }

    public void overrideTaskPoints(double points) {
        this.overriddenTaskPoints = points;
    }

    public void notOverrideTaskPoints() {
        this.overriddenTaskPoints = null;
    }

    public boolean isOverridden() {
        return Objects.nonNull(this.overriddenTaskPoints);
    }

    public double getCalculatedTaskPoints() {
        double sum = 0;
        int amount = 0;

        sum += (buildOk ? 1 : 0);
        amount++;
        sum += (javadocOk ? 1 : 0);
        amount++;

        if (this.task.isRunTests()) {
            sum += (testsOk ? 1 : 0);
            amount++;
        }
        return this.task.getSolvedPoints() * (sum / amount);
    }

    public double getTaskPoints() {
        return this.isOverridden() ? this.overriddenTaskPoints : this.getCalculatedTaskPoints();
    }

    public double getFine() {
        double fine = 0;
        if (this.started.isAfter(this.task.getSoftDeadline())) {
            fine += this.task.getSoftDeadlineSkipFine();
        }
        if (this.finished.isAfter(this.task.getHardDeadline())) {
            fine += this.task.getHardDeadlineSkipFine();
        }

        return fine;
    }

    public double getResultingPoints() {
        return getTaskPoints() - getFine();
    }
}