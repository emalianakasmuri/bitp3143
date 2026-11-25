package lab05.kopicorner.solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * PooledStockServer.java (Exercise 5: TODO 5)
 *
 * Program description:
 *   The Kopi Corner store-room server from Lab 04, Exercise 7. Instead of
 *   creating a new thread for every client, it keeps a fixed team of
 *   POOL_SIZE worker threads. The main thread only accepts connections and
 *   hands each one to the pool as a task.
 *
 * What it shows:
 *   ExecutorService, Executors.newFixedThreadPool(), execute() and
 *   shutdown(), and what happens to clients when every worker is busy.
 *
 * How to run:
 *   Run this program first, then CookScreens. Stop it with Terminate.
 */
public class PooledStockServer {

    /** Port number on which the store-room server listens. */
    private static final int PORT = 6200;

    /** Number of worker threads in the pool. */
    private static final int POOL_SIZE = 3;

    /** One store room, shared by every client. */
    private static final StockProtocol STORE = new StockProtocol();

    /**
     * Accepts clients for ever and hands each one to the pool.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        // TODO 5a: create a fixed pool of POOL_SIZE worker threads.
        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            CafeLog.say("KOPI-STOCK/1.0 ready on port " + PORT
                    + (pool == null ? " (one thread per client)" : " (pool of " + POOL_SIZE + " workers)"));
            int clientNo = 0;
            while (true) {
                Socket client = serverSocket.accept();
                clientNo++;
                int no = clientNo;
                CafeLog.say("accepted client " + no);
                // TODO 5b: hand the client to the pool instead of creating a new thread.
                pool.execute(() -> serve(client, no));
            }
        } catch (IOException e) {
            CafeLog.say("could not listen on port " + PORT + ": " + e.getMessage());
        } finally {
            // TODO 5c: shut the pool down.
            pool.shutdown();
        }
    }

    /**
     * Serves one client until it sends QUIT or disconnects.
     * Runs on a worker thread.
     *
     * @param client the socket returned by accept()
     * @param no     the client's number, for the log
     */
    private static void serve(Socket client, int no) {
        CafeLog.say("serving client " + no);
        try (client;
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true, StandardCharsets.UTF_8)) {
            out.println(StockProtocol.GREETING);
            String line;
            while ((line = in.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String reply = STORE.reply(line);
                out.println(reply);
                CafeLog.say("client " + no + ": " + line + "  =>  " + reply.replace("\n", " | "));
                if (reply.startsWith("221")) {
                    break;
                }
            }
        } catch (IOException e) {
            CafeLog.say("client " + no + " problem: " + e.getMessage());
        }
        CafeLog.say("finished with client " + no);
    }
}
