import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Iterator;

public class FileParser {

    private StringBuilder currentWord = new StringBuilder();
    private StateMachine st = new StateMachine();
    private BufferedReader input = null;
    private String filename, name;
    private double value;
    private Output gui;
    private Map<String, Double> varList = new HashMap<>();
    private Entry<String, Double> en;
    private Iterator it;
    private boolean noError = true;

    public FileParser(Output gui, String filename) {

        this.gui = gui; /* Reference to the output screen */
        this.filename = filename; /* File to be processed */
    }

    void readFile() {

        int i = 0;

        try {

            input = new BufferedReader(new FileReader(filename));
            gui.addText("\n\n----- BEGIN FILE READ -----\n\n");

            while (input.ready() && noError) {

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

            input.close();
            gui.addText("\n\n----- END FILE READ -----\n\n");

        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo " + filename);
        }

        gui.addText("Characters read: " + i + "\n");
        gui.addText("Variables: " + varList.size() + "\n\n");

        it = varList.entrySet().iterator();

        while (it.hasNext()) {

            en = (Entry) it.next();
            gui.addText(en.getKey() + ": " + en.getValue() + "\n");
        }
    }

    void parseCharacter(int ch) {

        currentWord.append((char) ch);
        int opCode = st.transition("" + (char) ch);

        if (opCode == 1) {

            name = currentWord.toString().replace('=', ' ').trim();
            currentWord = new StringBuilder();

        } else if (opCode == 5) {

            value = Double.parseDouble(currentWord.toString().replace(';', ' ').trim());
            varList.put(name, value);

            currentWord = new StringBuilder();

        } else if (opCode == 10) {

            ArrayList<String> values = new ArrayList<>();
            values.addAll(Arrays.asList(currentWord.toString().replace(';', ' ').trim().split("[+\\-/*]")));

            ArrayList<String> ops = new ArrayList<>();
            ops.addAll(Arrays.asList(currentWord.toString().replace(';', ' ').trim().split("\\w")));

            /*
            for (int i = 0; i < ops.size(); i++) {

                if (ops.get(i).charAt(0) == '*'){


                }
            }
            */
            currentWord = new StringBuilder();

        } else if (opCode == -1) {

            noError = false;
            gui.addText("\nCurrent Capture: " + currentWord.toString() + '\n');
            gui.addText("INVALID ATTRIBUTION\n\n");
            currentWord = new StringBuilder();
        }
    }
}
