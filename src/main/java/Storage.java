import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Storage {
    public static String filePath = "";

    public Storage (String filePath) {
        this.filePath = filePath;
    }

    public LinkedList<Task> load () {
        File file = new File(filePath);
        LinkedList<Task> loadedTasks = new LinkedList<>();

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                String taskDetails = sc.nextLine();
                String[] taskArray = taskDetails.split(" \\| ");
                String actionTask = taskArray[0];
                String taskDescription = taskArray[2];
                boolean isDone = taskArray[1].equals("1");

                if (actionTask.equals("T")) {
                    Todo newTodo = new Todo(taskDescription);
                    if (isDone) {
                        newTodo.markAsDone();
                    }

                    loadedTasks.add(newTodo);
                } else if (actionTask.equals("D")) {
                    Deadline newDeadline = new Deadline(taskDescription, Parser.convertDateAndTime(taskArray[3]));
                    if (isDone) {
                        newDeadline.markAsDone();
                    }

                    loadedTasks.add(newDeadline);
                } else if (actionTask.equals("E")) {
                    Event newEvent = new Event(taskDescription, Parser.convertDateAndTime(taskArray[3]));
                    if (isDone) {
                        newEvent.markAsDone();
                    }

                    loadedTasks.add(newEvent);
                } else {
                    throw new DukeException("Not an action!");
                }
            }
        } catch (FileNotFoundException err) {
            System.out.println("File not found!");
        } catch (DukeException err) {
            System.out.println(err);
        } catch (IOException err) {
            System.out.println(err);
        }

        return loadedTasks;
    }

    public static void writeFile (LinkedList<Task> updatedTask) {
        File file = new File(filePath);
        boolean isFileExists = file.exists();

        try {
            if (!isFileExists) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(filePath);
            for (Task subTask: updatedTask) {
                if (subTask instanceof Todo) {
                    String newTodo = "T | " +
                            (subTask.isDone ? "1" : "0") + " | " +
                            (subTask.getDescription());
                    fw.write(newTodo + System.lineSeparator());
                } else if (subTask instanceof Deadline) {
                    String newDeadline = "D | " +
                            (subTask.isDone ? "1" : "0") + " | " +
                            (subTask.getDescription()) + " | " +
                            (((Deadline) subTask).getBy());
                    fw.write(newDeadline + System.lineSeparator());
                } else if (subTask instanceof Event) {
                    String newEvent = "E | " +
                            (subTask.isDone ? "1" : "0") + " | " +
                            (subTask.getDescription()) + " | " +
                            (((Event) subTask).getAt());
                    fw.write(newEvent + System.lineSeparator());
                } else {
                    fw.close();
                    throw new DukeException("Task is unknown");
                }
            }

            fw.close();
        } catch (IOException e) {
            System.out.println("Error when trying to write a file" + e.getMessage());
        } catch (DukeException err) {
            System.out.println(err.getMessage());
        }
    }
}