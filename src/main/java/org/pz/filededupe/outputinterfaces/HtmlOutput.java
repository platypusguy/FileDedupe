package org.pz.filededupe.outputinterfaces;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

import org.pz.filededupe.LongStringListTable;

/**
 * Prints/persists duplicates and final count to html file
 *
 * Filename format: dupes-yyyy-MM-dd---HH-mm-ss.html, where yyyy-MM-dd---HH-mm-ss is the date and time of execution
 * File location: current user directory
 *
 * Process:
 * 1. Create html file and add html header and add <body> without closing tag
 * 2. Iterate over duplicates and append after <body> tag, for performance reasons we keep {@link PrintWriter} open
 * during iteration and close it in the end of iteration
 * 3. Append final count after processing duplicates
 * 4. Append closing </body> and </html> tags
 *
 * @author dsaraiva
 */
public class HtmlOutput extends DupesOutput implements OutputInterface {

    private final Clock clock;
    private final Path resultFilePath;

    public HtmlOutput(Clock clock) {
        this.clock = clock;
        this.resultFilePath = createResultFilePath();
    }

    @Override
    public void output(LongStringListTable dupes) {
        initHtml();
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(resultFilePath, APPEND))) {
            super.output(dupes, getDupesConsumer(writer), getFinalCountConsumer(writer));
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeTagsHtml();
    }

    private Consumer<List<String>> getDupesConsumer(PrintWriter pw) {
        return paths -> {
            pw.println("<p>" + THESE_FILES_ARE_THE_SAME + "</p>");
            for( String filepath : paths) {
                pw.println(
                        "<a href=\"file://" + filepath + "\">" +
                                "<BLOCKQUOTE>" + filepath + "</BLOCKQUOTE>" +
                                "</a>");
            }
            pw.println();
        };
    }

    private Consumer<Integer> getFinalCountConsumer(PrintWriter pw) {
        return dupesCount -> pw.println("<p>" + NUMBER_OF_DUPLICATES_FOUND + dupesCount + "</p>");
    }

    /**
     * 1. Creates an HTML file
     * 2. Adds the header
     * 3. Adds the tag <body> without closing
     */
    private void initHtml() {
        String htmlTemplateUpper = getHtmlTemplateUpper();
        closeableWriteToFile(resultFilePath, htmlTemplateUpper, CREATE);
    }

    /**
     * Adds close tag for "body" and "html"
     */
    private void closeTagsHtml() {
        String htmlTemplateBottom = getHtmlTemplateBottom();
        closeableWriteToFile(resultFilePath, htmlTemplateBottom, APPEND);
    }

    private Path createResultFilePath() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd---HH-mm-ss");
        String resultFileName = "dupes-" + LocalDateTime.now(clock).format(formatter) + ".html";
        return Paths.get(resultFileName);
    }

    private static void closeableWriteToFile(Path path, String str, OpenOption option)  {
        byte[] strToBytes = str.getBytes(StandardCharsets.UTF_8);

        try {
            Files.write(path, strToBytes, option);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getHtmlTemplateUpper() {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n" +
                "        \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <title>Duplicated Files</title>\n" +
                "</head>\n" +
                "<body>\n";
    }

    private static String getHtmlTemplateBottom() {
        return "</body>\n" +
                "</html>";
    }
}
