package work.racka.reluct.common.domain.usecases

import work.racka.reluct.common.domain.usecases.authentication.AuthVerifications
import work.racka.reluct.common.domain.usecases.authentication.EmailResult
import work.racka.reluct.common.domain.usecases.authentication.PasswordResult
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthVerificationsTest {
    private val validator = AuthVerifications()
    private val email: (email: String) -> EmailResult = validator::validateEmail
    private val password: (password: String) -> PasswordResult = validator::validatePassword

    @Test
    fun validateEmail_WhenWrongEmailInput_ReturnRelatedResult() {
        // Blank Email
        assertEquals(email("  "), EmailResult.BLANK, "Blank Email Test Fail")
        // Wrong Email 1
        assertEquals(email(EMAIL_WRONG1), EmailResult.INVALID, "Wrong Email 1 Test Fail")
        // Wrong Email 2
        assertEquals(email(EMAIL_WRONG2), EmailResult.INVALID, "Wrong Email 2 Test Fail")
        // Wrong Email 3
        assertEquals(email(EMAIL_WRONG3), EmailResult.INVALID, "Wrong Email 2 Test Fail")
    }

    @Test
    fun validateEmail_WhenCorrectEmailInput_ReturnValidEmailResult() {
        // Correct Email
        assertEquals(email(EMAIL_CORRECT), EmailResult.VALID, "Correct Email Test Fail")
    }

    @Test
    fun validatePassword_WhenPasswordIncorrect_ReturnRelatedPasswordResult() {
        // Incorrect Length 1
        assertEquals(
            password("  "),
            PasswordResult.INCORRECT_LENGTH,
            "Blank Password Test Fail"
        )
        // Incorrect Length 2
        assertEquals(
            password(PASSWORD_SHORT),
            PasswordResult.INCORRECT_LENGTH,
            "Incorrect Length Test Fail"
        )
        // Missing Digits
        assertEquals(
            password(PASSWORD_DIGIT_MISSING),
            PasswordResult.DIGITS_LETTERS_ABSENT,
            "Missing Digits Test Fail"
        )
        // Missing Letters
        assertEquals(
            password(PASSWORD_LETTER_MISSING),
            PasswordResult.DIGITS_LETTERS_ABSENT,
            "Missing Letters Test Fail"
        )
    }

    @Test
    fun validatePassword_WhenPasswordCorrect_ReturnValidPasswordResult() {
        // Incorrect Length 1
        assertEquals(
            password(PASSWORD_CORRECT),
            PasswordResult.VALID,
            "Correct Password Test Fail"
        )
    }

    companion object {
        private const val EMAIL_WRONG1 = "hey_lo@gmail."
        private const val EMAIL_WRONG2 = "_hey_lo@"
        private const val EMAIL_WRONG3 = "_hey_lo@gmail.co.cz"
        private const val EMAIL_CORRECT = "hello@example.com"

        private const val PASSWORD_SHORT = "waffle"
        private const val PASSWORD_DIGIT_MISSING = "waffle-waffle"
        private const val PASSWORD_LETTER_MISSING = "12345678"
        private const val PASSWORD_CORRECT = "kotlinlover92@@"
    }
}