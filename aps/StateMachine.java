public class StateMachine {

    private int activeState = 0;
    private TransitionTable states;
    // prevState, input, newState

    private String letMin = "[a-z]";
    private String num = "[0-9]";
    private String op_arit = "[+\\-/*]";
    private String op_atrib = "=";
    private String under = "_";
    private String pv = ";";
    private String dot = "\\.";

    public StateMachine() {

        states = new TransitionTable();

        states.createTransition(0, letMin, 1);

        states.createTransition(1, under, 1);
        states.createTransition(1, letMin, 1);
        states.createTransition(1, num, 1);
        states.createTransition(1, op_atrib, 2);

        states.createTransition(2, num, 3);
        states.createTransition(2, letMin, 4);

        states.createTransition(3, num, 3);
        states.createTransition(3, dot, 6);
        states.createTransition(3, op_arit, 7);
        states.createTransition(3, pv, 5);

        states.createTransition(4, letMin, 4);
        states.createTransition(4, num, 4);
        states.createTransition(4, under, 4);
        states.createTransition(4, op_arit, 7);
        states.createTransition(4, pv, 5);

        states.createTransition(6, num, 6);
        states.createTransition(6, op_arit, 7);
        states.createTransition(6, pv, 5);

        states.createTransition(7, num, 3);
        states.createTransition(7, letMin, 4);
    }

    public int transition(String input) {


        if (input.matches(letMin)) {

            System.out.printf("δ(%d, %s)\n", activeState, letMin);
            activeState = states.change(activeState, letMin);

        } else if (input.matches(num)) {

            System.out.printf("δ(%d, %s)\n", activeState, num);
            activeState = states.change(activeState, num);

        } else if (input.matches(op_arit)) {

            System.out.printf("δ(%d, %s)\n", activeState, op_arit);
            activeState = states.change(activeState, op_arit);

        } else if (input.matches(op_atrib)) {

            System.out.printf("δ(%d, %s)\n", activeState, op_atrib);
            activeState = states.change(activeState, op_atrib);

        } else if (input.matches(pv)) {

            System.out.printf("δ(%d, %s)\n", activeState, pv);
            activeState = states.change(activeState, pv);

        } else if (input.matches(dot)) {

            System.out.printf("δ(%d, %s)\n", activeState, dot);
            activeState = states.change(activeState, dot);
        }

        try {

            activeState += 0;

            if (activeState == 5){

                activeState = 0;
                return 5;

            } else {

                return activeState;
            }

        } catch (NullPointerException e){

            activeState = -1;
            return activeState;
        }

    }
}
