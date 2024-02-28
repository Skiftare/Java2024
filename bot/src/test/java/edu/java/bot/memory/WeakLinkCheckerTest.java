package edu.java.bot.memory;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WeakLinkCheckerTest {

    @Test
    public void testThatGetCorrectLinkAndReturnedApprovalOfCorrect() {
        // Given
        String weakLink = "https://stackoverflow.com";

        // When
        boolean result = WeakLinkChecker.checkLinkWithoutConnecting(weakLink);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    public void testThatGetIncorrectLinkAndReturnedApprovalOfInvalid() {
        // Given
        String strongLink = "https://www.google.com";

        // When
        boolean result = WeakLinkChecker.checkLinkWithoutConnecting(strongLink);

        // Then
        assertThat(result).isFalse();
    }

}
