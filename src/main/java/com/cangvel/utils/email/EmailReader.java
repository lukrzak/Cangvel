package com.cangvel.utils.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Component
public class EmailReader {

    @Value("${application.mail.password}")
    private String password;
    @Value("${application.mail.username}")
    private String username;

    public List<File> getAttachments() throws MessagingException, IOException {
        getMultipartEmailMessages();
        return null;
    }

    private Message[] getMultipartEmailMessages() {
        Properties props = getEmailProperties();
        Session session = Session.getDefaultInstance(props);
        try (Store store = session.getStore("imap")) {
            store.connect(username, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.getMessages();

            //Message[] messages = getMultipartEmailMessages();
            for (Message message : messages)
                if (message.isMimeType("multipart/*")) {
                    Multipart multipart = (Multipart) message.getContent();
                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart bp = multipart.getBodyPart(i);
                        if (Part.ATTACHMENT.equalsIgnoreCase(bp.getDisposition())) {
                            String fileName = bp.getFileName();
                            System.out.println("jest pdf");
                            File pdfFile = convertPdfToFileType(bp.getInputStream(), fileName);
                        }
                    }
                }

            inbox.close();
            return null;

            //return messages;

            //return fetchMultipartMessages(Arrays.stream(messages).toList());
        } catch (MessagingException e) {
            throw new RuntimeException("Cannot read emails");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Message> fetchMultipartMessages(List<Message> messages) {
        return messages.stream()
                .filter(m -> {
                    try {
                        return m.getContentType().contains("multipart");
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .toList();
    }

    private Properties getEmailProperties() {
        Properties props = new Properties();
        props.setProperty("mail.imap.ssl.enable", "true");
        props.setProperty("mail.imap.host", "imap.gmail.com");
        props.setProperty("mail.imap.port", "993");

        return props;
    }

    private File convertPdfToFileType(InputStream is, String fileName) {
        try {
            File tempFile = File.createTempFile(fileName, ".pdf");
            // Write the attachment content to the temporary file
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) fos.write(buffer, 0, bytesRead);
            }
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
