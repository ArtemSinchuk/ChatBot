    package chatbot;

    import java.io.IOException;
    import java.net.InetSocketAddress;
    import java.net.Socket;

    public class NetworkUtil {
        /**
         * Checks if the internet connection is available.
         * @return true if connection is successful, false otherwise.
         */
        public static boolean isInternetAvailable() {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress("8.8.8.8", 53), 2000);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
