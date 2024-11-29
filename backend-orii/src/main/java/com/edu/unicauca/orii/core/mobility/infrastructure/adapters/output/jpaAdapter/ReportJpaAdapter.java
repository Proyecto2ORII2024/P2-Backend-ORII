package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter;

import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;


import com.edu.unicauca.orii.core.mobility.application.ports.output.IExcelReportPersistencePort;
import com.edu.unicauca.orii.core.mobility.domain.model.Form;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.FormEntity;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.mapper.IFormAdapterMapper;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IFormRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReportJpaAdapter implements IExcelReportPersistencePort {

    private final IFormRepository formRepository;
    private final IFormAdapterMapper formAdapterMapper;

    @Override
    public byte[] generateExcelMobility() {
        List<FormEntity> formsEntities = formRepository.findAll();
        List<Form> forms = formAdapterMapper.toForm(formsEntities);

        try (

                InputStream templateStream = getClass().getResourceAsStream("/templates/template-mobility.xlsx");
                Workbook workbook = new XSSFWorkbook(templateStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            // Datos
            int rowIdx = 9;
            for (Form form : forms) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(form.getOrii() ? "SI" : "NO");
                row.createCell(1).setCellValue(form.getCta());
                row.createCell(2).setCellValue(form.getDirection().getDisplayName());
                row.createCell(3).setCellValue(form.getPerson().getPersonType().getDisplayName());
                row.createCell(4).setCellValue(form.getPerson().getIdentificationType().toString());
                row.createCell(5).setCellValue(form.getPerson().getIdentification());
                row.createCell(6).setCellValue(form.getPerson().getFirstName() + " " + form.getPerson().getLastName());
                row.createCell(7).setCellValue(form.getGender().toUpperCase());
                row.createCell(8).setCellValue(form.getExitDate().toString());
                row.createCell(9).setCellValue(form.getEntryDate().toString());
                row.createCell(10).setCellValue(calculateDaysBetween(form.getEntryDate(), form.getExitDate()));
                row.createCell(11).setCellValue(LocalDate.now().getYear());
                row.createCell(12).setCellValue(form.getOrigin().toUpperCase());
                row.createCell(13).setCellValue(form.getDestination().toUpperCase());
                row.createCell(14).setCellValue(form.getAgreement() != null ? "SI" : "NO");
                row.createCell(15)
                        .setCellValue(form.getAgreement() != null ? form.getAgreement().getAgreementNumber() : "");
                row.createCell(16).setCellValue(form.getEvent().getEventType().getName().toUpperCase());
                row.createCell(17).setCellValue(form.getEvent().getDescription());
                row.createCell(18).setCellValue(form.getOriginProgram().toUpperCase());
                row.createCell(19).setCellValue(form.getDestinationProgram().toUpperCase());
                row.createCell(20).setCellValue(form.getCity().toUpperCase());
                row.createCell(21).setCellValue(form.getCountry().toUpperCase());
                row.createCell(22).setCellValue(form.getTeacher().toUpperCase());
                row.createCell(23).setCellValue(form.getFaculty().getDisplayName());
                row.createCell(24).setCellValue("" + form.getFunding());
                row.createCell(25).setCellValue(form.getFundingSource());
            }

            // Escribir en un array de bytes
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el archivo Excel", e);
        }
    }

    public long calculateDaysBetween(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }
        long diffInMillis = Math.abs(date1.getTime() - date2.getTime());

        return diffInMillis / (24 * 60 * 60 * 1000);
    }
}
