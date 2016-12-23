package redux.api.helpers;

public class ActionsCreator {
    public static Object unknownAction() {
        return "unknownAction";
    }

    public static Object addTodo(String message) {
        return new Actions.AddTodo(message);
    }
}
