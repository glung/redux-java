package redux.api.helpers;

class Actions {

    static class AddTodo {
        final String message;

        AddTodo(String message) {
            this.message = message;
        }
    }
}
