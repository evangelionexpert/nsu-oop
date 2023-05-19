package ru.nsu.fit.smolyakov.labchecker.entity;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@Value
@RequiredArgsConstructor
@NonNull
public class Student {
    String nickName;
    String fullName;
    String repoLink;

    List<TaskInfo> tasks
        = new ArrayList<>();
    List<LessonInfo> lessons
        = new ArrayList<>();
}