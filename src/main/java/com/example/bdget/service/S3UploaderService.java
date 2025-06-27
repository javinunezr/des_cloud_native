package com.example.bdget.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class S3UploaderService {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final S3Client s3Client;

    public S3UploaderService() {
        this.s3Client = S3Client.builder()
            .region(Region.US_EAST_1)
            .build();
    }

    public void subirBoletaPDF(byte[] contenidoPdf, String clienteId, Long boletaId) {
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        String key = String.format("cliente%s/%s/boleta%d.pdf", clienteId, fecha, boletaId);

        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("application/pdf")
                .build(),
            RequestBody.fromBytes(contenidoPdf)
        );
    }

    public byte[] descargarBoletaPDF(String key) {
        try {
            ResponseBytes<?> response = s3Client.getObjectAsBytes(
                GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build()
            );
            return response.asByteArray();
        } catch (S3Exception e) {
            System.err.println("Error al descargar desde S3: " + e.awsErrorDetails().errorMessage());
            return null;
        }
    }

    public boolean eliminarBoletaPDF(String key) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build());
            return true;
        } catch (S3Exception e) {
            System.err.println("Error al eliminar desde S3: " + e.awsErrorDetails().errorMessage());
            return false;
        }
    }
}
