package redux.api.helpers;

public final class Todo {
    final int id;
    final String message;

    public Todo(int id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Todo todo = (Todo) o;

        if (id != todo.id) return false;
        return message != null ? message.equals(todo.message) : todo.message == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
