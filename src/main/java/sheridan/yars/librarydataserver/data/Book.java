package sheridan.yars.librarydataserver.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Book {
    @Id
    @NotBlank(message = "Item ID required")
    private String itemId;
    @NotBlank(message = "ISBN required")
    private String isbn;
    @NotBlank(message = "Book title required")
    private String bookTitle;
    @Positive(message = "Page count should be 1 page minimum")
    private int pageCount;
    @NotBlank(message = "Book availability notice required")
    private boolean isAvailable;
    @PositiveOrZero(message = "Late fee must be $0 USD or higher")
    private double lateFeeUsd;
}
