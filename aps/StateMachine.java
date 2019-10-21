public class StateMachine {

    private String activeState = "q0";
    private String letMin = "[a-z]";
    private String id = "[a-z_0-9]";
    private String num = "[0-9]";
    private String op_arit = "[+\\-/*]";

    public void setActiveState(String state) {

        this.activeState = state;
    }

    public String transition(String input) {

        if (activeState.equals("q0")) {

            if (input.matches(letMin)) {

                return "q1";
            }

        } else if (activeState.equals("q1")) {

            if (input.matches(id)) {

                return "q1";

            } else if (input.matches(" ")){

                return "q2";
            }

        } else if (activeState.equals("q2")) {

            if (input.matches("=")) {

                return "q3";
            }

        } else if (activeState.equals("q3")) {

            if (input.matches(" ")) {

                return "q4";
            }

        } else if (activeState.equals("q4")) {

            if (input.matches(letMin)) {

                return "q6";

            } else if (input.matches(num)){

                return "q5";
            }

        } else if (activeState.equals("q5")) {

            if (input.matches(num)) {

                return "q5";

            } else if (input.matches("\\.")){

                return "q8";

            } else if (input.matches(op_arit)){

                return "q7";

            } else if (input.matches(";")) {

                return "f1";
            }

        } else if (activeState.equals("q6")) {

            if (input.matches(id)) {

                return "q6";

            } else if (input.matches(op_arit)){

                return "q7";

            } else if (input.matches(op_arit)){

                return "q7";

            } else if (input.matches(";")) {

                return "f1";
            }

        } else if (activeState.equals("q7")) {

            if (input.matches(" ")){

                return "q4";
            }

        } else if (activeState.equals("q8")) {

            if (input.matches(num)){

                return "q9";
            }

        } else if (activeState.equals("q9")) {

            if (input.matches(";")){

                return "f1";

            } else if (input.matches(op_arit)) {

                return "q7";
            }
        }

        return "INVALID";
    }
}
