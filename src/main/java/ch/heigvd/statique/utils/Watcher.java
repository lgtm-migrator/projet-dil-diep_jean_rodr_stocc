package ch.heigvd.statique.utils;

import ch.heigvd.statique.convertors.Builder;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class Watcher {
    private final WatchService watcher;
    private final Path site;

    public Watcher(Path site) throws IOException {
        watcher = FileSystems.getDefault().newWatchService();
        this.site = site;

        // TODO: Check if needed
        site.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        watch();
    }

    public void watch() throws IOException {
        // from : https://docs.oracle.com/javase/tutorial/essential/io/notification.html
        while(true) {
            // Wait for key to be signaled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                // An OVERFLOW event can occur regardless if events are lost or discarded.
                if (kind == OVERFLOW) {
                    continue;
                }

                // The filename is the context of the event.
                WatchEvent<Path> ev = (WatchEvent<Path>)event;
                Path filename = ev.context();

                // Verify that the new file is a text file.
                // Resolve the filename against the directory.
                // If the filename is "test" and the directory is "foo",
                // the resolved name is "test/foo".
                Path child = site.resolve(filename);
                if (!Files.probeContentType(child).equals("text/plain")) {
                    System.err.format("File '%s'" +
                            " is not a plain text file.%n", filename);
                    continue;
                }

                // Builds all the site when changes appear
                Builder builder = new Builder(site, site.resolve("build"));
                builder.build();

                // TODO: Check what to do with config files
                // TODO: Build only a file
            }

            // Reset the key -- this step is critical if you want to
            // receive further watch events.  If the key is no longer valid,
            // the directory is inaccessible so exit the loop.
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
}
