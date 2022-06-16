package ch.heigvd.statique.utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.nio.file.Path;

/** This class makes possible to connect to a remote host using ssh. */
public class SSHUtils {

    /**
     * Delete a folder and all its subdirectories
     *
     * @param connection The connection to the remote host
     * @param remotePath The remote path to delete
     * @throws SftpException If an error occurs while deleting the remote directory
     */
    public static void recursiveFolderDelete(ChannelSftp connection, String remotePath)
            throws SftpException {
        // Save the current working directory to go back to it after the
        // deletion
        String current = connection.pwd();

        // Change to the remote directory
        try {
            connection.cd(remotePath);
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                // The directory does not exist, so we can return
                return;
            } else {
                throw e;
            }
        }

        // Go through all the files and subdirectories in the remote directory
        for (Object file : connection.ls("*")) {
            if (file instanceof ChannelSftp.LsEntry) {
                ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) file;

                // If the entry is a directory, go through it recursively
                if (entry.getAttrs().isDir()) {
                    recursiveFolderDelete(connection, entry.getFilename());
                } else {
                    // Delete the file
                    connection.rm(entry.getFilename());
                }
            }
        }

        // Go back to the previous working directory and remove the remote
        // directory
        connection.cd(current);
        connection.rmdir(remotePath);
    }

    /**
     * Copy a folder and all its subdirectories to a remote directory
     *
     * @param connection The connection to the remote host
     * @param localPath The local path to copy
     * @param remotePath The remote path where to copy
     * @throws SftpException If an error occurs while copying the local directory to the remote
     *     directory
     */
    public static void copy(ChannelSftp connection, Path localPath, String remotePath)
            throws SftpException {
        File localFile = localPath.toFile();

        // If the local path is a directory, go through it recursively
        if (localFile.isDirectory()) {
            try {
                // Create the remote directory
                connection.mkdir(remotePath);
            } catch (SftpException e) {
                // If the directory already exists, we can ignore the error
            }

            // Go through all the files and subdirectories in the local
            // directory
            for (File file : localFile.listFiles()) {
                copy(connection, file.toPath(), remotePath + "/" + file.getName());
            }
        } else {
            // Copy the file
            connection.put(localPath.toString(), remotePath);
        }
    }

    /**
     * Connect to a remote host using SSH and return a channel for SFTP
     *
     * @param host The host to connect to, the format is the same as for ssh so user@host, if no
     *     user is specified, the default user is root
     * @param port The port to connect to
     * @param password The password to use, if null then key-based authentication is used
     * @return A channel for SFTP
     */
    public static ChannelSftp connectSftp(String host, int port, String password)
            throws SftpException, JSchException {
        JSch jsch = new JSch();

        // Separate the user and host
        String[] hostSplit = host.split("@", 2);
        if (hostSplit.length == 1) {
            hostSplit = new String[] {"root", hostSplit[0]};
        }

        // Set key-based authentication if no password is specified
        if (password == null) {
            jsch.addIdentity("~/.ssh/id_rsa");
        }

        // Avoid the host key checking
        JSch.setConfig("StrictHostKeyChecking", "no");

        // Create the session
        Session jschSession = jsch.getSession(hostSplit[0], hostSplit[1], port);

        // Set the password if it is specified
        if (password != null) {
            jschSession.setPassword(password);
        }

        // Connect to the remote host
        jschSession.connect();

        // Create and connect the SFTP channel
        Channel channelSftp = jschSession.openChannel("sftp");
        channelSftp.connect();

        return (ChannelSftp) channelSftp;
    }

    /**
     * Disconnect from a remote host
     *
     * @param connection The connection to the remote host
     * @throws JSchException If an error occurs while disconnecting from the remote
     */
    public static void disconnect(ChannelSftp connection) throws JSchException {
        connection.disconnect();
        connection.getSession().disconnect();
    }
}
