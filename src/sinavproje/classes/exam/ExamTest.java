package sinavproje.classes.exam;

import sinavproje.Main;
import sinavproje.classes.question.Question;
import sinavproje.types.ExamType;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExamTest extends Exam {
    private List<Question> questions = new ArrayList<Question>();

    public ExamTest(String name, List<Question> questions) {
        super(name);
        setType(ExamType.TEST);
        generateQuestions(questions);
    }

    @Override
    public List<Question> getQuestions() {
        return questions;
    }

    @Override
    protected void generateQuestions(List<Question> questions) {
        this.questions.clear();
        List<Question> testQuestions = Main.data.search("", "", new String[]{"Çoktan Seçmeli"}, "", "", "");
        for(int i = 0; i < 10; i++)
            Collections.shuffle(testQuestions);
        int puan = 0;
        for(Question q : testQuestions) {
            if(puan >= 100) break;
            if(puan + q.getPoint() >= 110) continue;
            this.questions.add(q);
            puan += q.getPoint();
        }
        if(puan < 100) {
            this.questions.clear();
        }
    }

    @Override
    public boolean exportExam(UserExam user, File exam) {
        try {
            int total = user.getExam().getTotalPoint();
            int[] data = user.getExamPoints();
            Writer writer = new OutputStreamWriter(new FileOutputStream(exam), "UTF-8");
            BufferedWriter f = new BufferedWriter(writer);
            f.write("Sınav Adı: " + this.getName() + "\r\n");
            f.write("Sınav Tip: Test\r\n");
            f.write(String.format("Alınan Puan: %d/%d\r\n", data[0], total));
            f.write(String.format("Doğru/Yanlış/Boş: %d/%d\r\n", data[1], data[2], data[3]));
            f.write("\r\n");
            f.write("#".repeat(134));
            f.write("\r\n\r\n");
            for(Question q : questions) {
                int qID = this.questions.indexOf(q);
                f.write(q.getExamExportForm(qID, user.getAnswer(qID)));
                f.write("\r\n\r\n");
                f.write("#".repeat(134));
                f.write("\r\n\r\n");
            }
            f.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
