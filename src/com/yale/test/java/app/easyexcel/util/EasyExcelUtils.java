package com.yale.test.java.app.easyexcel.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.yale.test.java.app.easyexcel.constant.ExcelConstant;

/**
 * 依赖jar:
 * easyexcel-1.1.1.jar
 * poi-3.17.jar
 * poi-ooxml-3.17.jar
 * commons-beanutils-1.9.4-20171008.104537-25.jar
 * commons-compress-1.14.jar
 * xmlbeans-3.1.0.jar
 * commons-collections4-4.4.jar
 * ooxml-schemas-1.4.jar
 * https://juejin.cn/post/7039878722857467940 《还在用 POI？试试 EasyExcel，轻松导出 100W 数据，不卡死，好用到爆！》
 * https://juejin.cn/post/6844903682278948871 《一行代码完成 JAVA 的 EXCEL 读写——EasyExcel 的方法封装 》
 * https://juejin.cn/post/6956953740158959629 《SpringBoot整合EasyExcel实现文件导入导出 》
 * https://juejin.cn/post/7034687529282437133 《Easypoi导出金额字段，格式为数值 》
 * https://juejin.cn/post/6924844622275411975 《什么？你还在用POI导出数据？EasyExcel解决大数据量导出OOM(内存溢出) 》
 * https://juejin.cn/post/6947966513735434270 《EasyExcel实现springboot项目中导入导出 》
 * https://juejin.cn/post/6844903598405468173 《【死磕Java并发】—–J.U.C之AQS（一篇就够了）》
 * https://juejin.cn/post/6869914176941359118 《不要再重复造轮子了，这款开源工具类库贼好使！》
 * https://juejin.cn/post/6990743443563610126 《java实现利用阿里巴巴开源的easyexcel进行对excel表格的导入和导出[附完整代码] 》
 * https://juejin.cn/post/7007245485001932814 《这篇 Java 基础，我吹不动了 》
 * https://juejin.cn/post/6844903906296725518 《漫话：如何给女朋友解释什么是CDN？ 》
 * https://juejin.cn/post/6844903621990055949 《一个正则表达式引发的血案，让线上CPU100%异常！》
 * https://juejin.cn/post/6966084939309645838 《导入导出这么丝滑，你用的是 EasyPoi 吗？ 》
 * https://juejin.cn/post/7023180836354654245 《SpringBoot实现Excel导入导出，好用到爆，POI可以扔掉了！》
 * https://juejin.cn/post/7023649189196201992 《阿里EasyExcel让你彻底告别easypoi 》
 * 有一点推荐一下：在做分页的时候使用单表查询， 对于所需要处理的外键对应的冗余字段，在外面一次性查出来放到map里面(推荐使用@MapKey注解)，然后遍历list的时候根据外键从map中获取对应的名称。
 * 一个宗旨：少发查询sql, 才能更快的导出。
 * 题外话：如果数据量过大，在使用count(1)查询总数的时候会很慢，可以通过调整mysql的缓冲池参数来加快查询。
 * 还有就是遇到了一个问题，使用pagehelper的时候，数据量大的时候，limit 0,20W, limit 20W,40W, limit 40W,60W, limit 60W,80W 查询有的时候会很快，有的时候会很慢，待研究。
 * @author issuser
 */
public class EasyExcelUtils {
	
	/**
	 * 数据量少的(20W以内吧)：一个SHEET一次查询导出
	 * 针对较少的记录数(20W以内大概)可以调用该方法一次性查出然后写入到EXCEL的一个SHEET中
	 * 注意： 一次性查询出来的记录数量不宜过大，不会内存溢出即可。
	 * @throws IOException 
	 */
	public void writeExcelOneSheetOnceWriter() throws IOException {
		//生成EXCEL并指定输出路径
		OutputStream out = new FileOutputStream("E:\\temp\\withoutHead11.xlsx");
		ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
		
		//设置SHEET
		Sheet sheet = new Sheet(1, 0);
		sheet.setSheetName("sheet1");
		
		//设置标题，其实可以使用TableStyle设置表格样式
		Table table = new Table(1);
		List<List<String>> titles = new ArrayList<List<String>>();
		titles.add(Arrays.asList("用户ID","Head可以占俩行"));
		titles.add(Arrays.asList("名称"));
		titles.add(Arrays.asList("年龄"));
		titles.add(Arrays.asList("生日"));
		//table.setTableStyle(null);设置表格样式
		table.setHead(titles);
		
		//查询数据导出即可,比如说一次性总共查询出100条数据
		List<List<String>> userList = new ArrayList<>();
		for (int i=0; i<100; i++) {
			userList.add(Arrays.asList("ID_" + i, "小明" + i, String.valueOf(i), new Date().toString()));
		}
		writer.write0(userList, sheet, table);
		writer.finish();
		out.close();
	}
	
	/**
	 * 数据量适中（100W以内）：一个SHEET分批查询导出
	 * 针对105W以内的记录数可以调用该方法分多批次查出然后写入到excel的一个sheet中
	 * 注意：
	 * 每次查询出来的记录数量不宜过大,根据内存大小设置合理的每次查询记录数,不会内存溢出即可，还要防止频繁GC。
	 * 数据量不能超过一个SHEET存储的最大数量105W。
	 * @throws IOException 
	 */
	public void writeExcelOneSheetMoreWrite() throws IOException {
		//生成EXCEL并指定输出路径
		OutputStream out = new FileOutputStream("E:\\temp\\withoutHead2.xlsx");
		ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
		
		//设置SHEET
		Sheet sheet = new Sheet(1, 0);
		sheet.setSheetName("sheet1");
		
		//设置标题
		Table table = new Table(1);
		List<List<String>> titles = new ArrayList<List<String>>();
		titles.add(Arrays.asList("用户ID","可以有俩个"));
		titles.add(Arrays.asList("名称"));
		titles.add(Arrays.asList("年龄"));
		titles.add(Arrays.asList("生日"));
		table.setHead(titles);
		
		//模拟分批查询:总记录数50条,每次查询20条,分三次查询最后一次查询记录数是10
		int totalRowCount = 50;
		int pageSize = 20;
		int writeCount = totalRowCount % pageSize == 0 ? (totalRowCount / pageSize) : (totalRowCount / pageSize +1);
		
		//注：此处仅仅为了模拟数据，实用环境不需要将最后一次分开,合成一个即可,参数为:currentPage = i+1;pageSize=pageSize
		for (int i=0; i< writeCount; i++) {
			//前俩次查询每次查询20条
			if (i < writeCount -1) {
				List<List<String>> userList = new ArrayList<>();
				for (int j=0; j<pageSize; j++) {
					userList.add(Arrays.asList("ID_" + Math.random(), "小明", String.valueOf(Math.random()), new Date().toString()));
				}
				writer.write0(userList, sheet, table);
			} else if (i == (writeCount -1)) {
				//最后一次查询，查多余的10条记录
				List<List<String>> userList = new ArrayList<>();
				Integer lastWriteRowCount = totalRowCount - (writeCount -1) * pageSize;
				for (int j=0; j<lastWriteRowCount; j++) {
					userList.add(Arrays.asList("ID_" + Math.random(), "小明", String.valueOf(Math.random()), new Date().toString()));
				}
				writer.write0(userList, sheet, table);
			}
		}
		writer.finish();
		out.close();
	}
	
	/**
	 * 数据量很大(几百万都行),多个Sheet分批查询导出
	 * 针对几百万的记录数可以调用该方法分多批次查出然后写入到Excel的多个Sheet中
	 * 注意:perSheetRowCount % pageSize要能整除,为了简洁,非整除这块不做处理
	 * 每次查询出来的记录数量不宜过大,根据内存大小设置合理的每次查询记录数,不会内存溢出即可。
	 */
	public void writeExcelMoreSheetMoreWrite() throws IOException {
		//生成Excel并指定输出路径
		OutputStream out = new FileOutputStream("E:\\temp\\withoutHead3.xlsx");
		ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
		
		//设置SHEET名称
		String sheetName = "测试SHEET";
		
		//设置标题
		Table table = new Table(1);
		List<List<String>> titles = new ArrayList<List<String>>();
		titles.add(Arrays.asList("用户ID", "可以有俩个"));
		titles.add(Arrays.asList("名称"));
		titles.add(Arrays.asList("年龄"));
		titles.add(Arrays.asList("生日"));
		table.setHead(titles);
		
		//模拟分批查询:总记录数250条,每个sheet存100条,每次查询20条,则生成3个SHEET,前俩个SHEET查询次数为5,最后一个sheet查询次数为3
		//最后一次写的记录数是10,注:该版本为了较少数据判断的复杂度,暂时perSheetRowCount要能够整除pageSize,不去做过多处理
		//合理分配查询数据量大小不会内存溢出即可。
		int totalRowCount = 250;
		int perSheetRowCount = 100;
		int pageSize = 20;
		int sheetCount = totalRowCount % perSheetRowCount == 0 ? (totalRowCount / perSheetRowCount) : (totalRowCount / perSheetRowCount +1);
		int previousSheetWriterCount = perSheetRowCount / pageSize;
		int lastSheetWriteCount = totalRowCount % perSheetRowCount == 0 ? previousSheetWriterCount : (totalRowCount % perSheetRowCount % pageSize == 0 ? totalRowCount % perSheetRowCount / pageSize : (totalRowCount % perSheetRowCount / pageSize + 1));
		for (int i=0; i< sheetCount; i++) {
			//创建Sheet
			Sheet sheet = new Sheet(i, 0);
			sheet.setSheetName(sheetName + i);
			if (i < sheetCount -1) {
				//前2个Sheet,每个Sheet查5次，每次查20条每个sheet写满100行,2个sheet合计200行，实用环境：参数currentPage:j+1+previousSheetWriteCount*i,pageSize:pageSize
				for (int j=0; j<previousSheetWriterCount; j++) {
					List<List<String>> userList = new ArrayList<>();
					for (int k=0; k<20; k++) {
						userList.add(Arrays.asList("ID_" + Math.random(), "小明", String.valueOf(Math.random()), new Date().toString()));
					}
					writer.write0(userList, sheet, table);
				}
			} else if (i == (sheetCount -1)) {
				//最后一个SHEET,使用环境不需要将最后一次分开，合成一个即可。
				//参数为:currentPage = i + 1; pageSize = pageSize
				for (int j=0; j<lastSheetWriteCount; j++) {
					//前俩次查询每次查询20条
					if (j < lastSheetWriteCount -1) {
						List<List<String>> userList = new ArrayList<>();
						for (int k=0;k<20;k++) {
							userList.add(Arrays.asList("ID_" + Math.random(), "小明", String.valueOf(Math.random()), new Date().toString()));
						}
						writer.write0(userList, sheet, table);
					} else if (j == (lastSheetWriteCount -1)) {
						//最后一次查询将剩余的10条查询出来
						List<List<String>> userList = new ArrayList<>();
						int lastWriteRowCount = totalRowCount - (sheetCount -1) * perSheetRowCount - (lastSheetWriteCount -1) * pageSize;
						for (int k=0; k< lastWriteRowCount; k++) {
							userList.add(Arrays.asList("ID_" + Math.random(), "小明1", String.valueOf(Math.random()), new Date().toString()));
						}
						writer.write0(userList, sheet, table);
					}
				}
			}
		}
		writer.finish();
		out.close();
	}
	
	//2.4.1.数据量少的(20W以内吧)：一个SHEET一次查询导出
	public ResultVO<Void> exportSysSystemExcel(SysSystemVO sysSystemVO, HttpServletResponse response) throws Exception{
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
			
			//设置EXCEL名称
			String fileName = new String("SystemExcel".getBytes(), "UTF-8");
			
			//设置Sheet名称
			Sheet sheet = new Sheet(1, 0);
			sheet.setSheetName("系统列表sheet1");
			
			//设置标题
			Table table = new Table(1);
			List<List<String>> titles = new ArrayList<List<String>>();
			titles.add(Arrays.asList("系统名称", "你注意Sheet页的名字"));
			titles.add(Arrays.asList("系统标识"));
			titles.add(Arrays.asList("描述"));
			titles.add(Arrays.asList("状态"));
			titles.add(Arrays.asList("创建人"));
			titles.add(Arrays.asList("创建时间"));
			table.setHead(titles);
			
			//查数据写EXCEL
			List<List<String>> dataList = new ArrayList<>();
			List<SysSystemVO> sysSystemVOList = this.sysSystemReadMapper.selectSysSystemVOList(sysSystemVO);
			if(!CollectionUtils.isEmpty(sysSystemVOList)) {
				sysSystemVOList.forEach(eachSysSystemVO -> {
					dataList.add(Arrays.asList(eachSysSystemVO.getSystemName(),
							eachSysSystemVO.getSystemKey(),
							eachSysSystemVO.getDescription(),
							eachSysSystemVO.getState().toString(),
							eachSysSystemVO.getCreateUid(),
							eachSysSystemVO.getCreateTime().toString()));
				});
			}
			writer.write0(dataList, sheet, table);
			//下载Excel 
			response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes("gb2312"), "ISO-8859-1") + ".xls");
			response.setContentType("multipart/form-data");
			response.setCharacterEncoding("utf-8");
			writer.finish();
			out.flush();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ResultVO.getSuccess("导出系统列表EXCEL成功");
	}
	
	//2.4.2.数据量适中（100W以内）：一个SHEET分批查询导出
	public ResultVO<Void> exportSysSystemExcel(SysSystemVO sysSystemVO, HttpServletResponse response) throws Exception {
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
			
			//设置EXCEL名称
			String fileName = new String("SystemExcel".getBytes(), "UTF-8");
			
			//设置Sheet名称
			Sheet sheet = new Sheet(1, 0);
			sheet.setSheetName("系统列表sheet1");
			
			//设置标题
			Table table = new Table(1);
			List<List<String>> titles = new ArrayList<List<String>>();
			titles.add(Arrays.asList("系统名称", "可以有俩个呢"));
			titles.add(Arrays.asList("系统标识"));
			titles.add(Arrays.asList("描述"));
			titles.add(Arrays.asList("状态"));
			titles.add(Arrays.asList("创建人"));
			titles.add(Arrays.asList("创建时间"));
			table.setHead(titles);
			
			//查询总数并[封装相关变量 这块直接拷贝就行 不要改动]
			int totalRowCount = this.sysSystemReadMapper.selectCountSysSystemVOList(sysSystemVO);
			int pageSize = ExcelConstant.PER_WRITE_ROW_COUNT;
			int writeCount = totalRowCount % pageSize == 0 ? (totalRowCount / pageSize) : (totalRowCount / pageSize + 1);
			
			//写数据,这个i的最大值直接拷贝就行了 不要改
			for (int i=0; i < writeCount; i++) {
				List<List<String>> dataList = new ArrayList<>();
				//此处查询并封装数据即可,currentPage,pageSize这个变量封装好的 不要改动
				PageHelper.startPage(i + 1, pageSize);
				List<SysSystemVO> sysSystemVOList = this.sysSystemReadMapper.selectSysSystemVOList(sysSystemVO);
				if (!CollectionUtils.isEmpty(sysSystemVOList)) {
					sysSystemVOList.forEach(eachSysSystemVO -> {
						dataList.add(Arrays.asList(
								eachSysSystemVO.getSystemName(),
								eachSysSystemVO.getSystemKey(),
								eachSysSystemVO.getDescription(),
								eachSysSystemVO.getState().toString(),
								eachSysSystemVO.getCreateUid(),
								eachSysSystemVO.getCreateTime().toString()
								));
					});
				}
				writer.write0(dataList, sheet, table);
			}
			//下载EXCEL
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO-8859-1") + ".xls");
			response.setContentType("multipart/form-data");
			response.setCharacterEncoding("utf-8");
			writer.finish();
			out.flush();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch() {
				
			}
		}
		return ResultVO.getSuccess("导出系统列表EXCEL成功");
	}
	
	//2.4.3.数据里很大（几百万都行）：多个SHEET分批查询导出
	public ResultVO<Void> exportSysSystemExcel(SysSystemVO sysSystemVO, HttpServletResponse response) throws Exception {
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
			
			//设置EXCEL名称
			String fileName = new String("SystemExcel".getBytes(), "UTF-8");
			
			//设置SHEET名称
			String sheetName = "系统列表sheet";
			
			//设置标题
			Table table = new Table(1);
			List<List<String>> titles = new ArrayList<List<String>>();
			titles.add(Arrays.asList("系统名称", "可以有俩行"));
			titles.add(Arrays.asList("系统标识"));
			titles.add(Arrays.asList("描述"));
			titles.add(Arrays.asList("状态"));
			titles.add(Arrays.asList("创建人"));
			titles.add(Arrays.asList("创建时间"));
			table.setHead(titles);
			
			//查询总数并封装相关变量(这块直接拷贝就行了不要改)
			int totalRowCount = this.sysSystemReadMapper.selectCountSysSystemVOList(sysSystemVO);
			int perSheetRowCount = ExcelConstant.PER_SHEET_ROW_COUNT;
			int pageSize = ExcelConstant.PER_WRITE_ROW_COUNT;
			int sheetCount = totalRowCount % perSheetRowCount == 0 ? (totalRowCount / perSheetRowCount) : (totalRowCount / perSheetRowCount + 1);
			int previousSheetWriteCount = perSheetRowCount / pageSize;
			int lastSheetWriteCount = totalRowCount % perSheetRowCount == 0 ? previousSheetWriteCount : (totalRowCount % perSheetRowCount % pageSize == 0 ? totalRowCount % perSheetRowCount / pageSize : (totalRowCount % perSheetRowCount / pageSize + 1));
			for (int i=0; i < sheetCount; i++) {
				//创建sheet
				Sheet sheet  = new Sheet(i, 0);
				sheet.setSheetName(sheetName + i);
				
				//写数据这个j的最大值直接拷贝就行了,不要改动
				for (int j=0; j<(i != sheetCount -1 ? previousSheetWriteCount : lastSheetWriteCount);j++) {
					List<List<String>> dataList = new ArrayList<>();
					
					//此处查询并封装数据即可currentPage, pageSize这个俩个变量封装好的不要改动
					PageHelper.startPage(j + 1 + previousSheetWriteCount * i, pageSize);
					List<SysSystemVO> sysSystemVOList = this.sysSystemReadMapper.selectSysSystemVOList(sysSystemVO);
					if (!CollectionUtils.isEmpty(sysSystemVOList)) {
						sysSystemVOList.forEach(
								eachSysSystemVO -> {
									dataList.add(Arrays.asList(
											eachSysSystemVO.getSystemName(),
											eachSysSystemVO.getSystemKey(),
											eachSysSystemVO.getDescription(),
											eachSysSystemVO.getState().toString(),
											eachSysSystemVO.getCreateUid(),
											eachSysSystemVO.getCreateTime().toString()
											));
								}
						);
					}
					writer.write0(dataList, sheet, table);
				}
			}
			//下载Excel
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO-8859-1") + ".xls");
			response.setContentType("multipart/form-data");
			response.setCharacterEncoding("utf-8");
			writer.finish();
			out.flush();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ResultVO.getSuccess("导出系统列表EXCEL成功");
	}
}
