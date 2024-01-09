import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class V2RayCommandExecutorWithTerminationTXT extends JFrame {
    private static final String COMMANDS_FILE_PATH = "v2rayCommands.txt";
    private List<String> commandsList;
    private Process currentProcess;

    public V2RayCommandExecutorWithTerminationTXT() {
        super("V2Ray Commands Executor");

        readCommandsFromFile();

        JButton zeroButton = new JButton("00");
        zeroButton.setPreferredSize(new java.awt.Dimension(200, zeroButton.getPreferredSize().height));
        zeroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                terminateProcessOnPort(1080);
            }
        });
        add(zeroButton);

        for (int i = 0; i < commandsList.size(); i++) {
            JButton button = new JButton(String.format("%02d", i + 1));
            int finalI = i;

            button.setPreferredSize(new java.awt.Dimension(200, button.getPreferredSize().height));

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    executeV2RayCommand(commandsList.get(finalI));
                }
            });
            add(button);
        }

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
    }


    private void readCommandsFromFile() {
        commandsList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COMMANDS_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 移除命令周围的引号和额外的空格
                line = line.trim();
                if (!line.isEmpty()) {
                    commandsList.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // 打印读取的命令列表到终端
        System.out.println("Read commands from file:");
        for (String command : commandsList) {
            System.out.println(command);
        }
    }
    
   
    private void executeV2RayCommand(String command) {
        if (currentProcess != null) {
            System.out.println("当前进程ID: " + currentProcess.pid());
            currentProcess.destroy();
            try {
                currentProcess.waitFor();
                System.out.println("先前进程已终止。");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    
        System.out.println("执行命令: " + command);
    
        new Thread(() -> {
            try {
                String[] commandArray = command.split("\\s+", -1);
                System.out.println("拆分后的命令数组: ");
                for (String part : commandArray) {
                    System.out.println(part);
                }                
                ProcessBuilder processBuilder = new ProcessBuilder(commandArray);
                processBuilder.inheritIO(); // 将子进程的输出和错误流传递给当前进程
                currentProcess = processBuilder.start();
    
                System.out.println("新进程ID: " + currentProcess.pid());
    
                int exitCode = currentProcess.waitFor();
                System.out.println("进程结束，退出码: " + exitCode);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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

        ProcessBuilder processBuilder;
        if (os.contains("win")) {
            processBuilder = new ProcessBuilder("cmd", "/c", command);
        } else {
            processBuilder = new ProcessBuilder("sh", "-c", command);
        }

        Process terminateProcess = processBuilder.start();
        terminateProcess.waitFor();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new V2RayCommandExecutorWithTerminationTXT().setVisible(true);
            }
        });
    }
}
