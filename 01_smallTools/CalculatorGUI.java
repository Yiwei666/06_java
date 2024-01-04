import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CalculatorGUI extends JFrame {
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

    private Process currentProcess;  // Track the current cmd process

    public CalculatorGUI() {
        super("V2Ray Commands Executor");

        // 创建按钮并设置事件监听器
        for (int i = 0; i < COMMANDS.length; i++) {
            JButton button = new JButton(Integer.toString(i + 1));
            int finalI = i;  // to make it effectively final
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    executeCommandInCmd(COMMANDS[finalI]);
                }
            });
            add(button);
        }

        // 设置布局管理器
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // 设置窗口属性
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(250, 400);
        setLocationRelativeTo(null);
    }

    private void executeCommandInCmd(String command) {
        // 如果有上一个进程，先终止它
        if (currentProcess != null) {
            currentProcess.destroy();
            try {
                // 等待上一个进程终止
                currentProcess.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            // 使用ProcessBuilder启动新的cmd进程
            ProcessBuilder processBuilder = new ProcessBuilder("cmd", "/c", "start", "cmd.exe", "/K", command);
            currentProcess = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CalculatorGUI().setVisible(true);
            }
        });
    }
}
