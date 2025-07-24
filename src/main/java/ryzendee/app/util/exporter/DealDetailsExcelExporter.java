package ryzendee.app.util.exporter;

import org.springframework.stereotype.Component;
import ryzendee.app.dto.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Component
public class DealDetailsExcelExporter implements DealDetailsExporter {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public ExportResult export(Collection<DealDetails> items) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet(getSheetName());
            CellStyle headerStyle = createHeaderStyle(workbook);
            List<Column<DealDetails>> columns = getColumns();

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < columns.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns.get(i).getHeader());
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (DealDetails item : items) {
                Row row = sheet.createRow(rowIdx++);
                for (int col = 0; col < columns.size(); col++) {
                    row.createCell(col).setCellValue(columns.get(col).extractValue(item));
                }
            }

            for (int i = 0; i < columns.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return ExportResult.builder()
                    .filename(getFilename())
                    .content(out.toByteArray())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Excel export failed", e);
        }
    }

    private String getFilename() {
        return "deal_details.xlsx";
    }

    private String getSheetName() {
        return "Deals";
    }

    public List<Column<DealDetails>> getColumns() {
        return List.of(
                new Column<>("ID", deal -> deal.id() != null ? deal.id().toString() : ""),
                new Column<>("Описание", deal -> stringOrEmpty(deal.description())),
                new Column<>("Номер договора", deal -> stringOrEmpty(deal.agreementNumber())),
                new Column<>("Дата договора", deal -> formatDate(deal.agreementDate())),
                new Column<>("Дата начала", deal -> formatDate(deal.agreementStartDate())),
                new Column<>("Дата доступности", deal -> formatDate(deal.availabilityDate())),
                new Column<>("Тип", deal -> getIfPresent(deal.type(), DealTypeDetails::name)),
                new Column<>("Статус", deal -> getIfPresent(deal.status(), DealStatusDetails::name)),
                new Column<>("Сумма", deal -> getIfPresent(deal.sum(), DealSumDetails::valueToPlainString)),
                new Column<>("Валюта", deal -> getIfPresent(deal.sum(), DealSumDetails::currency)),
                new Column<>("Дата закрытия", deal -> formatDateTime(deal.closeDate())),
                new Column<>("Основной контрагент", this::extractMainContractorName)
        );
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private String stringOrEmpty(String value) {
        return value != null ? value : "";
    }

    private String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMAT) : "";
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATETIME_FORMAT) : "";
    }

    private <T> String getIfPresent(T value, Function<T, String> mapper) {
        return value != null ? stringOrEmpty(mapper.apply(value)) : "";
    }

    private String extractMainContractorName(DealDetails deal) {
        if (deal.contractors() == null) {
            return "";
        }

        return deal.contractors().stream()
                .filter(c -> Boolean.TRUE.equals(c.main()))
                .findFirst()
                .map(ContractorDetails::name)
                .orElse("");
    }
}
