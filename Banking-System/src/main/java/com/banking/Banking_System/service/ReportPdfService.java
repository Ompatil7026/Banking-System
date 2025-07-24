package com.banking.Banking_System.service;

import com.banking.Banking_System.entities.AuditLog;
import com.lowagie.text.*;
import java.util.List;
import java.awt.Color;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Service
public class ReportPdfService {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuditLogService auditLogService;

    public void generateBankingReportPdf(HttpServletResponse response) throws IOException, DocumentException {
        // Set response headers for PDF download
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=bank_report.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        addReportContent(document);

        document.close();
    }

    // Your given method here (private)
    private void addReportContent(Document document) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font subFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        // Title
        Paragraph title = new Paragraph("Banking System Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // empty line

        // Example summary (fetch counts and amounts from services)
        document.add(new Paragraph("Summary:", subFont));
        document.add(new Paragraph("Total Users: " + userService.countUsers(), normalFont));
        document.add(new Paragraph("Active Accounts: " + accountService.countByStatus("ACTIVE"), normalFont));
        document.add(new Paragraph("Pending Accounts: " + accountService.countByStatus("PENDING"), normalFont));
        document.add(new Paragraph("Total Balance Held: $" + accountService.getTotalBalance(), normalFont));

        document.add(new Paragraph(" ")); // empty line

        // Add recent Audit Logs Table
        document.add(new Paragraph("Recent Audit Logs:", subFont));
        PdfPTable table = new PdfPTable(4); // 4 columns: Date, User, Action, IP
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        // Table headers
        Stream.of("Date & Time", "User", "Action", "IP Address")
                .forEach(header -> {
                    PdfPCell headerCell = new PdfPCell(new Phrase(header, subFont));
                    headerCell.setBackgroundColor(Color.LIGHT_GRAY);
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(headerCell);
                });

        List<AuditLog> logs = auditLogService.getRecentLogs(20);

        for (AuditLog log : logs) {
            table.addCell(new Phrase(log.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), normalFont));
            table.addCell(new Phrase(log.getUser().getUsername(), normalFont));
            table.addCell(new Phrase(log.getAction(), normalFont));
            table.addCell(new Phrase(log.getIp(), normalFont));
        }

        document.add(table);

        // Add more sections if needed...
    }
}
