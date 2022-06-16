package ch.heigvd.statique.utils;

import static java.nio.file.StandardWatchEventKinds.*;

import ch.heigvd.statique.convertors.Builder;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** This class watches for change in a given folder. */
public class Watcher implements Runnable {
    /** A map of the watchkey and the related path */
    private static final Map<WatchKey, Path> keyPathMap = new HashMap<>();
    /** The watcher */
    private final WatchService watcher;
    /** The path of the directory to watch */
    private final Path dir;

    /**
     * Watcher constructor
     *
     * @param dir directory to watch
     */
    public Watcher(Path dir) throws IOException {
        watcher = FileSystems.getDefault().newWatchService();
        this.dir = dir;
    }

    /**
     * Registers the folder to watch from :
     * https://www.logicbig.com/tutorials/core-java-tutorial/java-nio/java-watch-service.html
     *
     * @param path directory path
     */
    private void registerDir(Path path) throws IOException {
        if (!Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)
                || path.getFileName().toString().equals("build")) {
            return;
        }

        System.out.println("Adding " + path.getFileName() + " folder to watch");

        WatchKey key =
                path.register(
                        watcher,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_MODIFY,
                        StandardWatchEventKinds.ENTRY_DELETE);
        keyPathMap.put(key, path);

        for (File f : Objects.requireNonNull(path.toFile().listFiles())) {
            registerDir(f.toPath());
        }
    }

    /** Starts watching */
    @Override
    public void run() {
        // from : https://docs.oracle.com/javase/tutorial/essential/io/notification.html
        System.out.println("Watching...");
        try {
            registerDir(dir);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            // Wait for key to be signaled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            if (key == null) continue;

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                // An OVERFLOW event can occur regardless if events are lost or discarded.
                if (kind == OVERFLOW) {
                    continue;
                }

                // The filename is the context of the event.
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path filename = ev.context();

                // Ignores build directory
                if (filename.toString().equals("build")) {
                    continue;
                }

                if (ENTRY_CREATE.equals(kind)) {
                    // this is not a complete path
                    Path path = ev.context();
                    // need to get parent path
                    Path parentPath = keyPathMap.get(key);
                    // get complete path
                    path = parentPath.resolve(path);

                    try {
                        registerDir(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                        continue;
                    }
                    System.out.format("File '%s'" + " added.%n", filename);
                } else if (ENTRY_DELETE.equals(kind)) {
                    System.out.format("File '%s'" + " deleted.%n", filename);
                } else if (ENTRY_MODIFY.equals(kind)) {
                    System.out.format("File '%s'" + " changed.%n", filename);
                }

                // Builds all the site when changes appear
                Builder builder = new Builder(dir, dir.resolve("build"));
                try {
                    builder.build();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Reset the key -- this step is critical if you want to
            // receive further watch events.  If the key is no longer valid,
            // the directory is inaccessible so exit the loop.
            if (!key.reset()) {
                keyPathMap.remove(key);
            }
            if (keyPathMap.isEmpty()) {
                System.err.println("Watcher : key no longer valid.");
                break;
            }
        }
    }
}
