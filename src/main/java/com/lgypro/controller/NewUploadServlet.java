package com.lgypro.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload2.core.DiskFileItem;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileUploadException;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletFileUpload;
import org.apache.commons.fileupload2.jakarta.servlet6.JakartaServletRequestContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@WebServlet("/upload2")
public class NewUploadServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uploadPath = getServletContext().getRealPath("")
            + File.separator + "upload";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        if (JakartaServletFileUpload.isMultipartContent(request)) {
            final DiskFileItemFactory fileItemFactory = DiskFileItemFactory.builder().get();
            final JakartaServletFileUpload<DiskFileItem, DiskFileItemFactory> fileUpload = new JakartaServletFileUpload<>(fileItemFactory);
            final List<DiskFileItem> items;
            try {
                items = fileUpload.parseRequest(new JakartaServletRequestContext(request));
            } catch (FileUploadException e) {
                throw new ServletException(e);
            }
            if (items != null && items.size() > 0) {
                for (DiskFileItem item : items) {
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        item.write(Paths.get(filePath));
                        request.setAttribute("message", "File "
                            + fileName + " has uploaded successfully!");
                    }
                }
            }
        }
        response.getWriter().println("upload ok.");
    }
}
