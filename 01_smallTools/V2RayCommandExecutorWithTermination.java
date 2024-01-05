import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class V2RayCommandExecutorWithTermination extends JFrame {
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

    public V2RayCommandExecutorWithTermination() {
        super("V2Ray Commands Executor");

        // 新增 "00" 按钮
        JButton zeroButton = new JButton("00");
        zeroButton.setPreferredSize(new java.awt.Dimension(200, zeroButton.getPreferredSize().height));
        zeroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                terminateProcessOnPort(1080);
            }
        });
        add(zeroButton);

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
            currentProcess = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void terminateProcessOnPort(int port) {
        try {
            int processId = findProcessIdByPort(port);

            if (processId != -1) {
                terminateProcess(processId);
                System.out.println("Process with ID " + processId + " terminated gracefully.");
            } else {
                System.out.println("No process found running on port " + port);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int findProcessIdByPort(int port) throws IOException, InterruptedException {
        String command = String.format("netstat -ano | find \"LISTENING\" | find \":%d\"", port);
        Process process = new ProcessBuilder().command("cmd", "/c", command).start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.trim().split("\\s+");
            if (parts.length >= 5) {
                return Integer.parseInt(parts[4]);
            }
        }

        process.waitFor();
        return -1;
    }

    private void terminateProcess(int processId) throws IOException, InterruptedException {
        String os = System.getProperty("os.name").toLowerCase();
        String command;

        if (os.contains("win")) {
            command = "taskkill /F /PID " + processId;
        } else {
            command = "kill -9 " + processId;
        }

        Process terminateProcess = Runtime.getRuntime().exec(command);
        terminateProcess.waitFor();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new V2RayCommandExecutorWithTermination().setVisible(true);
            }
        });
    }
}
