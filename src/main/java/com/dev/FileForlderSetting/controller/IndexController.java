package com.dev.FileForlderSetting.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.FileForlderSetting.model.AIStamp;
import com.dev.FileForlderSetting.repository.AIStampRepository;
import com.dev.FileForlderSetting.service.ModifiedDate;

@Controller
public class IndexController {

	@Autowired
	AIStampRepository aiRepository;

	@RequestMapping({ "/index", "" })
	public String index(HttpServletResponse res) throws IOException {

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1"); // 엑셀 sheet 이름
		sheet.setDefaultColumnWidth(28); // 디폴트 너비 설정

		XSSFFont headerXSSFFont = (XSSFFont) workbook.createFont();
		headerXSSFFont.setColor(new XSSFColor(new byte[] { (byte) 255, (byte) 255, (byte) 255 }));

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
		headerXssfCellStyle.setFillForegroundColor(new XSSFColor(new byte[] { (byte) 34, (byte) 37, (byte) 41 }));
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

		int rowCount = 0; // 데이터가 저장될 행
		String headerNames[] = new String[] { "path", "fileName" };

		Row headerRow = null;
		Cell headerCell = null;

		headerRow = sheet.createRow(rowCount++);
		for (int i = 0; i < headerNames.length; i++) {
			headerCell = headerRow.createCell(i);
			headerCell.setCellValue(headerNames[i]); // 데이터 추가
			headerCell.setCellStyle(headerXssfCellStyle); // 스타일 추가
		}
		/**
		 * body data
		 */

		List<AIStamp> list = aiRepository.findAllByOrderByIdAsc();
		String bodyDatass[][] = new String[list.size() * 20][2];
		int idx = 0;
		int imageIdx = 0;
		for (int a = 0; a < list.size(); a++) {
			for (int x = idx; x < idx + 20; x++) {
				bodyDatass[x][0] = list.get(a).getAnimal() + "_" + list.get(a).getAction() + "_"
						+ list.get(a).getPoint() + "_" + list.get(a).getEnv() + "_" + list.get(a).getStyle() + "_"
						+ list.get(a).getPrompt() + "_" + imageIdx + ".jpg";
				bodyDatass[x][1] = "" + (a + 1);
				imageIdx++;
				if (imageIdx == 20) {
					imageIdx = 0;
				}
			}
			idx = idx + 20;
		}

		Row bodyRow = null;
		Cell bodyCell = null;

		for (String[] bodyDatas : bodyDatass) {
			bodyRow = sheet.createRow(rowCount++);

			for (int i = 0; i < bodyDatas.length; i++) {
				bodyCell = bodyRow.createCell(i);
				bodyCell.setCellValue(bodyDatas[i]); // 데이터 추가
				bodyCell.setCellStyle(bodyXssfCellStyle); // 스타일 추가
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
		return "index";
	}
	@RequestMapping("/manyFolder")
	public String oneFolder() {
		String root = "E:\\WORK\\2023우표전시회_AI\\stamp-전종화\\7\\";
		int start = 1352;
		// 299까지
		for(int c=103; c<115; c++) {
			String path = root +  c+ "\\";
			File file = new File(path);
			File[] files = file.listFiles();
			Arrays.sort(files, new ModifiedDate());
			
			List<AIStamp> list = aiRepository.findAllByOrderByIdAsc();
			String bodyDatass[][] = new String[files.length*20][1];
			int idx = 0;
			int imageIdx = 0;
		
			for (int a = start; a < start + 1; a++) {
				for (int x = idx; x < idx + 20; x++) {
					bodyDatass[x][0] = list.get(a).getAnimal() + "_" + list.get(a).getAction() + "_"
							+ list.get(a).getPoint() + "_" + list.get(a).getEnv() + "_" + list.get(a).getStyle() + "_"
							+ list.get(a).getPrompt() + "_" + imageIdx + ".jpg";
					imageIdx++;
					if (imageIdx == 20) {
						imageIdx = 0;
					}
				}
			}
			int iddx = 0;
			String target = "E:\\WORK\\real\\";
			for(File f : files) {
				File newFile = new File(target + bodyDatass[iddx][0]);
				f.renameTo(newFile);
				iddx++;
			}
			start++;
		}
		
		return "index";
	}
	
	
	@RequestMapping("/change")
	public String change() {
		
		String path = "E:\\WORK\\2023우표전시회_AI\\stamp-전종화\\5\\";
		File file = new File(path);
		File[] files = file.listFiles();
		Arrays.sort(files, new ModifiedDate());
		List<AIStamp> list = aiRepository.findAllByOrderByIdAsc();
		String bodyDatass[][] = new String[list.size()*20][1];
		int idx = 0;
		int imageIdx = 0;
		for (int a = 1326; a <1339; a++) {
			for (int x = idx; x < idx + 20; x++) {
				bodyDatass[x][0] = list.get(a).getAnimal() + "_" + list.get(a).getAction() + "_"
						+ list.get(a).getPoint() + "_" + list.get(a).getEnv() + "_" + list.get(a).getStyle() + "_"
						+ list.get(a).getPrompt() + "_" + imageIdx + ".jpg";
				imageIdx++;
				if (imageIdx == 20) {
					imageIdx = 0;
				}
			}
			idx = idx + 20;
		}
		int iddx = 0;
		String target = "E:\\WORK\\real\\";
		for(File f : files) {
			File newFile = new File(target + bodyDatass[iddx][0]);
			f.renameTo(newFile);
			iddx++;
		}
		return "index";
	}
}
