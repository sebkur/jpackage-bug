import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class App {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Please specify a command to run and optional arguments");
            System.exit(1);
        }

        // Forward all arguments to the process builder
        Process process = new ProcessBuilder(args).redirectErrorStream(true).start();

        // Start a new thread to forward the launched thread's output to our own stdout
        Thread gobbler = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[stdout] " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gobbler.start();
    }

}
