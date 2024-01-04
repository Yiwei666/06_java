import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class V2RayExecutorGUI extends JFrame {
    private static final String[] COMMANDS = {
        "D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\v2ray.exe run -c D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\config_0311_do3-1.json",
        "D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\v2ray.exe run -c D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\config_0311_do3-2.json",
        "D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\v2ray.exe run -c D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\config_0303_do1-1.json",
        "D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\v2ray.exe run -c D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\config_0303_do1-2.json",
        "D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\v2ray.exe run -c D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\config_0331_aws1-2.json",
        "D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\v2ray.exe run -c D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\config_1111_aws1-3.json",
        "D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\v2ray.exe run -c D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\config_0525_azure1-1.json",
        "D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\v2ray.exe run -c D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\config_0526_azure2-1.json",
        "D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\v2ray.exe run -c D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\config_0530_azure5-1.json",
        "D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\v2ray.exe run -c D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\config_0530_azure6-1.json",
        "D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\v2ray.exe run -c D:\\software\\09_v2ray\\v2ray-windows-64-v5.4\\config_1124_cc1-1.json"
    };

    private Process currentProcess;

    public V2RayExecutorGUI() {
        super("V2Ray Commands Executor");

        for (int i = 0; i < COMMANDS.length; i++) {
            JButton button = new JButton(Integer.toString(i + 1));
            int finalI = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    executeV2RayCommand(COMMANDS[finalI]);
                }
            });
            add(button);
        }

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(250, 400);
        setLocationRelativeTo(null);
    }

    private void executeV2RayCommand(String command) {
        if (currentProcess != null) {
            currentProcess.destroy();
            try {
                currentProcess.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            // 将命令和参数分解，并传递给 ProcessBuilder
            String[] commandArray = command.split(" ");
            ProcessBuilder processBuilder = new ProcessBuilder(commandArray);
            currentProcess = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new V2RayExecutorGUI().setVisible(true);
            }
        });
    }
}
