
import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{
    static String ip = "localhost";

    static Socket socket;
    static ObjectOutputStream oos;
    static ObjectInputStream ois;

    static JFrame frame = new JFrame("CLIENT");
    static JTextField number = new JTextField(20);
    static JButton ok = new JButton("GET PERSONS");
    static JLabel status = new JLabel("STATUS:");

    static JPanel results = new JPanel();

    public static void main(String[] args) throws IOException
    {
        init();
    }

    public static void init() throws IOException
    {
        socket = new Socket(ip, 7777);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());

        frame.setSize(500, 500);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        results.setLayout(new BoxLayout(results, BoxLayout.Y_AXIS));

        frame.add(status);
        frame.add(number);
        frame.add(ok);
        frame.add(results);



        ok.addActionListener(e ->
        {
            try
            {
                ok();
            } catch (IOException ex)
            {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex)
            {
                ex.printStackTrace();
            }
        });


        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void ok() throws IOException, ClassNotFoundException
    {
        results.removeAll();

        String str = number.getText();

        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(str);

        if(matcher.matches())
        {
            if(str.length() > 6)
            {
                str = str.substring(0, 5);
            }

            Integer number = Integer.parseInt(str);
            ArrayList<String> person_list;

            oos.writeObject(number);

            person_list = (ArrayList<String>) ois.readObject();

            for(String person : person_list)
            {
                results.add(new JLabel(person));
            }

            status.setText(person_list.size() + " PERSONS ADDED!");
        }
        else
        {
            status.setText("INCORRECT NUMBER ENTERED!");
        }
    }
}
