import javax.swing.*;

public class Output {
    public JPanel panel1;
    public JTextArea txtOutput;

    public void run() {
        JFrame frame = new JFrame("Output");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void addText(String text){
        txtOutput.append(text);
    }

    public void addText(char text){
        txtOutput.append("" + text);
    }
}
