package ch.heigvd.statique.commands;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import ch.heigvd.statique.utils.SSHUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "publish", description = "Publish the site")
public class Publish implements Callable<Integer> {

  @Parameters(paramLabel = "Site", description = "The site to publish")
  private Path site;

  @Parameters(paramLabel = "Destination dir", description = "The directory on the host where the site will be published")
  private String destSite;

  @Parameters(paramLabel = "Host", description = "The host where to publish the site")
  private String host;

  @Option(names = "-p", description = "Use password authentication")
  private boolean usePassword;

  @Override
  public Integer call() throws SftpException, JSchException {
    String password = null;
    if (usePassword) {
      // Ask for the password
      throw new UnsupportedOperationException("Password authentication is not supported yet");
    }

    // Connect to the host with SFTP
    ChannelSftp connection = SSHUtils.connectSftp(host, 22, password);

    // Delete the remote directory and then copy the site
    SSHUtils.recursiveFolderDelete(connection, destSite);
    SSHUtils.copy(connection, site.resolve("build"), destSite);

    // Disconnect from the host
    SSHUtils.disconnect(connection);
    return 0;
  }
}
