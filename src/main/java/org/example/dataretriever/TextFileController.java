package org.example.dataretriever;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TextFileController {

    private static final String FILE_PATH = "data/uploadedTextFile.txt";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Path path = Paths.get(FILE_PATH);
            Files.write(path, file.getBytes());
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading file.");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addLine(@RequestParam("newLine") String newLine) {
        try {
            Path path = Paths.get(FILE_PATH);
            List<String> lines = Files.readAllLines(path);
            lines.add(newLine);
            Files.write(path, lines);
            return ResponseEntity.ok("Line added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error adding line.");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateLine(@RequestParam("lineNumber") int lineNumber,
                                             @RequestParam("updatedLine") String updatedLine) {
        try {
            Path path = Paths.get(FILE_PATH);
            List<String> lines = Files.readAllLines(path);
            if (lineNumber < 1 || lineNumber > lines.size()) {
                return ResponseEntity.badRequest().body("Invalid line number.");
            }
            // Update the line at the specified index
            lines.set(lineNumber - 1, updatedLine);
            Files.write(path, lines);
            return ResponseEntity.ok("Line updated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error updating line.");
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteLine(@RequestParam("lineNumber") int lineNumber) {
        try {
            Path path = Paths.get(FILE_PATH);
            List<String> lines = Files.readAllLines(path);
            if (lineNumber < 1 || lineNumber > lines.size()) {
                return ResponseEntity.badRequest().body("Invalid line number.");
            }
            // Remove the line at the specified index
            lines.remove(lineNumber - 1);
            Files.write(path, lines);
            return ResponseEntity.ok("Line deleted successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error deleting line.");
        }
    }

    @GetMapping("/view")
    public ResponseEntity<String> viewFileContent() {
        try {
            Path path = Paths.get(FILE_PATH);
            String content = Files.readAllLines(path).stream().collect(Collectors.joining("\n"));
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error reading file.");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<String> searchInFile(@RequestParam("query") String query) {
        try {
            Path path = Paths.get(FILE_PATH);
            List<String> lines = Files.readAllLines(path);
            String result = lines.stream()
                    .filter(line -> line.contains(query))
                    .collect(Collectors.joining("\n"));
            return ResponseEntity.ok(result.isEmpty() ? "No matches found." : result);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error searching in file.");
        }
    }
}
