package com.inventa.inventory.model;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.util.List;
import java.util.stream.Stream;
import java.awt.Color;
import java.io.OutputStream;
import java.time.LocalDate;

public class PdfExporter {
    public static void export(List<Transaction> transactions, LocalDate fromDate, LocalDate toDate, String transactionStatus, OutputStream outputStream) {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font fontTitle = FontFactory.getFont(FontFactory.TIMES_BOLD, 16);
            Font fontTitle2 = FontFactory.getFont(FontFactory.TIMES_BOLD, 10);
            Font fontParagraph = FontFactory.getFont(FontFactory.TIMES, 8);

            // main heading
            Paragraph title = new Paragraph("Transaction Report", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph());

            // from
            Paragraph from = fromDate == null ? new Paragraph("From: -", fontParagraph) : new Paragraph("From: " + fromDate, fontParagraph);
            from.setAlignment(Element.ALIGN_LEFT);
            document.add(from);
            // to
            Paragraph to = toDate == null ? new Paragraph("To: -", fontParagraph) : new Paragraph("To: " + toDate, fontParagraph);
            to.setAlignment(Element.ALIGN_LEFT);
            document.add(to);
            // status
            Paragraph status = transactionStatus == "" ? new Paragraph("Status: -", fontParagraph) : new Paragraph("Status: " + transactionStatus, fontParagraph);
            status.setAlignment(Element.ALIGN_LEFT);
            document.add(status);

            document.add(new Paragraph());

            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            addTableHeader(table, fontTitle2);
            addRows(table, transactions, fontParagraph);

            document.add(table);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    public static void addTableHeader(PdfPTable table, Font fontTitle2) {
        Stream.of("No", "Image", "Name", "Category", "Qty", "Fee", "Condition", "Status", "Event Date")
              .forEach(col -> {
                table.addCell(makeHeaderCell(col, fontTitle2));
              });
    }

    public static void addRows(PdfPTable table, List<Transaction> transactions, Font fontParagraph) {
        Integer no = 1;
        for(Transaction transaction : transactions) {
            table.addCell(makeTextCell(no.toString(), fontParagraph));

            try {
                Image img = Image.getInstance("uploads/" + transaction.getImage());
                img.scaleToFit(35, 35);
                table.addCell(makeImageCell(img));
            } catch(Exception e) {
                table.addCell(makeTextCell("-", fontParagraph));
            }

            table.addCell(makeTextCell(transaction.getName(), fontParagraph));
            table.addCell(makeTextCell(transaction.getCategory(), fontParagraph));
            table.addCell(makeTextCell(transaction.getQty().toString(), fontParagraph));
            table.addCell(makeTextCell("Rp" + transaction.getFee().toString(), fontParagraph));
            table.addCell(makeTextCell(transaction.getCondition(), fontParagraph));
            table.addCell(makeTextCell(transaction.getStatus(), fontParagraph));
            table.addCell(makeTextCell(transaction.getDate().toString(), fontParagraph));

            no++;
        }
    }

    private static PdfPCell makeHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setFixedHeight(20);
        cell.setBackgroundColor(new Color(99, 102, 241));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        return cell;
    }

    private static PdfPCell makeTextCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setMinimumHeight(40);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        return cell;
    }

    private static PdfPCell makeImageCell(Image image) {
        PdfPCell cell = new PdfPCell(image);
        cell.setMinimumHeight(40);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        return cell;
    }
}
