package ru.nsu.fit.smolyakov.evaluationsuite.interpreterprovider;

import lombok.NonNull;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.AbstractCommandProvider;
import ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.ConsoleCommand;
import ru.nsu.fit.smolyakov.consoleinterpreter.exception.InternalCommandException;
import ru.nsu.fit.smolyakov.consoleinterpreter.processor.ConsoleProcessor;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.SubjectData;
import ru.nsu.fit.smolyakov.evaluationsuite.entity.group.Student;

import java.time.LocalDate;

/**
 * Command provider for the "forStudent" command.
 * Allows to override student status. Also allows to open a new block of commands for a specified task.
 *
 * @see ru.nsu.fit.smolyakov.consoleinterpreter.commandprovider.annotation.ConsoleCommand
 */
public class ForStudentCommandProvider extends AbstractCommandProvider {
    private final Student student;
    private final SubjectData subjectData;

    /**
     * Creates a new command provider for the "forStudent" command.
     *
     * @param consoleProcessor console processor
     * @param studentName      student name used for representation
     * @param student          student
     * @param subjectData      subject data
     */
    public ForStudentCommandProvider(@NonNull ConsoleProcessor consoleProcessor,
                                     @NonNull String studentName,
                                     @NonNull Student student,
                                     @NonNull SubjectData subjectData) {
        super(consoleProcessor, "student [" + studentName + "]");
        this.student = student;
        this.subjectData = subjectData;
    }

    @ConsoleCommand(description = "sets student as been on a lesson")
    private void beenOnLesson(String lessonDate) {
        subjectData.getCourse()
            .getLessons()
            .getByDate(LocalDate.parse(lessonDate))
            .flatMap(student::getLessonStatusByLesson)
            .ifPresent(lessonStatus -> lessonStatus.setBeenOnALesson(true));
    }

    @ConsoleCommand(description = "opens a new block of commands for a specified task")
    private void forTask(String taskName) {
        this.subjectData.getCourse()
            .getAssignments()
            .getByIdentifier(taskName)
            .ifPresentOrElse(
                assignment -> {
                    student.getAssignmentStatusByAssignment(assignment)
                        .ifPresentOrElse(
                            assignmentStatus -> {
                                getConsoleProcessor().getProviderStack().push(
                                    new ForTaskCommandProvider(
                                        getConsoleProcessor(),
                                        taskName,
                                        assignmentStatus,
                                        subjectData
                                    )
                                );
                            },
                            () -> {
                                throw new InternalCommandException("This student has no specified task (why???)");
                            }
                        );

                },
                () -> {
                    throw new InternalCommandException("No task with such identifier");
                }
            );
    }
}