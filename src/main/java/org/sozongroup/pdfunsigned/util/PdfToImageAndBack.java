package org.sozongroup.pdfunsigned.util;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.sozongroup.pdfunsigned.common.HandleResult;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * @Author Xsgard
 * @Description PDF转换
 * @Date 2024/12/25 16:11
 * @Version 1.0
 */
@Slf4j
public class PdfToImageAndBack {
    public static List<String> convertPdfToImages(String pdfPath, String outputFolder) throws IOException {
        File pdfFile = new File(pdfPath);
        PDDocument document = PDDocument.load(pdfFile);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        List<String> imagePaths = new ArrayList<>();

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300); // 渲染为 300 DPI 的图片
            String imagePath = outputFolder + "/page-" + (page + 1) + ".png";
            ImageIO.write(bufferedImage, "PNG", new File(imagePath));
            imagePaths.add(imagePath);
        }

        document.close();
        return imagePaths;
    }

    // 将图片生成新的 PDF
    public static void createPdfFromImages(List<String> imagePaths, String outputPdfPath) throws IOException {
        PdfWriter writer = new PdfWriter(outputPdfPath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        for (String imagePath : imagePaths) {
            ImageData imageData = ImageDataFactory.create(imagePath);
            Image pdfImage = new Image(imageData);

            // 自适应页面大小
            pdfImage.scaleToFit(pdfDoc.getDefaultPageSize().getWidth() - 50, pdfDoc.getDefaultPageSize().getHeight() - 50);
            document.add(pdfImage);
        }

        document.close();
    }

    public static HandleResult handlePDF(String sourcePath) {
        File sourceFile = new File(sourcePath);
        String outputPdf = sourcePath.replace(".pdf", "(图片版).pdf"); // 新生成的 PDF 文件

        try {
            // 创建输出文件夹
            File folder = new File(sourceFile.getParent(), "images");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // 第一步：将 PDF 转换为图片
            List<String> imagePaths = convertPdfToImages(sourcePath, folder.getAbsolutePath());
            log.info("PDF 已成功转换为图片: {}", imagePaths);

            // 第二步：将图片生成新的 PDF
            createPdfFromImages(imagePaths, outputPdf);
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            folder.delete();
            log.info("PDF 文件转换完成: {}", outputPdf);
            return HandleResult.success("成功", "PDF转换为图片模式，已生成至原路径中！");

        } catch (IOException e) {
            System.err.println("处理 PDF 时出现错误: " + e.getMessage());
            return HandleResult.success("错误", "转换出现错误，请稍后重试！");
        }
    }
}
