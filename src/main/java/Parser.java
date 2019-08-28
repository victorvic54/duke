import java.time.LocalDateTime;

public class Parser {
    public static LocalDateTime convertDateAndTime(String rawTimestamp) throws DukeException {
        String[] dateTime = rawTimestamp.split(" ");
        String[] datePortion = dateTime[0].split("/");
        String timePortion = dateTime[1];

        if (dateTime.length == 2) {
            int date = Integer.parseInt(datePortion[0]);
            int month = Integer.parseInt(datePortion[1]);
            int year = Integer.parseInt(datePortion[2]);
            int hour = Integer.parseInt(timePortion.substring(0, 2));
            int minute = Integer.parseInt(timePortion) % 100;

            return LocalDateTime.of(year, month, date, hour, minute);
        } else {
            throw new DukeException("Date time format invalid");
        }
    }

    public static void parse(TaskList tasks, Ui ui, String inputText, Storage storage) {
        String[] keyList = inputText.split(" ", 2);
        String actionKey = keyList[0];

        try {
            if (inputText.equals("list")) { // to print all the list of plans
                ui.printList(tasks);
            } else if (actionKey.equals("done")) { // mark as done if the plan is finished
                int index = Integer.parseInt(inputText.split(" ")[1]);
                Task selectedTask = tasks.getListOfTasks().get(index - 1);
                selectedTask.markAsDone();
                ui.printTaskDone(selectedTask);

                storage.writeFile(tasks.getListOfTasks());
            } else if (actionKey.equals("delete")) { // delete a specific plan

                int index = Integer.parseInt(keyList[1]);
                ui.printTaskDelete(tasks.getListOfTasks(), index);
                tasks.deleteTask(index - 1);

                storage.writeFile(tasks.getListOfTasks());
            } else { // to handle addition of a specific type of plan
                if (actionKey.equals("deadline")) {
                    if (keyList.length <= 1) {
                        throw new DukeException("☹ OOPS!!! The description of a deadline cannot be empty.");
                    }

                    String[] contentList = keyList[1].split(" /by ");
                    if (contentList.length <= 1) {
                        throw new DukeException("☹ OOPS!!! Time need to be specified");
                    }

                    LocalDateTime convertedTimeStamp = convertDateAndTime(contentList[1]);
                    Deadline newDeadline = new Deadline(contentList[0], convertedTimeStamp);
                    tasks.addTask(newDeadline);
                    ui.printAddTask(tasks, newDeadline);
                    storage.writeFile(tasks.getListOfTasks());
                } else if (actionKey.equals("event")) {
                    if (keyList.length <= 1) {
                        throw new DukeException("☹ OOPS!!! The description of an event cannot be empty.");
                    }

                    String[] contentList = keyList[1].split(" /at ");
                    if (contentList.length == 1) {
                        throw new DukeException("☹ OOPS!!! Time need to be specified");
                    }

                    LocalDateTime convertedTimeStamp = convertDateAndTime(contentList[1]);
                    Event newEvent = new Event(contentList[0], convertedTimeStamp);
                    tasks.addTask(newEvent);
                    ui.printAddTask(tasks, newEvent);
                    storage.writeFile(tasks.getListOfTasks());
                } else if (actionKey.equals("todo")) {
                    if (keyList.length <= 1) {
                        throw new DukeException("☹ OOPS!!! The description of a todo cannot be empty.");
                    }

                    Todo newTodo = new Todo(inputText.split(" ", 2)[1]);
                    tasks.addTask(newTodo);
                    ui.printAddTask(tasks, newTodo);
                    storage.writeFile(tasks.getListOfTasks());
                } else {
                    throw new DukeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            }
        } catch (DukeException err) {
            System.out.println(err.getMessage());
        } catch (Exception err) {
            System.out.println(err);
        }
    }
}