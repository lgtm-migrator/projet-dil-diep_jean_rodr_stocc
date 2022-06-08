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

  @Parameters(paramLabel = "SITE", description = "The site to publish")
  public Path site;

  @Parameters(paramLabel = "Destination dir", description = "The directory on the host where the site will be published")
  public String destSite;

  @Parameters(paramLabel = "Host", description = "The host where to publish the site")
  public String host;

  @Option(names = "-p", description = "Use password authentication")
  public boolean usePassword;

  @Override
  public Integer call() throws SftpException, JSchException {
    ChannelSftp connection = null;
    if (usePassword) {
      throw new UnsupportedOperationException("Password authentication is not supported yet");
    } else {
      connection = SSHUtils.connectSftp(host, 22, null);
    }

    SSHUtils.cleanRemote(connection, destSite);
    SSHUtils.copy(connection, site.resolve("build").toString(), destSite);
    SSHUtils.disconnect(connection);
    return 0;
  }
}
