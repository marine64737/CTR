package com.shkim.CTR;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ExcelController {
    @Autowired
    WebClientServiceImpl webClientService;

    @GetMapping("/download")
    public void download(HttpServletResponse res) throws Exception {
        /**
         * excel sheet 생성
         */
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1"); // 엑셀 sheet 이름
        sheet.setDefaultColumnWidth(28); // 디폴트 너비 설정

        /**
         * header font style
         */
        XSSFFont headerXSSFFont = (XSSFFont) workbook.createFont();
        headerXSSFFont.setColor(new XSSFColor(new byte[]{(byte) 255, (byte) 255, (byte) 255}));

        /**
         * header cell style
         */
        XSSFCellStyle headerXssfCellStyle = (XSSFCellStyle) workbook.createCellStyle();

        // 테두리 설정
        headerXssfCellStyle.setBorderLeft(BorderStyle.THIN);
        headerXssfCellStyle.setBorderRight(BorderStyle.THIN);
        headerXssfCellStyle.setBorderTop(BorderStyle.THIN);
        headerXssfCellStyle.setBorderBottom(BorderStyle.THIN);

        // 배경 설정
        headerXssfCellStyle.setFillForegroundColor(new XSSFColor(new byte[]{(byte) 34, (byte) 37, (byte) 41}));
        headerXssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerXssfCellStyle.setFont(headerXSSFFont);

        /**
         * body cell style
         */
        XSSFCellStyle bodyXssfCellStyle = (XSSFCellStyle) workbook.createCellStyle();

        // 테두리 설정
        bodyXssfCellStyle.setBorderLeft(BorderStyle.THIN);
        bodyXssfCellStyle.setBorderRight(BorderStyle.THIN);
        bodyXssfCellStyle.setBorderTop(BorderStyle.THIN);
        bodyXssfCellStyle.setBorderBottom(BorderStyle.THIN);

        /**
         * header data
         */
        int rowCount = 0; // 데이터가 저장될 행
        List<Problem> tmp = webClientService.get(0);
        Field[] list = tmp.get(0).getClass().getDeclaredFields();
        List<String> headerNames = //new String[]{"ID", "Title", "level"};
                new ArrayList<>();
        for (Field f : list) {
            String[] ftemp = f.toString().split("\\.");
            headerNames.add(ftemp[ftemp.length-1]);
        }

        Row headerRow = null;
        Cell headerCell = null;

        headerRow = sheet.createRow(rowCount++);
        for (int i = 0; i < headerNames.size(); i++) {
            headerCell = headerRow.createCell(i);
            headerCell.setCellValue(headerNames.get(i)); // 데이터 추가
            headerCell.setCellStyle(headerXssfCellStyle); // 스타일 추가
        }

        /**
         * body data
         */
        //List<String[]> bodyDatass = new ArrayList<>();
//        String[][] bodyDatass = new String[][]{
//                {"첫번째 행 첫번째 데이터", "첫번째 행 두번째 데이터", "첫번째 행 세번째 데이터"},
//                {"두번째 행 첫번째 데이터", "두번째 행 두번째 데이터", "두번째 행 세번째 데이터"},
//                {"세번째 행 첫번째 데이터", "세번째 행 두번째 데이터", "세번째 행 세번째 데이터"},
//                {"네번째 행 첫번째 데이터", "네번째 행 두번째 데이터", "네번째 행 세번째 데이터"}
//        };
//        for (int i=1000; i<=1100; i++) {
//            //bodyDatass.put(i, webClientService.get(i));
//            String[] api = webClientService.get(i);
//            bodyDatass.add(new String[]{String.valueOf(i), api[0], api[1]});
//        }
        for (int t=0; t<340; t++){
            List<Problem> apis = webClientService.get(t);

            Row bodyRow = null;
            Cell bodyCell = null;

            for(Problem api : apis) {
                bodyRow = sheet.createRow(rowCount++);

                for(int i=0; i<headerNames.size(); i++) {
                    bodyCell = bodyRow.createCell(i);
                    switch (i){
                        case 0: bodyCell.setCellValue(api.problemId()); break;
                        case 1: bodyCell.setCellValue(api.titleKo()); break;
                        case 2: bodyCell.setCellValue(0); break;
                        case 3: bodyCell.setCellValue(api.isSolvable()); break;
                        case 4: bodyCell.setCellValue(api.isPartial()); break;
                        case 5: bodyCell.setCellValue(api.acceptedUserCount()); break;
                        case 6: bodyCell.setCellValue(api.level()); break;
                        case 7: bodyCell.setCellValue(api.votedUserCount()); break;
                        case 8: bodyCell.setCellValue(api.sprout()); break;
                        case 9: bodyCell.setCellValue(api.givesNoRating()); break;
                        case 10: bodyCell.setCellValue(api.isLevelLocked()); break;
                        case 11: bodyCell.setCellValue(api.averageTries()); break;
                        case 12: bodyCell.setCellValue(api.official()); break;
                        default: bodyCell.setCellValue(0); break;
                    }
                    bodyCell.setCellStyle(bodyXssfCellStyle); // 스타일 추가
                }
            }
        }


        /**
         * download
         */
        String fileName = "spring_excel_download";

        res.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        res.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream servletOutputStream = res.getOutputStream();

        workbook.write(servletOutputStream);
        workbook.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }
}
