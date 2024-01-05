import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.File;

public class V2RayExecutorWithOutputGUI extends JFrame {
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

    public V2RayExecutorWithOutputGUI() {
        super("V2Ray Commands Executor");

        for (int i = 0; i < COMMANDS.length; i++) {
            JButton button = new JButton(String.format("%02d", i + 1));
            int finalI = i;

            button.setPreferredSize(new java.awt.Dimension(200, button.getPreferredSize().height));

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
        pack();
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
            String[] commandArray = command.split(" ");
            ProcessBuilder processBuilder = new ProcessBuilder(commandArray);

            // Redirect standard output and error to files
            processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(new File("v2ray_output.txt")));
            processBuilder.redirectError(ProcessBuilder.Redirect.appendTo(new File("v2ray_error.txt")));

            currentProcess = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new V2RayExecutorWithOutputGUI().setVisible(true);
            }
        });
    }
}
