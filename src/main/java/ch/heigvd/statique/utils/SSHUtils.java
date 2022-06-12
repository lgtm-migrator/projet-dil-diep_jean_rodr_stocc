package ch.heigvd.statique.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Vector;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SSHUtils {
    public static void cleanRemote(ChannelSftp connection, String remotePath) throws SftpException {
        recursiveFolderDelete(connection, remotePath);
        connection.mkdir(remotePath);
    }

    private static void recursiveFolderDelete(ChannelSftp connection, String remotePath) throws SftpException {
        String current = connection.pwd();
        connection.cd(remotePath);
        for (Object file : connection.ls("*")) {
            if (file instanceof ChannelSftp.LsEntry) {
                ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) file;
                if (entry.getAttrs().isDir()) {
                    recursiveFolderDelete(connection, entry.getFilename());
                } else {
                    connection.rm(entry.getFilename());
                }
            }
        }
        connection.cd(current);
        connection.rmdir(remotePath);
    }

    public static void copy(ChannelSftp connection, Path localPath, String remotePath) throws SftpException {
        File localFile = localPath.toFile();

        if (localFile.isDirectory()) {
            try {
                connection.mkdir(remotePath);
            } catch (SftpException e) {
            }

            for (File file : localFile.listFiles()) {
                copy(connection, file.toPath(), remotePath + "/" + file.getName());
            }
        } else {
            connection.put(localPath.toString(), remotePath);
        }
    }

    public static ChannelSftp connectSftp(String host, int port, String password) throws SftpException, JSchException {
        JSch jsch = new JSch();
        String[] hostSplit = host.split("@", 2);

        if (hostSplit.length == 1) {
            hostSplit = new String[] {"root", hostSplit[0]};
        }

        if (password == null) {
            jsch.addIdentity("~/.ssh/id_rsa");
        }
        jsch.setConfig("StrictHostKeyChecking", "no");

        Session jschSession = jsch.getSession(hostSplit[0], hostSplit[1], port);

        if (password != null) {
            jschSession.setPassword(password);
        }

        jschSession.connect();
        Channel channelSftp = jschSession.openChannel("sftp");
        channelSftp.connect();

        return (ChannelSftp) channelSftp;
    }

    public static void disconnect(ChannelSftp connection) throws JSchException {
        connection.disconnect();
        connection.getSession().disconnect();
    }
}