package org.sozongroup.pdfunsigned.util;


import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.sozongroup.pdfunsigned.common.HandleResult;

import java.io.File;
import java.io.IOException;

/**
 * @Author Xsgard
 * @Description PDF工具类
 * @Date 2024/12/25 10:56
 * @Version 1.0
 */
@Slf4j
public class PdfUnsignedUtils {
    public static HandleResult removePDFSignature(String sourcePath) {
        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists() || !sourceFile.isFile()) //原文件校验
            return HandleResult.error("错误", "原文件不存在，请重新选择文件！");
        else if (!sourceFile.getName().endsWith(".pdf"))
            return HandleResult.error("错误", "您选择的不是一个PDF文件，请重新选！");

        File outputFile = new File(sourceFile.getAbsolutePath().replace(".pdf", "(破解版).pdf"));

        try {
            // 创建 PdfReader 并忽略加密
            PdfReader reader = new PdfReader(sourceFile);
            reader.setUnethicalReading(true);

            // 创建 PdfWriter 和 PdfDocument
            PdfWriter writer = new PdfWriter(outputFile);
            PdfDocument inputDoc = new PdfDocument(reader);
            PdfDocument outputDoc = new PdfDocument(writer);
            // 遍历所有页面
            for (int i = 1; i <= inputDoc.getNumberOfPages(); i++) {
                PdfPage page = inputDoc.getPage(i);

                // 移除页面上的所有注解
                page.getAnnotations().clear();
            }

            // 复制所有页面到新文档
            inputDoc.copyPagesTo(1, inputDoc.getNumberOfPages(), outputDoc);

            // 关闭文档
            inputDoc.close();
            outputDoc.close();
            log.info("成功生成无签名限制的 PDF 文件: {}", outputFile.getAbsolutePath());
            return HandleResult.success("成功", "文件解密成功，已生成至原路径中！");
        } catch (Exception e) {
            log.error(e.getMessage());
            return HandleResult.success("错误", "解密出现错误，请稍后重试！");
        }
    }

}
