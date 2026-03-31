package sheridan.yars.librarydataserver.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Book {
    @Id
    private String itemId;
    private String isbn;
    private String bookTitle;
    private int pageCount;
    private boolean isAvailable;
    private double lateFeeUsd;
}
