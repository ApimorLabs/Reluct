package work.racka.reluct.common.model.domain.authentication

enum class EmailResult {
    VALID, BLANK, INVALID;
}

enum class PasswordResult {
    VALID, DIGITS_LETTERS_ABSENT, INCORRECT_LENGTH
}
