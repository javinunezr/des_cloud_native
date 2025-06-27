package com.example.bdget.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.bdget.entity.Boleta;
import com.example.bdget.entity.Producto;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

@Service
public class PDFService {

    public byte[] generarBoletaPDF(Boleta boleta) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("BOLETA DE COMPRA")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold().setFontSize(18));

            document.add(new Paragraph("Boleta ID: " + boleta.getBoletaId()));
            document.add(new Paragraph("Cliente ID: " + boleta.getCliente().getClienteId()));
            document.add(new Paragraph("Fecha: " + LocalDate.now().toString()));

            Table table = new Table(UnitValue.createPercentArray(new float[]{4, 2}))
                    .useAllAvailableWidth();
            table.addHeaderCell("Producto");
            table.addHeaderCell("Precio");

            for (Producto producto : boleta.getProductos()) {
                table.addCell(producto.getNombre());
                table.addCell("$" + producto.getPrecio());
            }

            document.add(table);
            document.add(new Paragraph("Subtotal: $" + boleta.getSubtotal()));
            document.add(new Paragraph("Total: $" + boleta.getTotal()).setBold());

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF: " + e.getMessage(), e);
        }
    }
}
