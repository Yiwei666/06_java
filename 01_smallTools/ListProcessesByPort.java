import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ListProcessesByPort {
    public static void main(String[] args) {
        // 指定要查询的端口号
        int port = 1080;

        // 构建查询进程的命令
        String command = String.format("netstat -ano | find \"LISTENING\" | find \":%d\"", port);

        try {
            // 执行命令
            Process process = new ProcessBuilder().command("cmd", "/c", command).start();

            // 获取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                // 提取进程ID
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 5) {
                    String processId = parts[4];
                    // 获取服务名称
                    String serviceName = getServiceName(processId);
                    System.out.println("Process ID: " + processId + ", Service Name: " + serviceName);
                    found = true;
                }
            }

            // 等待命令执行完成
            int exitCode = process.waitFor();

            if (!found) {
                System.out.println("No process found running on port " + port);
            }

            System.out.println("Command executed with exit code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String getServiceName(String processId) {
        String command = String.format("tasklist /fi \"pid eq %s\"", processId);
        try {
            Process process = new ProcessBuilder().command("cmd", "/c", command).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Services")) {
                    // 提取服务名称
                    return line.trim().split("\\s+")[1];
                }
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "N/A";
    }
}
