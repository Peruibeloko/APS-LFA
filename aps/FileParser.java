import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FileParser {

    private StringBuilder currentWord = new StringBuilder();
    private StateMachine st = new StateMachine();

    private String filename;
    private BufferedReader input = null;
    private Output gui;

    LinkedList<String> exp = new LinkedList<>();
    Stack<String> ops = new Stack<>();
    String currVar = "";

    private HashMap<String, Double> varList = new HashMap<>();

    private boolean noError = true;
    private boolean creatingVars = true;

    public FileParser(Output gui, String filename) {

        this.gui = gui; /* Reference to the output screen */
        this.filename = filename; /* File to be processed */
    }

    void readFile() {

        int i = 0;

        try {

            input = new BufferedReader(new FileReader(filename));
            gui.addText("----- BEGIN FILE READ -----\n\n");

            while (input.ready() && noError) {

                if (creatingVars) {

                    currentWord.append(input.readLine());

                    if (currentWord.toString().equals("")) {

                        creatingVars = false;
                        gui.addText("\n----- END VARIABLE DECLARATION, BEGIN PROCESSING -----\n\n");

                    } else {

                        gui.addText(currentWord.toString() + "\n");
                        String[] split = currentWord.toString().replace(";", "").split("=");

                        if (split[0].trim().charAt(0) < 97 || split[0].trim().charAt(0) > 122) {

                            gui.addText("Invalid Identifier: " + split[0] + "\n");
                            noError = false;

                        } else {

                            varList.put(split[0].trim(), Double.parseDouble(split[1].trim()));
                            currentWord = new StringBuilder();
                        }
                    }

                } else {

                    int ch = input.read();

                    if (ch == 13) { // Carriage Return

                        gui.addText("[CR]");

                    } else if (ch == 10) { // Line Feed

                        gui.addText("[LF]\n");

                    } else if (ch == 32) { // Space

                        gui.addText("[ ]");

                    } else {

                        gui.addText((char) ch);
                        parseCharacter(ch);
                    }

                    i++;
                }
            }

            input.close();
            gui.addText("\n\n----- END FILE READ -----\n\n");

        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo " + filename);
        }

        gui.addText("Characters read: " + i + "\n");
        gui.addText("Variables: " + varList.size() + "\n\n");

        Object[] keyList = varList.keySet().toArray();
        for (int j = 0; j < keyList.length; j++) {

            Double val = varList.get(keyList[j].toString());

            if (val % 1 != 0)
                gui.addText(keyList[j].toString() + ": " + val + "\n");
            else
                gui.addText(keyList[j].toString() + ": " + val.intValue() + "\n");
        }
    }

    void parseCharacter(int ch) {

        currentWord.append((char) ch);
        int opCode = st.transition("" + (char) ch);

        if (opCode == 2) {

            String temp = currentWord.toString().trim();

            if (currVar.equals("")) {

                currVar = temp.substring(0, temp.length() - 1);

                try {
                    varList.get(currVar).toString();
                } catch (Exception f) {

                    noError = false;
                    gui.addText("\nNon existent variable: " + currVar + "\nINVALID ATTRIBUTION");
                }

            } else {
                String temp2 = temp.substring(0, temp.length() - 1);
                exp.add(temp2);

                if (temp2.charAt(0) >= 97 && temp2.charAt(0) <= 122)
                    try {
                        varList.get(temp2).doubleValue();
                    } catch (Exception e) {
                        noError = false;
                        gui.addText("\nNon existent variable: " + temp2 + "\nINVALID ATTRIBUTION");
                    }

                while (!ops.empty() && precedence((char) ch) <= precedence(ops.peek().charAt(0)))
                    exp.add(ops.pop());

                ops.push("" + temp.charAt(temp.length() - 1));
            }

            currentWord = new StringBuilder();

        } else if (opCode == 6) {

            String temp = currentWord.toString().trim();
            exp.add(temp.substring(0, temp.length() - 1));

            while (!ops.isEmpty())
                exp.add(ops.pop());

            currentWord = new StringBuilder();
            processLine();
            currVar = "";

        } else if (opCode == -1) {

            noError = false;
            gui.addText("\nCurrent Capture: " + currentWord.toString() + "\nINVALID ATTRIBUTION");
            currentWord = new StringBuilder();
        }
    }

    void processLine() {

        int i = 0;
        while (exp.size() > 1) {

            if (exp.get(i).matches("[+\\-/*]")) {

                double a, b;
                String tok1 = exp.get(i - 2);
                String tok2 = exp.get(i - 1);

                if (tok1.charAt(0) >= 97 && tok1.charAt(0) <= 122)
                    a = varList.get(tok1);
                else
                    a = Double.parseDouble(tok1);

                if (tok2.charAt(0) >= 97 && tok2.charAt(0) <= 122)
                    b = varList.get(tok2);
                else
                    b = Double.parseDouble(tok2);

                char op = exp.get(i).charAt(0);

                try {

                    exp.set(i, String.valueOf(applyOp(a, b, op)));
                    i -= 2;
                    exp.remove(i);
                    exp.remove(i);
                    i = 0;

                } catch (Exception e) {

                    noError = false;
                    gui.addText("\nCurrent Capture: " + currentWord.toString() + "\nINVALID ATTRIBUTION");
                }

            } else {
                i++;
            }
        }

        varList.replace(currVar, Double.parseDouble(exp.pop()));
    }

    int precedence(char op) {
        if (op == '+' || op == '-')
            return 1;
        if (op == '*' || op == '/')
            return 2;
        return 0;
    }

    double applyOp(double a, double b, char op) throws Exception {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0)
                    throw new Exception();
                else
                    return a / b;
            default:
                throw new Exception();
        }
    }
}
