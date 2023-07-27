package com.education_platform.service;

import com.education_platform.data.CourseRepository;
import com.education_platform.data.TestRepository;
import com.education_platform.data.UserCourseRepository;
import com.education_platform.data.UserRepository;
import com.education_platform.model.Course;
import com.education_platform.model.Test;
import com.education_platform.model.User;
import com.education_platform.model.UserCourse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class CertificateService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserCourseRepository userCourseRepository;

    @Autowired
    TestRepository testRepository;


    public static byte[] makeCertificate() throws DocumentException, IOException {
//        String mark = String.valueOf(countCourseMark(user.getId(), course.getId()));
//        String participantName = user.getName() + " " + user.getSurname();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A5.rotate());

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            BaseFont baseFont = BaseFont.createFont("Helvetica", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

            Font titleFont = new Font(baseFont, 24);
            Font textFont = new Font(baseFont, 16);
            Font blueFont = new Font(baseFont, 20);
            Font nameFont = new Font(baseFont, 24);

            Paragraph title = new Paragraph("Learniverse certificate", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            Paragraph certificateText = new Paragraph("This certificate is issued for successful completion of the course:", textFont);
            certificateText.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(certificateText);

            Paragraph courseName = new Paragraph("«"  + "»", blueFont);
            courseName.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(courseName);

            Paragraph scoreText = new Paragraph("With a score of: " +  "%", textFont);
            scoreText.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(scoreText);



        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }

    public byte[] generationPDF(String nameCertificate, String nameAndSurname, float grade, float maxGrade) {
        String text = "Це текст для PDF файлу.";

        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
            contentStream.newLineAtOffset(100, 700); // Координати тексту на сторінці
            contentStream.showText(text);
            contentStream.endText();

//            File imageFile = new File(imagePath);
//            PDImageXObject image = PDImageXObject.createFromFileByContent(imageFile, document);
//            contentStream.drawImage(image, 100, 500); // Координати фотографії на сторінці

            contentStream.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            document.close();

            System.out.println("PDF файл збережено в базу даних успішно.");
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.println("Помилка під час генерації або збереження PDF файлу в базу даних: " + e.getMessage());
        }
        return null;
    }


    public float getMaxGradeForCourse(Long course_id){
        float grade = 0;
        List<Test> tests = testRepository.findAllByModule_CourseId(course_id);
        for(Test test:tests){
            grade += test.getMaxGrade();
        }
        return grade;
    }

}
