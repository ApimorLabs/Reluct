package work.racka.reluct.common.domain.usecases.authentication

import work.racka.reluct.common.model.domain.authentication.EmailResult
import work.racka.reluct.common.model.domain.authentication.PasswordResult

class AuthVerifications {

    fun validateEmail(email: String): EmailResult {
        if (email.isBlank()) return EmailResult.BLANK
        if (!EMAIL_REGEX.toRegex().matches(email)) return EmailResult.INVALID
        return EmailResult.VALID
    }

    fun validatePassword(password: String): PasswordResult {
        if (password.length < 8) return PasswordResult.INCORRECT_LENGTH
        val containsDigitsAndLetters = password.any { it.isDigit() } &&
            password.any { it.isLetter() }
        if (!containsDigitsAndLetters) return PasswordResult.DIGITS_LETTERS_ABSENT
        return PasswordResult.VALID
    }

    companion object {
        const val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    }
}
