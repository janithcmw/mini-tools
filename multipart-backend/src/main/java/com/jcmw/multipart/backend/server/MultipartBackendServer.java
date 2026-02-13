package com.jcmw.multipart.backend.server;

    import org.apache.http.entity.ContentType;
    import org.apache.http.entity.mime.MultipartEntityBuilder;
    import org.apache.http.HttpEntity;
    import org.apache.http.entity.mime.HttpMultipartMode;

    import jakarta.servlet.http.HttpServlet;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import java.io.*;
    import java.rmi.RemoteException;

    public class MultipartBackendServer extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

            /* Build multipart automatically */
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            // PART 1 — JSON
            builder.addTextBody(
                    "metadata",
                    "{\"message\":\"Multipart from Java Backend\"}",
                    ContentType.APPLICATION_JSON
            );

            // PART 2 — TEXT FILE
            InputStream textStream =
                    getClass().getClassLoader().getResourceAsStream("text.txt");

            if (textStream == null) {
                throw new RuntimeException("Missing TEXT part in resources directory of the project.");
            } else {
                builder.addBinaryBody(
                        "notes",
                        textStream,
                        ContentType.TEXT_PLAIN,
                        "text.txt"
                );

            }

            // PART 3 — BINARY FILE (PDF)
            InputStream pdfStream =
                    getClass().getClassLoader().getResourceAsStream("pdf.pdf");

            if (pdfStream == null) {
                throw new RemoteException("Missing PDF part in resources directory of the project.");
            }

            builder.addBinaryBody(
                    "file",
                    pdfStream,
                    ContentType.APPLICATION_OCTET_STREAM,
                    "pdf.pdf"
            );


            HttpEntity entity = builder.build();

            /* IMPORTANT: set correct content-type WITH AUTO BOUNDARY */
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setHeader("Content-Type", entity.getContentType().getValue());
            resp.setContentLengthLong(entity.getContentLength());

            OutputStream os = resp.getOutputStream();
            entity.writeTo(os);
            os.flush();
        }
    }
