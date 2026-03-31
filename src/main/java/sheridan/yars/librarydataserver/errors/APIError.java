package sheridan.yars.librarydataserver.errors;

public record APIError(int status, String error, String message){}
