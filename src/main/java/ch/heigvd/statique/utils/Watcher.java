package ch.heigvd.statique.utils;

import ch.heigvd.statique.convertors.Builder;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class Watcher implements Runnable{
    private final WatchService watcher;
    private final Path dir;

    public Watcher(Path dir) throws IOException {
        watcher = FileSystems.getDefault().newWatchService();
        this.dir = dir;

        dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
    }

    @Override
    public void run() {
        // from : https://docs.oracle.com/javase/tutorial/essential/io/notification.html
        System.out.println("Watching...");
        while(true) {
            // Wait for key to be signaled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            if(key == null)
                continue;

            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                // An OVERFLOW event can occur regardless if events are lost or discarded.
                if (kind == OVERFLOW) {
                    continue;
                }


                // The filename is the context of the event.
                WatchEvent<Path> ev = (WatchEvent<Path>)event;
                Path filename = ev.context();

                // Ignores build directory
                if(filename.toString().equals("build")){
                    continue;
                }


                if (ENTRY_CREATE.equals(kind)) {
                    System.out.format("File '%s'" + " added.%n", filename);
                } else if (ENTRY_DELETE.equals(kind)) {
                    System.out.format("File '%s'" + " deleted.%n", filename);
                } else if (ENTRY_MODIFY.equals(kind)) {
                    System.out.format("File '%s'" + " changed.%n", filename);
                }

                // Verify that the new file is a text file.
                // Resolve the filename against the directory.
                // If the filename is "test" and the directory is "foo",
                // the resolved name is "test/foo".
                /*
                Path child = dir.resolve(filename);
                if (!Files.probeContentType(child).equals("text/plain")) {
                    System.err.format("File '%s'" +
                            " is not a plain text file.%n", filename);
                    continue;
                }
                */

                // Builds all the site when changes appear
                Builder builder = new Builder(dir, dir.resolve("build"));
                try {
                    builder.build();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                System.out.println("Site built");

                // TODO: Check what to do with config files
                // TODO: Build only a file
            }

            // Reset the key -- this step is critical if you want to
            // receive further watch events.  If the key is no longer valid,
            // the directory is inaccessible so exit the loop.
            boolean valid = key.reset();
            if (!valid) {
                System.err.println("Watcher : key no longer valid.");
                break;
            }
        }
    }
}
