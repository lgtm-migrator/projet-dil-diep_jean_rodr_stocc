package ch.heigvd.statique.commands;

import ch.heigvd.statique.utils.SSHUtils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "publish", description = "Publish the site")
public class Publish implements Callable<Integer> {

    @Parameters(paramLabel = "Site", description = "The site to publish")
    private Path site;

    @Parameters(
            paramLabel = "Destination dir",
            description = "The directory on the host where the site will be published")
    private String destSite;

    @Parameters(paramLabel = "Host", description = "The host where to publish the site")
    private String host;

    @Option(
            names = {"-p", "--password"},
            description = "Password",
            interactive = true)
    private char[] password;

    @Override
    public Integer call() throws SftpException, JSchException {
        String str_password = null;
        if (password != null) {
            str_password = new String(password);
        }

        // Connect to the host with SFTP
        System.out.println("Connecting to " + host);
        ChannelSftp connection;
        try {
            connection = SSHUtils.connectSftp(host, 22, str_password);
        } catch (JSchException e) {
            if (e.getMessage().contains("Auth fail")) {
                System.out.println("Authentication failed");
            } else {
                System.out.println("Connection failed");
            }
            return 1;
        }

        // Delete the remote directory and then copy the site
        System.out.println("Cleaning the remote directory");
        SSHUtils.recursiveFolderDelete(connection, destSite);
        System.out.println("Copying the site");
        SSHUtils.copy(connection, site.resolve("build"), destSite);

        // Disconnect from the host
        System.out.println("Closing the connection");
        SSHUtils.disconnect(connection);
        return 0;
    }
}
