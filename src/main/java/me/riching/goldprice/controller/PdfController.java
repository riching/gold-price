package me.riching.goldprice.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.itext.extension.IPdfWriterConfiguration;
import freemarker.template.Template;

@Controller
@RequestMapping("pdf")
public class PdfController {

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	private static final String doc = "/Users/riching/Documents/zbomc/海口人民医院-HIS核心系统-20171221-14-日报.doc";

	@RequestMapping("index")
	public String index(Model model) {
		model.addAttribute("title", "这是标题");
		model.addAttribute("content", "这是内容");
		return "pdf.ftl";
	}

	@RequestMapping("download")
	public void downloadPdf(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Template template = freeMarkerConfigurer.createConfiguration().getTemplate("pdf.ftl");
		StringWriter sw = new StringWriter();
		Map<String, Object> map = new HashMap<>();
		map.put("title", "这是标题");
		map.put("content", "this is content");
		template.process(map, sw);
		sw.flush();
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocumentFromString(sw.toString());
		ITextFontResolver fontResolver = renderer.getFontResolver();
		// BaseFont chineseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
		// BaseFont.EMBEDDED);
		fontResolver.addFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
		renderer.layout();
		renderer.createPDF(new FileOutputStream(new File("/tmp/test.pdf")));
	}

	public static void main(String[] args) throws Exception {
		// Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		// cfg.setDirectoryForTemplateLoading(
		// new
		// File("/Users/riching/eclipse-workspace/gold-price/src/main/webapp/WEB-INF/template"));
		// Template template = cfg.getTemplate("pdf.ftl");
		// Map<String, Object> map = new HashMap<>();
		// map.put("title", "this is 这是标题");
		// map.put("content", "this is content2233");
		// String html = FreeMarkerTemplateUtils.processTemplateIntoString(template,
		// map);
		// // System.out.println(html);
		// ITextRenderer renderer = new ITextRenderer();
		// ITextFontResolver fontResolver = renderer.getFontResolver();
		// fontResolver.addFont("/Users/riching/eclipse-workspace/gold-price/src/main/webapp/font/微软雅黑.ttf",
		// "微软雅黑",
		// BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, null);
		// renderer.setDocumentFromString(html);
		// System.out.println(renderer.getDocument());
		// // fontResolver.addFont("STSong-Light", "UniGB-UCS2-H",
		// BaseFont.NOT_EMBEDDED);
		// renderer.layout();
		// // renderer.createPDF(os, finish);
		// renderer.createPDF(new FileOutputStream(new File("/tmp/test.pdf")));
		// renderer.finishPDF();
		// poiTest();
		html2pdf();
	}

	static void html2pdf() throws Exception {
		ITextRenderer renderer = new ITextRenderer();
		ITextFontResolver fontResolver = renderer.getFontResolver();
		fontResolver.addFont("/Users/riching/eclipse-workspace/gold-price/src/main/webapp/font/微软雅黑.ttf",
				"Microsoft YaHei UI Light", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, null);
		renderer.setDocument(new File("/tmp/doc.html"));
		renderer.layout();
		renderer.createPDF(new FileOutputStream(new File("/tmp/test.pdf")));
		renderer.finishPDF();
	}

	static void xdocreportTest() throws Exception {
		XWPFDocument document = new XWPFDocument(new FileInputStream(new File(doc)));
		PdfOptions option = PdfOptions.getDefault();
		IPdfWriterConfiguration config = new IPdfWriterConfiguration() {

			@Override
			public void configure(PdfWriter writer) {

			}
		};
		option.setConfiguration(config);
		PdfConverter.getInstance().convert(document, new FileOutputStream(new File("/tmp/test5.pdf")), option);
		System.out.println("finish");
	}

	static void poiTest() throws Exception {
		HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(new File(doc)));
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

		wordToHtmlConverter.processDocument(wordDocument);

		org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(out);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "HTML");
		serializer.transform(domSource, streamResult);
		out.close();
		org.jsoup.nodes.Document doc = Jsoup.parse(out.toString());
		FileUtils.writeStringToFile(new File("/tmp/poi.html"), doc.html(), "utf-8");
	}
}
