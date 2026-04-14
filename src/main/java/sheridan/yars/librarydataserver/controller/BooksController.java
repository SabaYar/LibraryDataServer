package sheridan.yars.librarydataserver.controller;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sheridan.yars.librarydataserver.data.Book;
import sheridan.yars.librarydataserver.data.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/books", produces = "application/json")
@Tag(name = "Books", description = "Endpoints for managing books")
public class BooksController {
    private final BookRepository bookRepository;

    public BooksController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Retrieve all books", description = "Returns a list of all books")
    public List<Book> getAllBooks() {
        log.trace("getAllBooks() is called");
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a book by ID", description = "Returns a book with the specified ID")
    @Parameters(
            @Parameter(name = "id", description = "The ID of the book to retrieve", required = true, example = "BK-01")
    )
    public ResponseEntity<Book> getBookById(@PathVariable String id) throws NoResourceFoundException {
        log.trace("getBookById() is called with id={}", id);
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoResourceFoundException(HttpMethod.GET, null, "/api/books/" + id));
    }

    @PostMapping
    @Operation(summary = "Add a new book", description = "Returns OK response if book object unique item ID is given")
    @Parameters(
            @Parameter(name = "book", description = "The Book object to add", required = true)
    )
    public ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        log.trace("addBook() is called with book={}", book);
        if (bookRepository.existsById(book.getItemId())) {
            throw new RuntimeException("Book with this ID already exists");
        }
        return ResponseEntity.ok(bookRepository.save(book));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing book", description = "Deletes the book attached to the given item ID")
    @Parameters(
            @Parameter(name = "id", description = "The item ID of the book to delete", required = true)
    )
    public ResponseEntity<Void> deleteBook(@PathVariable String id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with given item ID");
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit an existing book", description = "Edits book object of given item ID")
    @Parameters({
            @Parameter(name = "id", description = "The item ID of the book to edit", required = true),
            @Parameter(name = "editedBook", description = "The Book object with the edited information", required = true)
    })
    public ResponseEntity<Book> editBook(@PathVariable String id, @Valid @RequestBody Book editedBook) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setIsbn(editedBook.getIsbn());
                    book.setBookTitle(editedBook.getBookTitle());
                    book.setPageCount(editedBook.getPageCount());
                    book.setAvailable(editedBook.isAvailable());
                    book.setLateFeeUsd(editedBook.getLateFeeUsd());
                    return ResponseEntity.ok(bookRepository.save(book));
                })
                .orElseThrow(() -> new RuntimeException("Book not found with given item ID"));
    }
}
