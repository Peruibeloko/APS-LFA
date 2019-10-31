import java.util.HashMap;

public class TransitionTable {

    HashMap<String, Integer> matrix;

    public TransitionTable() {

        this.matrix = new HashMap<>();
    }

    public int change(int state, String input) {

        String key = state + "|" + input;

        try {

            return matrix.get(key);

        } catch (NullPointerException e) {

            return -1;
        }
    }

    public void createTransition(int prevState, String input, int newState) {

        String key = prevState + "|" + input;
        matrix.put(key, newState);
    }
}