package izzy.medium.security.exams.app;

import lombok.RequiredArgsConstructor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
class RiddleXmlReader {
    private final Element root;
    private final Principal principal;

    RiddleXmlReader(InputStream in, Principal principal) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(in);
        doc.getDocumentElement().normalize();
        this.root = doc.getDocumentElement();
        this.principal = principal;
    }

    Riddle read(){
        NodeList childNodes = root.getChildNodes();
        String text = IntStream.range(0, childNodes.getLength())
                .mapToObj(childNodes::item)
                .filter(n -> n.getNodeType() == Node.TEXT_NODE)
                .map(Node::getTextContent)
                .findFirst()
                .orElse("")
                .trim();
        return Riddle.builder()
                .principal(principal)
                .questions(readQuestions(root))
                .text(text)
                .build();
    }


    private List<Question> readQuestions(Element root) {
        NodeList nodes = root.getElementsByTagName("question");
        return IntStream.range(0, nodes.getLength())
                .mapToObj(nodes::item)
                .filter(n -> n.getNodeType() == Node.ELEMENT_NODE)
                .filter(n -> n.getNodeName().equals("question"))
                .map(this::readQuestion)
                .collect(Collectors.toList());
    }

    private Question readQuestion(Node questionNode) {
        int correctSolution = Integer.parseInt(questionNode.getAttributes().getNamedItem("correctSolution").getNodeValue());
        NodeList childNodes = questionNode.getChildNodes();
        List<String> answers = IntStream.range(0, childNodes.getLength())
                .mapToObj(childNodes::item)
                .filter(n -> n.getNodeType() == Node.ELEMENT_NODE)
                .filter(n -> n.getNodeName().equals("answer"))
                .map(Node::getTextContent)
                .map(String::trim)
                .collect(Collectors.toList());
        String text = IntStream.range(0, childNodes.getLength())
                .mapToObj(childNodes::item)
                .filter(n -> n.getNodeType() == Node.TEXT_NODE)
                .map(Node::getTextContent)
                .findFirst()
                .orElse("")
                .trim();
        return Question.builder()
                .answers(answers)
                .correctSolution(correctSolution)
                .text(text)
                .build();
    }
}
