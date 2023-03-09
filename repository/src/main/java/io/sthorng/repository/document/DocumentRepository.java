package io.sthorng.repository.document;

import io.sthorng.domain.entity.Document;
import io.sthorng.domain.entity.TitleFormat;
import io.sthorng.domain.enums.Order;
import io.sthorng.domain.port.DocumentPort;
import jakarta.xml.bind.JAXBElement;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.docx4j.XmlUtils;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.STBrType;
import org.docx4j.wml.Text;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DocumentRepository implements DocumentPort {

    private ObjectFactory objectFactory = new ObjectFactory();

    @Override
    @SneakyThrows
    public Document generate(Document document) {

        List<File> files = Arrays.stream(new File(document.getConfig().getScanDirectory()).listFiles()).collect(Collectors.toList());

        files = switch (document.getConfig().getSort()) {
            case ALPHABETIC -> files.stream().sorted().collect(Collectors.toList());
            case CREATE_DATE ->
                    files.stream().sorted((f1, f2) -> getFileAttributes(f1).compareTo(getFileAttributes(f2))).collect(Collectors.toList());
            case UPDATE_DATE ->
                    files.stream().sorted(Comparator.comparingLong(File::lastModified)).collect(Collectors.toList());
        };

        if (document.getConfig().getOrder() == Order.DESC) {
            Collections.reverse(files);
        }

        List<String> title = files.stream()
                .map(f -> f.getName().replaceAll("\\.\\w+$", "")
                        .replaceAll("^\\d+", ""))
                .map(s -> {
                            String ss = s;
                            for (TitleFormat format : document.getConfig().getFormats()) {
                                ss = ss.replaceAll(format.getRegex(), format.getTo());
                            }
                            return ss;
                        }
                ).toList();

        _generate(new ArrayDeque<>(title), document.getConfig().getTemplate(), document.getFilename(), 1);


        return document;
    }

    private FileTime getFileAttributes(File file) {
        try {
            return Files.readAttributes(file.toPath(), BasicFileAttributes.class).creationTime();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void _generate(ArrayDeque<String> titles, String template, String name, int startNumber) throws Exception {
        val source = Paths.get(template);
        val tmp = Files.copy(source, Paths.get("tmp.docx"), StandardCopyOption.REPLACE_EXISTING);
        val target = WordprocessingMLPackage.load(tmp.toFile());

        val mainDocumentPart = target.getMainDocumentPart();
        VariablePrepare.prepare(target);

        Map<String, String> variables = new HashMap<>();
        for (int i = 1; i <= 100; i++) {
            val index = StringUtils.leftPad(String.valueOf(i), 4, '0');
            variables.put("n" + index, "${n" + index + "}");
            variables.put("t" + index, "${t" + index + "}");
        }

        mainDocumentPart.variableReplace(variables);
        val textNodesXPath = "//w:t";

        val textNodes = mainDocumentPart.getJAXBNodesViaXPath(textNodesXPath, true);

        String fullText = String.join("", textNodes.stream()
                .map(it -> ((JAXBElement) it).getValue())
                .map(it -> ((Text) it).getValue())
                .toList());

        int itemsPerPage = Pattern.compile("\\$\\{n\\d{4}\\}").matcher(fullText).results().map(MatchResult::group).mapToInt(f -> Integer.valueOf(f.trim().subSequence(3, 7).toString())).max().orElseThrow();

        val nbTotalPage = titles.size() / itemsPerPage + 1;

        log.debug("item per page : {} ; nb total page : {}", itemsPerPage, nbTotalPage);

        var deepCopy = XmlUtils.deepCopy(target.getMainDocumentPart().getContents());
        var countItem = 1;
        var countPage = 1;
        while (countPage <= nbTotalPage) {
            variables = new HashMap();
            if (countPage != 1) {
                deepCopy.getContent().forEach(it -> mainDocumentPart.getContent().add(it));
            }

            for (int i = 1; i <= itemsPerPage; i++) {
                val index = StringUtils.leftPad(String.valueOf(i), 4, '0');
                variables.put("n" + index, String.valueOf(countItem + startNumber - 1));
                countItem++;
                val text = titles.isEmpty() ? "" : titles.removeFirst();
                variables.put("t" + index, text);
            }
            countPage++;
            addPageBreak(mainDocumentPart);
            mainDocumentPart.variableReplace(variables);

        }
        val out = new FileOutputStream(name);
        target.save(out);
    }

    private void addPageBreak(MainDocumentPart main) {
        val p = objectFactory.createP();
        // Create object for r
        val r = objectFactory.createR();
        p.getContent().add(r);
        // Create object for br
        val br = objectFactory.createBr();
        r.getContent().add(br);
        br.setType(STBrType.PAGE);
        main.addObject(p);
    }
}
